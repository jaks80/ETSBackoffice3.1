package com.ets.accountingdoc.service;

import com.ets.Application;
import com.ets.accountingdoc.dao.TPurchaseAcDocDAO;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.model.BSPReport;
import com.ets.pnr.domain.Ticket;
import com.ets.accountingdoc.model.InvoiceReport;
import com.ets.accountingdoc.model.TktingInvoiceSummery;
import com.ets.client.domain.Agent;
import com.ets.pnr.model.GDSSaleReport;
import com.ets.pnr.service.TicketService;
import com.ets.productivity.model.ProductivityReport;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import com.ets.pnr.logic.PnrUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("tPurchaseAcDocService")
public class TPurchaseAcDocService {

    @Resource(name = "tPurchaseAcDocDAO")
    private TPurchaseAcDocDAO dao;
    @Autowired
    TicketService ticketService;

    public synchronized TicketingPurchaseAcDoc createNewDocument(TicketingPurchaseAcDoc doc) {

        if (doc.getTickets() != null && !doc.getTickets().isEmpty()) {
            AcDocUtil.initTPAcDocInTickets(doc, doc.getTickets());
            PnrUtil.initPnrInTickets(doc.getPnr(), doc.getTickets());
        }

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.initAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        if (!doc.getTickets().isEmpty() || !doc.getAdditionalChargeLines().isEmpty()) {
            doc.setDocumentedAmount(doc.calculateDocumentedAmount());
        }

        doc.setStatus(Enums.AcDocStatus.ACTIVE);
        dao.save(doc);
        return doc;
    }

    public TicketingPurchaseAcDoc saveorUpdate(TicketingPurchaseAcDoc doc) {
        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.initAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        if (!doc.getTickets().isEmpty() || !doc.getAdditionalChargeLines().isEmpty()) {
            doc.setDocumentedAmount(doc.calculateDocumentedAmount());
        }
        dao.save(doc);
        return undefineChildren(doc);
    }

    public String delete(long id) {
        TicketingPurchaseAcDoc document = dao.findByID(TicketingPurchaseAcDoc.class, id);

        if (document.getStatus().equals(Enums.AcDocStatus.VOID)
                && !document.getType().equals(Enums.AcDocType.PAYMENT)
                && !document.getType().equals(Enums.AcDocType.REFUND)) {
            dao.delete(document);
            return "Deleted";
        } else {
            return "Only VOID document can be deleted.";
        }
    }

    public TicketingPurchaseAcDoc _void(TicketingPurchaseAcDoc ticketingPurchaseAcDoc) {
        TicketingPurchaseAcDoc doc = dao.getWithChildrenById(ticketingPurchaseAcDoc.getId());
        doc.setLastModified(ticketingPurchaseAcDoc.getLastModified());
        doc.setLastModifiedBy(ticketingPurchaseAcDoc.getLastModifiedBy());

//        Set<TicketingPurchaseAcDoc> relatedDocs = doc.getRelatedDocuments();
//        if (doc.getType().equals(Enums.AcDocType.INVOICE) && !relatedDocs.isEmpty()) {
//            return doc;
//        } else {
        dao.voidDocument(undefineChildren(doc));
        return doc;
    }

    public TicketingPurchaseAcDoc getWithChildrenById(long id) {
        TicketingPurchaseAcDoc doc = dao.getWithChildrenById(id);
        //validateDocumentedAmount(doc);

        if (doc.getParent() != null) {
            doc.getParent().setAdditionalChargeLines(null);
            doc.getParent().setPayment(null);
            doc.getParent().setPnr(null);
            doc.getParent().setTickets(null);
            doc.getParent().setRelatedDocuments(null);
        }
        return undefineChildren(doc);
    }

    public List<TicketingPurchaseAcDoc> findInvoiceByReference(Long refNo) {
        return dao.findInvoiceByRef(refNo);
    }

    public InvoiceReport findInvoiceSummeryByReference(Long... refNo) {
        List<TicketingPurchaseAcDoc> invoices = dao.findInvoiceByRef(refNo);
        InvoiceReport report = InvoiceReport.serializeToPurchaseSummery(null, invoices, null, null);
        report.setTitle("Invoice Report");
        return report;
    }

    private TicketingPurchaseAcDoc undefineChildren(TicketingPurchaseAcDoc doc) {

        for (Ticket t : doc.getTickets()) {
            t.setTicketingPurchaseAcDoc(null);
        }

        Set<TicketingPurchaseAcDoc> relatedDocs = AcDocUtil.filterPVoidRelatedDocuments(doc.getRelatedDocuments());
        doc.setRelatedDocuments(relatedDocs);
        for (TicketingPurchaseAcDoc r : relatedDocs) {
            if (r.getType().equals(Enums.AcDocType.PAYMENT) || r.getType().equals(Enums.AcDocType.REFUND)) {
                AcDocUtil.undefineTPAcDocumentInPayment(r);
            }
            AcDocUtil.undefineAddChgLine(r, r.getAdditionalChargeLines());
        }
        //doc.setRelatedDocuments(relatedDocs);
        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.undefineAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        PnrUtil.undefinePnrInSegments(doc.getPnr(), doc.getPnr().getSegments());
        doc.getPnr().setTickets(null);
        doc.getPnr().setRemarks(null);

        return doc;
    }

    public List<TicketingPurchaseAcDoc> getByGDSPnr(String pnr) {
        List<TicketingPurchaseAcDoc> list = dao.getByGDSPnr(pnr);
        return list;
    }

    public TicketingPurchaseAcDoc findInvoiceByPnrId(Long pnrId) {

        List<TicketingPurchaseAcDoc> docs = getByPnrId(pnrId);
        TicketingPurchaseAcDoc invoice = null;

        for (TicketingPurchaseAcDoc doc : docs) {
            if (doc.getType().equals(Enums.AcDocType.INVOICE)) {
                invoice = doc;
                break;
            }
        }
        return invoice;
    }

    public List<TicketingPurchaseAcDoc> getByPnrId(Long pnrId) {
        List<TicketingPurchaseAcDoc> list = dao.getByPnrId(pnrId);
        for (TicketingPurchaseAcDoc a : list) {
            a.setRelatedDocuments(null);
            if (a.getType().equals(Enums.AcDocType.PAYMENT) || a.getType().equals(Enums.AcDocType.REFUND)) {
                if (a.getParent() != null) {
                    AcDocUtil.undefineTPAcDocumentInPayment(a);
                }
            }
            for (Ticket t : a.getTickets()) {
                t.setTicketingPurchaseAcDoc(null);
            }
            AcDocUtil.undefineAddChgLine(a, a.getAdditionalChargeLines());
        }
        return list;
    }

//    public void delete(TicketingPurchaseAcDoc document) {
//        dao.delete(document);
//    }
    public InvoiceReport invoiceHistoryReport(Long agentid, Date dateStart, Date dateEnd) {
        List<TicketingPurchaseAcDoc> invoice_history = dao.findInvoiceHistory(agentid, dateStart, dateEnd);

        InvoiceReport report = InvoiceReport.serializeToPurchaseSummery(agentid, invoice_history, dateStart, dateEnd);
        report.setTitle("3rdParty Invoice History");
        return report;
    }

    public InvoiceReport dueInvoiceReportSQL(Enums.AcDocType type, Long clientid,
            Date dateStart, Date dateEnd) {

        BigDecimal totalInvAmount = new BigDecimal("0.00");
        BigDecimal totalDMAmount = new BigDecimal("0.00");
        BigDecimal totalCMAmount = new BigDecimal("0.00");
        BigDecimal totalDue = new BigDecimal("0.00");
        BigDecimal totalPayment = new BigDecimal("0.00");
        BigDecimal totalRefund = new BigDecimal("0.00");

        List results = dao.findOutstandingInvoiceSQL(type, clientid, dateStart, dateEnd);
        List<TktingInvoiceSummery> invoices = new ArrayList<>();

        Iterator it = results.iterator();
        while (it.hasNext()) {
            TktingInvoiceSummery s = new TktingInvoiceSummery();

            Object[] objects = (Object[]) it.next();
            BigInteger bid = new BigInteger(objects[0].toString());
            s.setId(bid.longValue());
            Date date = (Date) objects[1];
            s.setDocIssueDate(DateUtil.dateToString(date));

            BigInteger ref = new BigInteger(objects[2].toString());
            s.setReference(ref.longValue());

            s.setGdsPnr((String) objects[3]);
            s.setNoOfPax((Integer) objects[4]);
            s.setOutBoundDetails((String) objects[5]);
            s.setLeadPsgr((String) objects[6]);
            s.setAirLine((String) objects[7]);

            BigDecimal invoiceAmount = (BigDecimal) objects[8];
            s.setDocumentedAmount(invoiceAmount);

            BigDecimal dueAmount = (BigDecimal) objects[9];
            s.setDue(dueAmount);

            String type_ = (String) objects[10];

            if (type_ != null) {
                char[] types = type_.replaceAll(",", "").toCharArray();

                String amount_ = (String) objects[11];
                String[] amounts = amount_.split(",");

                BigDecimal payment = new BigDecimal("0.00");
                BigDecimal refund = new BigDecimal("0.00");
                BigDecimal debitMemo = new BigDecimal("0.00");
                BigDecimal creditMemo = new BigDecimal("0.00");

                for (int i = 0; i < types.length; i++) {

                    int c = Character.getNumericValue(types[i]);

                    if (c == Enums.AcDocType.PAYMENT.getId()) {
                        payment = payment.add(new BigDecimal(amounts[i]));
                    } else if (c == Enums.AcDocType.REFUND.getId()) {
                        refund = refund.add(new BigDecimal(amounts[i]));
                    } else if (c == Enums.AcDocType.CREDITMEMO.getId()) {
                        creditMemo = creditMemo.add(new BigDecimal(amounts[i]));
                    } else if (c == Enums.AcDocType.DEBITMEMO.getId()) {
                        debitMemo = debitMemo.add(new BigDecimal(amounts[i]));
                    }
                }

                totalInvAmount = totalInvAmount.add(invoiceAmount);
                totalDMAmount = totalDMAmount.add(debitMemo);
                totalCMAmount = totalCMAmount.add(creditMemo);
                totalPayment = totalPayment.add(payment);
                totalRefund = totalRefund.add(refund);
                totalDue = totalDue.add(dueAmount);

                s.setPayment(payment.add(refund));
                s.setOtherAmount(creditMemo.add(debitMemo));
            }

            s.setInvBy((String) objects[12] + "/" + (String) objects[13]);
            s.setClientName((String) objects[14]);

            invoices.add(s);
        }

        InvoiceReport report = new InvoiceReport();
        if (dateStart != null && dateEnd != null) {
            report.setDateFrom(DateUtil.dateToString(dateStart));
            report.setDateTo(DateUtil.dateToString(dateEnd));
        }
        report.setInvoices(invoices);

        String currency = Application.currency();

        report.setTotalInvoice(String.valueOf(results.size()));
        report.setTotalInvAmount(currency + totalInvAmount.toString());
        report.setTotalCMAmount(currency + totalCMAmount.toString());
        report.setTotalDMAmount(currency + totalDMAmount.toString());
        report.setTotalDue(currency + totalDue.toString());
        report.setTotalPayment(currency + totalPayment.abs().toString());
        report.setTotalRefund(currency + totalRefund.abs().toString());

        report.setTitle("Outstanding Invoice Report");
        return report;
    }

    /**
     * @deprecated @param type
     * @param agentid
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<TicketingPurchaseAcDoc> dueThirdPartyInvoices(Enums.AcDocType type, Long agentid, Date dateStart, Date dateEnd) {

        //List<Long> dueref = dao.findOutstandingInvoiceReference(type, agentid, dateStart, dateEnd);
        //List<TicketingPurchaseAcDoc> dueInvoices = dao.findInvoiceByRef(dueref);
        List<TicketingPurchaseAcDoc> dueInvoices = dao.findOutstandingInvoice(type, agentid, dateStart, dateEnd);

        for (TicketingPurchaseAcDoc inv : dueInvoices) {

            Set<TicketingPurchaseAcDoc> relatedDocs = AcDocUtil.filterPVoidRelatedDocuments(inv.getRelatedDocuments());

            for (TicketingPurchaseAcDoc related : relatedDocs) {
                related.setAdditionalChargeLines(null);
                related.setPayment(null);
                related.setPnr(null);
                related.setTickets(null);
                related.setRelatedDocuments(null);
                related.setParent(null);
            }
            inv.setRelatedDocuments(relatedDocs);

            AcDocUtil.undefineTPAcDoc(inv, inv.getTickets());
            inv.setAdditionalChargeLines(null);
            inv.getPnr().setTickets(null);
            inv.getPnr().setRemarks(null);
            PnrUtil.undefinePnrInSegments(inv.getPnr(), inv.getPnr().getSegments());
            //inv.getPnr().setSegments(null);
        }
        return dueInvoices;
    }

    /**
     * Technically BSP invoices belongs to master agent.
     *
     * @param agentid
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<TicketingPurchaseAcDoc> dueBSPInvoices(Long agentid, Date dateStart, Date dateEnd) {

        //AcDoc type null will return boath
        List<TicketingPurchaseAcDoc> dueInvoices = dao.findOutstandingBSPInvoice(agentid, dateStart, dateEnd);

        int index = 0;
        for (TicketingPurchaseAcDoc inv : dueInvoices) {
            if (inv.getType().equals(Enums.AcDocType.CREDITMEMO)
                    || inv.getType().equals(Enums.AcDocType.DEBITMEMO)) {
                inv = inv.getParent();
                dueInvoices.set(index, inv);
            }

            inv.setAdditionalChargeLines(null);
            inv.setTickets(null);
            //inv.setRelatedDocuments(null);
            inv.getPnr().setTickets(null);
            inv.getPnr().setRemarks(null);
            inv.getPnr().setSegments(null);

            for (TicketingPurchaseAcDoc related : inv.getRelatedDocuments()) {
                related.setAdditionalChargeLines(null);
                related.setPayment(null);
                related.setPnr(null);
                related.setTickets(null);
                related.setRelatedDocuments(null);
                related.setParent(null);
            }
            index++;
        }

        return dueInvoices;
    }

    public BSPReport dueBSPReport(Long agentid, Date dateStart, Date dateEnd) {
        List<TicketingPurchaseAcDoc> dueInvoices = dueBSPInvoices(agentid, dateStart, dateEnd);
        List<TicketingPurchaseAcDoc> adm_acm = dao.findBSP_ADM_ACM(agentid, dateStart, dateEnd);
        GDSSaleReport sale_report = ticketService.saleReport(Enums.TicketingType.IATA, null, null, dateStart, dateEnd, null);

        for (TicketingPurchaseAcDoc related : adm_acm) {
            related.setAdditionalChargeLines(null);
            related.setPayment(null);
            related.setTickets(null);
            related.setRelatedDocuments(null);
            related.setParent(null);
            PnrUtil.undefineChildrenInPnr(related.getPnr());
        }

        BSPReport report = new BSPReport();
        report.setAdm_acm(adm_acm);
        report.setDueInvoices(dueInvoices);
        report.setSale_report(sale_report);
        return report;
    }

    public InvoiceReport dueInvoiceReportBSP(Enums.AcDocType type, Long agentid, Date dateStart, Date dateEnd) {

        List<TicketingPurchaseAcDoc> dueInvoices = dueThirdPartyInvoices(type, agentid, dateStart, dateEnd);

        InvoiceReport report = InvoiceReport.serializeToPurchaseSummery(agentid, dueInvoices, dateStart, dateEnd);
        report.setTitle("Vendor Due Invoice Report");
        return report;
    }

    public InvoiceReport dueInvoiceReport(Enums.AcDocType type, Long agentid, Date dateStart, Date dateEnd) {

        List<TicketingPurchaseAcDoc> dueInvoices = dueThirdPartyInvoices(type, agentid, dateStart, dateEnd);

        InvoiceReport report = InvoiceReport.serializeToPurchaseSummery(agentid, dueInvoices, dateStart, dateEnd);
        report.setTitle("Vendor Due Invoice Report");
        return report;
    }

    public ProductivityReport allAgentDueReport(Date dateStart, Date dateEnd) {

        Map<String, BigDecimal> productivityLine = dao.allAgentOutstandingReport(dateStart, dateEnd);

        ProductivityReport report = new ProductivityReport();
        report.setTitle("Outstanding Invoice Report");
        report.setSaleType(Enums.SaleType.TKTPURCHASE);
        report.setDateFrom(DateUtil.dateToString(dateStart));
        report.setDateTo(DateUtil.dateToString(dateEnd));

        for (String key : productivityLine.keySet()) {
            ProductivityReport.ProductivityLine line = new ProductivityReport.ProductivityLine();
            line.setKey(key);
            line.setValue(productivityLine.get(key).abs().toString());
            report.addLine(line);
        }
        return report;
    }

    public List<Agent> outstandingAgents(Enums.AcDocType acDocType) {

        List<Agent> agents = dao.outstandingAgentsSQL(acDocType);
        return agents;
    }

    public List<Agent> findTicketingAgents() {
        return dao.findTicketingAgents();
    }
}
