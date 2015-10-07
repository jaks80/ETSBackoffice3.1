package com.ets.accountingdoc_o.service;

import com.ets.Application;
import com.ets.accountingdoc.domain.*;
import com.ets.accountingdoc_o.dao.*;
import com.ets.accountingdoc_o.model.*;
import com.ets.productivity.model.ProductivityReport;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.exception.ClientNotFoundException;
import com.ets.settings.domain.User;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("oSalesAcDocService")
public class OSalesAcDocService {

    @Resource(name = "otherSalesAcDocDAO")
    private OtherSalesAcDocDAO dao;

    @Resource(name = "accountingDocumentLineDAO")
    private AccountingDocumentLineDAO lineDao;

    public void fixDB() {
        
        List<OtherSalesAcDoc> docs = dao.findAllDocuments();

        for (OtherSalesAcDoc d : docs) {
            Set<AccountingDocumentLine> lines = d.getAccountingDocumentLines();
            if (lines.size() > 0) {
                System.out.println("Id:"+d.getId()+" Qty: "+lines.size()+" Cat: "+lines.iterator().next().getOtherService().getCategory().getTitle());
                dao.update(lines.size(), lines.iterator().next().getOtherService().getCategory().getTitle(), d.getId());
            }
        }
    }

    public synchronized OtherSalesAcDoc newDocument(OtherSalesAcDoc doc) throws ClientNotFoundException {

        if (doc.getAgent() == null && doc.getCustomer() == null) {
            throw new ClientNotFoundException("##Agent/Customer needed##");
        }

        if (doc.getReference() == null && doc.getType().equals(Enums.AcDocType.INVOICE)) {

            doc.setReference(AcDocUtil.generateAcDocRef(dao.getNewAcDocRef()));
            Set<AccountingDocumentLine> lines = doc.getAccountingDocumentLines();
            if (lines.isEmpty()) {
                return doc;
            } else {

            }
        }

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.initAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        AcDocUtil.initAcDocInLine(doc, doc.getAccountingDocumentLines());
        doc.setStatus(Enums.AcDocStatus.ACTIVE);

        if (doc.getParent() != null) {
            OtherSalesAcDoc parent = getWithChildrenById(doc.getParent().getId());
            doc.setParent(parent);
        }

        if (!doc.getAccountingDocumentLines().isEmpty() || !doc.getAdditionalChargeLines().isEmpty()) {
            doc.setDocumentedAmount(doc.calculateDocumentedAmount());
        }

        dao.save(doc);

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.undefineAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        if (doc.getAccountingDocumentLines() != null && !doc.getAccountingDocumentLines().isEmpty()) {
            AcDocUtil.undefineAcDocInLine(doc, doc.getAccountingDocumentLines());
        }
        return doc;
    }

    public OtherSalesAcDoc getWithChildrenById(long id) {
        OtherSalesAcDoc doc = dao.getWithChildrenById(id);
        //validateDocumentedAmount(doc);
        if (doc.getParent() != null) {
            doc.getParent().setAccountingDocumentLines(null);
            doc.getParent().setAdditionalChargeLines(null);
            doc.getParent().setPayment(null);
            doc.getParent().setRelatedDocuments(null);
        }

        return undefineChildren(doc);
    }

    public List<OtherSalesAcDoc> findAllById(Long... id) {
        List<OtherSalesAcDoc> invoices = dao.findAllById(id);
        return invoices;
    }

    public InvoiceReportOther findInvoiceSummeryByReference(Long... refNo) {
        List<OtherSalesAcDoc> invoices = dao.findInvoiceByRef(refNo);
        InvoiceReportOther report = InvoiceReportOther.serializeToSalesSummery(null, invoices, null, null);
        report.setTitle("Invoice Report");
        return report;
    }

    public boolean delete(Long id) {
        OtherSalesAcDoc doc = dao.getWithChildrenById(id);
        dao.delete(doc);
        return true;
    }

    public boolean deleteLine(Long id, Long userid) {
        boolean success = false;
        lineDao.deleteLine(id, userid);
        success = true;
        return success;
    }

    public OtherSalesAcDoc _void(OtherSalesAcDoc other_doc) {
        OtherSalesAcDoc doc = dao.getWithChildrenById(other_doc.getId());

        doc.setLastModified(other_doc.getLastModified());
        doc.setLastModifiedBy(other_doc.getLastModifiedBy());

        Set<OtherSalesAcDoc> relatedDocs = doc.getRelatedDocuments();
        Set<OtherSalesAcDoc> filtered_relatedDocs = AcDocUtil.filterVoidRelatedDocumentsOther(relatedDocs);
        if (doc.getType().equals(Enums.AcDocType.INVOICE) && !filtered_relatedDocs.isEmpty()) {
            doc = getWithChildrenById(doc.getId());
            return doc;
        } else {
            dao.voidDocument(undefineChildren(doc));
            doc = getWithChildrenById(doc.getId());
            return doc;
        }
    }

    public InvoiceReportOther invoiceHistoryReport(Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd) {
        List<OtherSalesAcDoc> invoice_history = dao.findInvoiceHistory(clienttype, clientid, dateStart, dateEnd);

        InvoiceReportOther report = InvoiceReportOther.serializeToSalesSummery(clientid, invoice_history, dateStart, dateEnd);
        report.setTitle("Invoice History Report");
        return report;
    }

    public List<OtherSalesAcDoc> dueInvoices(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd) {
        List<OtherSalesAcDoc> dueInvoices = dao.findOutstandingDocuments(type, clienttype, clientid, dateStart, dateEnd);

        for (OtherSalesAcDoc inv : dueInvoices) {

            Set<OtherSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocumentsOther(inv.getRelatedDocuments());

            for (AccountingDocumentLine l : inv.getAccountingDocumentLines()) {
                l.setOtherSalesAcDoc(null);
            }

            for (OtherSalesAcDoc related : relatedDocs) {
                related.setAccountingDocumentLines(null);
                related.setAdditionalChargeLines(null);
                //related.setPayment(null);
                related.setRelatedDocuments(null);
                related.setParent(null);
            }
            inv.setRelatedDocuments(relatedDocs);
            inv.setAdditionalChargeLines(null);
        }

        return dueInvoices;
    }

    public InvoiceReportOther dueInvoiceReport(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd) {

        List<OtherSalesAcDoc> dueInvoices = dueInvoices(type, clienttype, clientid, dateStart, dateEnd);
        InvoiceReportOther report = InvoiceReportOther.serializeToSalesSummery(clientid, dueInvoices, dateStart, dateEnd);
        report.setTitle("Outstanding Invoice Report");
        return report;
    }

    public InvoiceReportOther dueInvoiceReportSQL(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid,
            Date dateStart, Date dateEnd) {

        BigDecimal totalInvAmount = new BigDecimal("0.00");
        BigDecimal totalDMAmount = new BigDecimal("0.00");
        BigDecimal totalCMAmount = new BigDecimal("0.00");
        BigDecimal totalDue = new BigDecimal("0.00");
        BigDecimal totalPayment = new BigDecimal("0.00");
        BigDecimal totalRefund = new BigDecimal("0.00");

        List results = dao.findOutstandingDocumentsSQL(type, clienttype, clientid, dateStart, dateEnd);
        List<OtherInvoiceSummery> invoices = new ArrayList<>();

        Iterator it = results.iterator();
        while (it.hasNext()) {
            OtherInvoiceSummery s = new OtherInvoiceSummery();

            Object[] objects = (Object[]) it.next();
            BigInteger bid = new BigInteger(objects[0].toString());
            s.setId(bid.longValue());

            Date date = (Date) objects[1];
            s.setDocIssueDate(DateUtil.dateToString(date));

            s.setRemark((String) objects[2]);
            s.setStatus(Enums.AcDocStatus.valueOf(Integer.valueOf(objects[3].toString())));

            s.setNoOfItems((Integer) objects[4]);
            s.setCategory((String) objects[5]);

            BigInteger ref = new BigInteger(objects[6].toString());
            s.setReference(ref.longValue());

            BigDecimal invoiceAmount = (BigDecimal) objects[7];
            s.setDocumentedAmount(invoiceAmount);

            BigDecimal dueAmount = (BigDecimal) objects[8];
            s.setDue(dueAmount);

            String type_ = (String) objects[9];
            s.setType(Enums.AcDocType.INVOICE);

            if (type_ != null) {

                char[] types = type_.replaceAll(",", "").toCharArray();

                String amount_ = (String) objects[10];
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

            s.setInvBy((String) objects[11] + "/" + (String) objects[12]);

            if (Enums.ClientType.AGENT.equals(clienttype)) {
                s.setClientName((String) objects[13]);
            } else if (Enums.ClientType.CUSTOMER.equals(clienttype)) {
                s.setClientName((String) objects[13] + "/" + objects[14]);
            } else {
                String name = (String) objects[13];
                if (name != null && !name.isEmpty()) {
                    s.setClientName(name);
                } else {
                    s.setClientName((String) objects[14] + "/" + objects[15]);
                }
            }

            invoices.add(s);
        }

        InvoiceReportOther report = new InvoiceReportOther();
        if (dateStart != null && dateEnd != null) {
            report.setDateFrom(DateUtil.dateToString(dateStart));
            report.setDateTo(DateUtil.dateToString(dateEnd));
        }
        report.setInvoices(invoices);

        String currency = Application.currency();

        report.setTotalInvAmount(currency + totalInvAmount.toString());
        report.setTotalCMAmount(currency + totalCMAmount.toString());
        report.setTotalDMAmount(currency + totalDMAmount.toString());
        report.setTotalDue(currency + totalDue.toString());
        report.setTotalPayment(currency + totalPayment.abs().toString());
        report.setTotalRefund(currency + totalRefund.abs().toString());

        report.setTitle("Outstanding Invoice Report");
        return report;
    }

    private OtherSalesAcDoc undefineChildren(OtherSalesAcDoc doc) {

        Set<OtherSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocumentsOther(doc.getRelatedDocuments());

        for (OtherSalesAcDoc r : relatedDocs) {
            if (r.getType().equals(Enums.AcDocType.PAYMENT) || r.getType().equals(Enums.AcDocType.REFUND)) {
                AcDocUtil.undefineOAcDocumentInPayment(r);
            }
            if (r.getAdditionalChargeLines() != null) {
                AcDocUtil.undefineAddChgLine(r, r.getAdditionalChargeLines());
            }
        }
        doc.setRelatedDocuments(relatedDocs);

        if (doc.getAccountingDocumentLines() != null && !doc.getAccountingDocumentLines().isEmpty()) {
            AcDocUtil.undefineAcDocInLine(doc, doc.getAccountingDocumentLines());
        }

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.undefineAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        return doc;
    }

    public OtherInvoiceModel getModelbyId(long id) {
        OtherSalesAcDoc doc = getWithChildrenById(id);
        return OtherInvoiceModel.createModel(undefineChildren(doc));
    }

    public ServicesSaleReport servicesSaleReport(Date from, Date to, Long categoryId,
            Long itemId, Enums.ClientType clienttype, Long clientid) {

        List<AccountingDocumentLine> line_items = lineDao.findLineItems(from, to, categoryId,
                itemId, clienttype, clientid);
        ServicesSaleReport sale_report = new ServicesSaleReport();
        sale_report.setReportTitle("Sale Report: Services");
        sale_report = ServicesSaleReport.serializeToSalesSummery(line_items, from, to);

        return sale_report;
    }

    public ProductivityReport userProductivityReport(Date from, Date to) {

        Map<User, BigDecimal> productivityLine = dao.userProductivityReport(from, to);

        ProductivityReport report = new ProductivityReport();
        report.setTitle("Productivity Report");
        report.setDateFrom(DateUtil.dateToString(from));
        report.setDateTo(DateUtil.dateToString(to));
        report.setSaleType(Enums.SaleType.OTHERSALES);

        for (User key : productivityLine.keySet()) {
            //report.getProductivityLine().put(key.calculateFullName(), productivityLine.get(key).toString());
            ProductivityReport.ProductivityLine line = new ProductivityReport.ProductivityLine();
            line.setKey(key.calculateFullName());
            line.setValue(productivityLine.get(key).abs().toString());
            report.addLine(line);
        }

        return report;
    }

    public ProductivityReport agentOutstandingReport(Date dateStart, Date dateEnd) {

        Map<String, BigDecimal> productivityLine = dao.allAgentOutstandingReport(dateStart, dateEnd);

        ProductivityReport report = new ProductivityReport();
        report.setTitle("Agent Outstanding Report");
        report.setSaleType(Enums.SaleType.OTHERSALES);
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

    public List<Customer> outstandingCusotmers(Enums.AcDocType acDocType) {

        List<Customer> customers = dao.outstandingCustomersSQL(acDocType);
        return customers;
    }

}
