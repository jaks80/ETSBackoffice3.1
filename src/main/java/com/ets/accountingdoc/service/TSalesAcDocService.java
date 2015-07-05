package com.ets.accountingdoc.service;

import com.ets.accountingdoc.dao.TSalesAcDocDAO;
import com.ets.accountingdoc.domain.*;
import com.ets.accountingdoc.logic.TicketingAcDocBL;
import com.ets.accountingdoc.model.InvoiceModel;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.pnr.service.PnrService;
import com.ets.accountingdoc.model.InvoiceReport;
import com.ets.productivity.model.ProductivityReport;
import com.ets.settings.domain.User;
import com.ets.util.*;
import com.ets.util.Enums.AcDocType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
@Service("tSalesAcDocService")
public class TSalesAcDocService {

    @Resource(name = "tSalesAcDocDAO")
    private TSalesAcDocDAO dao;
    @Autowired
    private PnrService pnrService;
    @Autowired
    TPurchaseAcDocService purchase_service;

    /**
     * It is really important accounting documents are loaded in order by id.
     *
     * @param pnrid
     * @return
     */
    public synchronized TicketingSalesAcDoc newDraftDocumentUpdate(Long pnrid) {

        TicketingSalesAcDoc draftDocument = new TicketingSalesAcDoc();

        Pnr pnr = pnrService.getByIdWithChildren(pnrid);
        List<TicketingSalesAcDoc> acdocList = dao.getByPnrId(pnrid);
        List<TicketingSalesAcDoc> invoices = new ArrayList<>();

        for (TicketingSalesAcDoc doc : acdocList) {
            if (doc.getType().equals(AcDocType.INVOICE)) {
                invoices.add(doc);
            }
        }

        //We need to imrpove this logic here. Booking invoice
        // Booking date will be invoice date, then after issue how iisue date will be in invoice.
        //        
        Set<Ticket> uninvoicedIssuedTicket = PnrUtil.getUnInvoicedIssuedTicket(pnr, Enums.SaleType.TKTSALES);
        Set<Ticket> uninvoicedReIssuedTicket = PnrUtil.getUnInvoicedReIssuedTicket(pnr, Enums.SaleType.TKTSALES);
        Set<Ticket> uninvoicedRefundTicket = PnrUtil.getUnRefundedTickets(pnr, Enums.SaleType.TKTSALES);
        Set<Ticket> uninvoicedBookedTicket = PnrUtil.getUnInvoicedBookedTicket(pnr.getTickets(), Enums.SaleType.TKTSALES);

        TicketingAcDocBL logic = new TicketingAcDocBL(pnr);

        if (!uninvoicedIssuedTicket.isEmpty()) {
            if (invoices.isEmpty()) {
                draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedIssuedTicket);
            } else {
                List<TicketingSalesAcDoc> void_invoices = AcDocUtil.getVoidSalesInvoices(invoices);
                if (void_invoices.isEmpty()) {
                    draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedIssuedTicket);
                } else {
                    TicketingSalesAcDoc void_invoice = void_invoices.iterator().next();
                    void_invoice.setRelatedDocuments(null);
                    draftDocument = logic.newTicketingDraftInvoice(void_invoice, uninvoicedIssuedTicket);
                }
            }
        } else if (!uninvoicedReIssuedTicket.isEmpty()) {
            if (invoices.isEmpty()) {
                draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedReIssuedTicket);
            } else {
                List<TicketingSalesAcDoc> void_invoices = AcDocUtil.getVoidSalesInvoices(invoices);
                if (void_invoices.isEmpty()) {
                    draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedReIssuedTicket);
                } else {
                    TicketingSalesAcDoc void_invoice = void_invoices.iterator().next();
                    void_invoice.setRelatedDocuments(null);
                    draftDocument = logic.newTicketingDraftInvoice(void_invoice, uninvoicedReIssuedTicket);
                }
            }
        } else if (!uninvoicedRefundTicket.isEmpty()) {

            if (invoices.size() == 1) {
                TicketingSalesAcDoc invoice = invoices.get(0);
                //We need only Invoice here not children
                invoice.setTickets(null);
                invoice.setRelatedDocuments(null);
                invoice.setAdditionalChargeLines(null);
                draftDocument = logic.newTicketingDraftCMemo(invoice, uninvoicedRefundTicket);
            } else {
                //For multiple invoices rfd tickets needs to be allocated properly. Which invoice credits which tickets
                // So improve logic in this area.   
                TicketingSalesAcDoc invoice = invoices.get(0);
                //We need only Invoice here not children
                invoice.setTickets(null);
                invoice.setRelatedDocuments(null);
                invoice.setAdditionalChargeLines(null);
                draftDocument = logic.newTicketingDraftCMemo(invoice, uninvoicedRefundTicket);
            }
        } else if (!uninvoicedBookedTicket.isEmpty()) {
            if (invoices.isEmpty()) {
                draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedBookedTicket);
            } else {
                List<TicketingSalesAcDoc> void_invoices = AcDocUtil.getVoidSalesInvoices(invoices);

                if (void_invoices.isEmpty()) {
                    draftDocument = logic.newTicketingDraftInvoice(new TicketingSalesAcDoc(), uninvoicedBookedTicket);
                } else {
                    TicketingSalesAcDoc void_invoice = void_invoices.iterator().next();
                    void_invoice.setRelatedDocuments(null);
                    draftDocument = logic.newTicketingDraftInvoice(void_invoice, uninvoicedBookedTicket);
                }
            }
        }
        return draftDocument;
    }

    /**
     * Synchronize this to avoid acdoc ref duplication This method uses to
     * create every single accounting document with/without tickets
     *
     * @param doc
     * @return
     */
    public synchronized TicketingSalesAcDoc newDocument(TicketingSalesAcDoc doc) {

        if (doc.getReference() == null && doc.getType().equals(Enums.AcDocType.INVOICE)) {
            if (doc.getReference() == null) {
                //There will be refference from void invoice.
                doc.setReference(AcDocUtil.generateAcDocRef(dao.getNewAcDocRef()));
            }
        }

        AcDocUtil.initTSAcDocInTickets(doc, doc.getTickets());
        PnrUtil.initPnrInTickets(doc.getPnr(), doc.getTickets());

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.initAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        if (!doc.getTickets().isEmpty() || !doc.getAdditionalChargeLines().isEmpty()) {
            doc.setDocumentedAmount(doc.calculateDocumentedAmount());
        }

        TicketingPurchaseAcDoc p_doc = null;
        if (!doc.getTickets().isEmpty()) {
            Set<Ticket> bookedTickets = PnrUtil.getUnInvoicedBookedTicket(doc.getTickets(), Enums.SaleType.TKTPURCHASE);
            if (bookedTickets.isEmpty()) {
                p_doc = autoCreatePurchaseDocumentUpdate(doc);
            }
        }

        doc.setStatus(Enums.AcDocStatus.ACTIVE);
        dao.save(doc);

        AcDocUtil.undefineTSAcDoc(doc, doc.getTickets());

        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.undefineAddChgLine(doc, doc.getAdditionalChargeLines());
        }
        return doc;
    }

    /*
    Check if invoice already was in database. In case of VOID.
    */
    private TicketingPurchaseAcDoc autoCreatePurchaseDocumentUpdate(TicketingSalesAcDoc doc) {
        TicketingPurchaseAcDoc p_doc = purchase_service.findInvoiceByReference(doc.getReference());
        if (p_doc == null || !p_doc.getStatus().equals(Enums.AcDocStatus.VOID)) {
            p_doc = new TicketingPurchaseAcDoc();
        }

        TicketingAcDocBL logic = new TicketingAcDocBL(doc.getPnr());

        p_doc = logic.newTicketingPurchaseInvoice(doc, p_doc);
        purchase_service.createNewDocument(p_doc);

        return p_doc;
    }

    /**
     * Every time a sales document is generated, a corresponding purchase
     * document should generate automatically. Logic is document will be
     * generated automatically mirroring sales document but should be editable
     * by user except ticket fields
     *
     * @param doc
     */
//    private TicketingPurchaseAcDoc autoCreatePurchaseDocuments(TicketingSalesAcDoc doc, Long pnrid) {
//
//        TicketingPurchaseAcDoc p_doc = new TicketingPurchaseAcDoc();
//        TicketingAcDocBL logic = new TicketingAcDocBL(doc.getPnr());
//
//        if (doc.getType().equals(Enums.AcDocType.INVOICE)) {
//            List<TicketingPurchaseAcDoc> acdocList = purchase_service.getByPnrId(pnrid);
//            for (TicketingPurchaseAcDoc p : acdocList) {
//                if (p.getType().equals(Enums.AcDocType.INVOICE)) {
//                    p_doc = p;
//                    break;
//                }
//            }
//
//            p_doc = logic.newTicketingPurchaseInvoice(doc, p_doc);
//            purchase_service.createNewDocument(p_doc);
//
//        } else if (doc.getType().equals(Enums.AcDocType.DEBITMEMO)) {
//            TicketingPurchaseAcDoc invoice = purchase_service.findInvoiceByPnrId(doc.getPnr().getId());
//            p_doc = logic.newTicketingPurchaseDMemo(doc, invoice);
//            purchase_service.createNewDocument(p_doc);
//
//        } else if (doc.getType().equals(Enums.AcDocType.CREDITMEMO)) {
//            TicketingPurchaseAcDoc invoice = purchase_service.findInvoiceByPnrId(doc.getPnr().getId());
//            p_doc = logic.newTicketingPurchaseCMemo(doc, invoice);
//            purchase_service.createNewDocument(p_doc);
//        }
//        return p_doc;
//    }
    public TicketingSalesAcDoc getWithChildrenById(long id) {
        TicketingSalesAcDoc doc = dao.getWithChildrenById(id);
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

    public List<TicketingSalesAcDoc> findAllById(Long... id) {
        List<TicketingSalesAcDoc> invoices = dao.findAllById(id);
        return invoices;
    }

    private TicketingSalesAcDoc undefineChildren(TicketingSalesAcDoc doc) {

        for (Ticket t : doc.getTickets()) {
            t.setTicketingSalesAcDoc(null);
            t.setTicketingPurchaseAcDoc(null);
        }

        Set<TicketingSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocuments(doc.getRelatedDocuments());
        for (TicketingSalesAcDoc r : relatedDocs) {
            if (r.getType().equals(Enums.AcDocType.PAYMENT) || r.getType().equals(Enums.AcDocType.REFUND)) {
                AcDocUtil.undefineTSAcDocumentInPayment(r);
            }
            AcDocUtil.undefineAddChgLine(r, r.getAdditionalChargeLines());
        }
        doc.setRelatedDocuments(relatedDocs);
        if (doc.getAdditionalChargeLines() != null && !doc.getAdditionalChargeLines().isEmpty()) {
            AcDocUtil.undefineAddChgLine(doc, doc.getAdditionalChargeLines());
        }

        PnrUtil.undefinePnrInSegments(doc.getPnr(), doc.getPnr().getSegments());
        doc.getPnr().setTickets(null);
        doc.getPnr().setRemarks(null);

        return doc;
    }

    public List<TicketingSalesAcDoc> getByReffference(int refNo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<TicketingSalesAcDoc> getByGDSPnr(String pnr) {
        List<TicketingSalesAcDoc> list = dao.getByGDSPnr(pnr);

        return list;
    }

    public List<TicketingSalesAcDoc> getByPnrId(Long pnrId) {
        List<TicketingSalesAcDoc> list = dao.getByPnrId(pnrId);
        for (TicketingSalesAcDoc a : list) {
            a.setRelatedDocuments(null);
            if (a.getType().equals(Enums.AcDocType.PAYMENT) || a.getType().equals(Enums.AcDocType.REFUND)) {
                if (a.getParent() != null) {
                    AcDocUtil.undefineTSAcDocumentInPayment(a);
                }
            }
            for (Ticket t : a.getTickets()) {
                t.setTicketingSalesAcDoc(null);
            }
            AcDocUtil.undefineAddChgLine(a, a.getAdditionalChargeLines());
        }
        return list;
    }

    public TicketingSalesAcDoc findInvoiceByPaxName(String surName, Enums.TicketStatus ticketStatus, Long pnrId) {
        return dao.findInvoiceByPaxName(surName, ticketStatus, pnrId);
    }

    /*
     Do not delete corresponding purchase document, it might have bsppayment
     */
    public int delete(long id) {
        TicketingSalesAcDoc document = dao.getWithChildrenById(id);
        Set<TicketingSalesAcDoc> relatedDocs = document.getRelatedDocuments();

        //MySql gives exception if deleting with related docs.
        if (!relatedDocs.isEmpty()) {
            return 0;
        }

        if (document.getStatus().equals(Enums.AcDocStatus.VOID)
                && !document.getType().equals(Enums.AcDocType.PAYMENT)
                && !document.getType().equals(Enums.AcDocType.REFUND)) {
            dao.delete(document);
            return 1;
        }

        return 0;
    }

    /**
     * If invoice has related documents it is not possible to void invoice while
     * it has active children. Children needs to be VOID first.
     *
     * @param ticketingSalesAcDoc
     * @return
     */
    public TicketingSalesAcDoc _void(TicketingSalesAcDoc ticketingSalesAcDoc) {
        if (!ticketingSalesAcDoc.getTickets().isEmpty()) {
            ticketingSalesAcDoc = dao.voidSimpleDocument(ticketingSalesAcDoc);
            return ticketingSalesAcDoc;
        }
        TicketingSalesAcDoc doc = dao.getWithChildrenById(ticketingSalesAcDoc.getId());
        doc.setLastModified(ticketingSalesAcDoc.getLastModified());
        doc.setLastModifiedBy(ticketingSalesAcDoc.getLastModifiedBy());

        Set<TicketingSalesAcDoc> relatedDocs = doc.getRelatedDocuments();
        Set<TicketingSalesAcDoc> filtered_relatedDocs = AcDocUtil.filterVoidRelatedDocuments(relatedDocs);

        if (doc.getType().equals(Enums.AcDocType.INVOICE) && !filtered_relatedDocs.isEmpty()) {
            doc = getWithChildrenById(doc.getId());
            return doc;
        } else {

            dao.voidTicketedDocument(undefineChildren(doc));
            doc = getWithChildrenById(doc.getId());
            return doc;
        }
    }

    public TicketingSalesAcDoc voidInvoiceByAIRReader(TicketingSalesAcDoc invoice) {
        TicketingSalesAcDoc doc = dao.getWithChildrenById(invoice.getId());
        return dao.voidTicketedDocument(doc);
    }

    public InvoiceReport invoiceHistoryReport(Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd) {
        List<TicketingSalesAcDoc> invoice_history = dao.findInvoiceHistory(clienttype, clientid, dateStart, dateEnd);

        InvoiceReport report = InvoiceReport.serializeToSalesSummery(clientid, invoice_history, dateStart, dateEnd);
        report.setTitle("Invoice History Report");
        return report;
    }

    public List<TicketingSalesAcDoc> dueInvoices(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd) {
        List<TicketingSalesAcDoc> dueInvoices = dao.findOutstandingDocuments(type, clienttype, clientid, dateStart, dateEnd);

        for (TicketingSalesAcDoc inv : dueInvoices) {

            Set<TicketingSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocuments(inv.getRelatedDocuments());

            for (TicketingSalesAcDoc related : relatedDocs) {
                related.setAdditionalChargeLines(null);
                related.setPayment(null);
                related.setPnr(null);
                related.setTickets(null);
                related.setRelatedDocuments(null);
                related.setParent(null);
            }
            inv.setRelatedDocuments(relatedDocs);
            inv.setAdditionalChargeLines(null);
            AcDocUtil.undefineTSAcDoc(inv, inv.getTickets());
            //inv.setRelatedDocuments(null);
            inv.getPnr().setTickets(null);
            inv.getPnr().setRemarks(null);
            PnrUtil.undefinePnrInSegments(inv.getPnr(), inv.getPnr().getSegments());
            //inv.getPnr().setSegments(null);
        }

        return dueInvoices;
    }

    public InvoiceReport dueInvoiceReport(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid,
            Date dateStart, Date dateEnd) {

        List<TicketingSalesAcDoc> dueInvoices = dueInvoices(type, clienttype, clientid, dateStart, dateEnd);
        InvoiceReport report = InvoiceReport.serializeToSalesSummery(clientid, dueInvoices, dateStart, dateEnd);
        report.setTitle("Outstanding Invoice Report");
        return report;
    }

    public InvoiceReport outstandingFlightReport(Enums.ClientType clienttype,
            Long clientid, Date dateFrom, Date dateEnd) {

        Date dateStart = new java.util.Date();

        List<TicketingSalesAcDoc> dueInvoices = dao.outstandingFlightReport(clienttype, clientid, dateFrom, dateEnd);
        InvoiceReport report = InvoiceReport.serializeToSalesSummery(clientid, dueInvoices, dateStart, dateEnd);
        report.setTitle("Unpaid Flight Report");
        return report;
    }

    //This method making payments 0. Needs to be fixed.
    private void validateDocumentedAmount(TicketingSalesAcDoc doc) {
        if (doc.calculateDocumentedAmount().compareTo(doc.getDocumentedAmount()) != 0) {
            doc.setDocumentedAmount(doc.calculateDocumentedAmount());
            dao.save(doc);
        }
    }

    public InvoiceModel getModelbyId(long id) {
        TicketingSalesAcDoc doc = getWithChildrenById(id);
        return InvoiceModel.createModel(undefineChildren(doc));
    }

    public ProductivityReport userProductivityReport(Date from, Date to) {

        Map<User, BigDecimal> productivityLine = dao.userProductivityReport(from, to);

        ProductivityReport report = new ProductivityReport();
        report.setTitle("Productivity Report");
        report.setDateFrom(DateUtil.dateToString(from));
        report.setDateTo(DateUtil.dateToString(to));
        report.setSaleType(Enums.SaleType.TKTSALES);

        for (User key : productivityLine.keySet()) {
            //report.getProductivityLine().put(key.calculateFullName(), productivityLine.get(key).toString());
            ProductivityReport.ProductivityLine line = new ProductivityReport.ProductivityLine();
            line.setKey(key.calculateFullName());
            line.setValue(productivityLine.get(key).abs().toString());
            report.addLine(line);
        }

        return report;
    }

    public ProductivityReport allAgentDueReport(Date dateStart, Date dateEnd) {

        Map<String, BigDecimal> productivityLine = dao.allAgentOutstandingReport(dateStart, dateEnd);

        ProductivityReport report = new ProductivityReport();
        report.setTitle("Outstanding Invoice Report");
        report.setSaleType(Enums.SaleType.TKTSALES);
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
}
