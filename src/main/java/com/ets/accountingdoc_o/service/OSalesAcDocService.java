package com.ets.accountingdoc_o.service;

import com.ets.accountingdoc.domain.*;
import com.ets.accountingdoc_o.dao.*;
import com.ets.accountingdoc_o.model.*;
import com.ets.productivity.model.ProductivityReport;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.exception.ClientNotFoundException;
import com.ets.settings.domain.User;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.math.BigDecimal;
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

    public synchronized OtherSalesAcDoc newDocument(OtherSalesAcDoc doc) throws ClientNotFoundException {

        if(doc.getAgent() == null && doc.getCustomer() == null){
            throw new ClientNotFoundException("##Agent/Customer needed##");
        }
        
        if (doc.getReference() == null && doc.getType().equals(Enums.AcDocType.INVOICE)) {
            //if (doc.getReference() == null) {
                doc.setReference(AcDocUtil.generateAcDocRef(dao.getNewAcDocRef()));
            //}
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

    public List<OtherSalesAcDoc> getByReffference(int refNo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete(Long id) {
        OtherSalesAcDoc doc = dao.getWithChildrenById(id);
        dao.delete(doc);
        return true;
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

    public ProductivityReport agentOutstandingReport(Date dateStart,Date dateEnd) {

        Map<String, BigDecimal> productivityLine = dao.allAgentOutstandingReport(dateStart,dateEnd);

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
}
