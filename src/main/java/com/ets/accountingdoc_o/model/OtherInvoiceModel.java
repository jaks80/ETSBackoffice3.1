package com.ets.accountingdoc_o.model;

import com.ets.accountingdoc.domain.AccountingDocumentLine;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc.model.RelatedDocSummery;
import com.ets.report.model.Letterhead;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.DateUtil;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * com.ets.fe.acdoc.model.report.InvoiceModel
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class OtherInvoiceModel implements Serializable {

    @XmlElement
    private Letterhead letterhead = AppSettingsService.getLetterhead();
    @XmlElement
    private String title;
    @XmlElement
    private String clientName;
    @XmlElement
    private String clientAddress;
    @XmlElement
    private String refference;
    @XmlElement
    private String date;
    @XmlElement
    private String terms;
    @XmlElement
    private String documentBy;
    @XmlElement
    private String subTotal;
    @XmlElement
    private String additionalChg;
    @XmlElement
    private String otherAdjustment;
    @XmlElement
    private String invoiceAmount;
    @XmlElement
    private String payment;
    @XmlElement
    private String balance;
    @XmlElement
    private String termsAndConditions;
    @XmlElement
    private Set<DocumentLineSummery> lineItems = new LinkedHashSet<>();
    @XmlElement
    private Set<RelatedDocSummery> relateddocs = new LinkedHashSet<>();

    public OtherInvoiceModel() {

    }

    public static OtherInvoiceModel createModel(OtherSalesAcDoc invoice) {
        OtherInvoiceModel model = new OtherInvoiceModel();
        model.setTitle("Invoice");

        if (invoice.getAgent() != null) {
            model.setClientName(invoice.getAgent().getName());
            model.setClientAddress(invoice.getAgent().getFullAddressCRSeperated());
        } else {
            model.setClientName(invoice.getCustomer().calculateFullName());
            model.setClientAddress(invoice.getCustomer().getFullAddressCRSeperated());
        }
        model.setRefference(invoice.getReference().toString());
        model.setDate(DateUtil.dateToString(invoice.getDocIssueDate()));
        model.setTerms(invoice.getTerms());
        model.setDocumentBy(invoice.getCreatedBy().calculateFullName());
        model.setSubTotal(invoice.calculateOtherServiceSubTotal().toString());
        
        model.setAdditionalChg(invoice.calculateAddChargesSubTotal().toString());
        model.setOtherAdjustment(invoice.calculateTotalCreditMemo().add(invoice.calculateTotalDebitMemo()).toString());
        model.setPayment(invoice.calculateTotalPayment().toString());
        model.setInvoiceAmount(invoice.getDocumentedAmount().toString());
        model.setBalance(invoice.calculateDueAmount().toString());

        Set<OtherSalesAcDoc> relatedDocs = invoice.getRelatedDocuments();

        for (OtherSalesAcDoc rd : relatedDocs) {
            RelatedDocSummery sum = new RelatedDocSummery();
            sum.setAmount(rd.getDocumentedAmount().toString());
            sum.setDate(DateUtil.dateToString(rd.getDocIssueDate()));
            sum.setRemark(rd.getRemark());
            sum.setType(rd.getType().toString());
        }

        Set<AccountingDocumentLine> lines = invoice.getAccountingDocumentLines();

        for (AccountingDocumentLine l : lines) {
            model.addLine(DocumentLineSummery.documentLineToModel(l));
        }
      
        Set<RelatedDocSummery> relateddocs = new LinkedHashSet<>();

        for (OtherSalesAcDoc d : relatedDocs) {
            relateddocs.add(RelatedDocSummery.documentToSummery(d));
        }
        model.setRelateddocs(relateddocs);
        return model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getRefference() {
        return refference;
    }

    public void setRefference(String refference) {
        this.refference = refference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getDocumentBy() {
        return documentBy;
    }

    public void setDocumentBy(String documentBy) {
        this.documentBy = documentBy;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getAdditionalChg() {
        return additionalChg;
    }

    public void setAdditionalChg(String additionalChg) {
        this.additionalChg = additionalChg;
    }

    public String getOtherAdjustment() {
        return otherAdjustment;
    }

    public void setOtherAdjustment(String otherAdjustment) {
        this.otherAdjustment = otherAdjustment;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Set<RelatedDocSummery> getRelateddocs() {
        return relateddocs;
    }

    public void setRelateddocs(Set<RelatedDocSummery> relateddocs) {
        this.relateddocs = relateddocs;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Letterhead getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(Letterhead letterhead) {
        this.letterhead = letterhead;
    }

    public Set<DocumentLineSummery> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<DocumentLineSummery> lineItems) {
        this.lineItems = lineItems;
    }

    public void addLine(DocumentLineSummery line) {
        this.lineItems.add(line);

    }
}
