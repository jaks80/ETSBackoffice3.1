package com.ets.accountingdoc_o.model;

import com.ets.Application;
import com.ets.accountingdoc.domain.AccountingDocumentLine;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.client.domain.Contactable;
import com.ets.report.model.Letterhead;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class InvoiceReportOther implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private String title;
    @XmlElement
    private String totalInvoice;
    @XmlElement
    private Letterhead letterhead = AppSettingsService.getLetterhead();
    @XmlElement
    private String totalInvAmount = new String("0.00");
    @XmlElement
    private String totalDMAmount = new String("0.00");
    @XmlElement
    private String totalCMAmount = new String("0.00");
    @XmlElement
    private String totalDue = new String("0.00");
    @XmlElement
    private String totalPayment = new String("0.00");
    @XmlElement
    private String totalRefund = new String("0.00");
    @XmlElement
    private List<OtherInvoiceSummery> invoices = new ArrayList<>();

    @XmlElement
    private String clientName;
    @XmlElement
    private String addressCRSeperated;
    @XmlElement
    private String telNo;
    @XmlElement
    private String mobile;
    @XmlElement
    private String email;
    @XmlElement
    private String fax;
    
    @XmlElement
    private String dateFrom;
    @XmlElement
    private String dateTo;

    public static InvoiceReportOther serializeToSalesSummery(Long clientid,List<OtherSalesAcDoc> invoices,Date from, Date to) {        
                
        BigDecimal totalInvAmount = new BigDecimal("0.00");
        BigDecimal totalDMAmount = new BigDecimal("0.00");
        BigDecimal totalCMAmount = new BigDecimal("0.00");
        BigDecimal totalDue = new BigDecimal("0.00");
        BigDecimal totalPayment = new BigDecimal("0.00");
        BigDecimal totalRefund = new BigDecimal("0.00");

        InvoiceReportOther report = new InvoiceReportOther();
        for (OtherSalesAcDoc invoice : invoices) {
            
            if (invoice.getRelatedDocuments() != null) {
                Set<OtherSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocumentsOther(invoice.getRelatedDocuments());
                invoice.setRelatedDocuments(relatedDocs);
            }

            OtherInvoiceSummery invSummery = new OtherInvoiceSummery();

            if (invoice.getAgent() != null) {
                invSummery.setClientName(invoice.getAgent().getName());
            } else if(invoice.getCustomer()!= null){
                invSummery.setClientName(invoice.getCustomer().calculateFullName());
            }
            
            invSummery.setId(invoice.getId());
            invSummery.setDocIssueDate(DateUtil.dateToString(invoice.getDocIssueDate()));
            invSummery.setRemark(invoice.getRemark());
                        
            invSummery.setNoOfItems(invoice.getItemQuantity());
            invSummery.setCategory(invoice.getCategory());
            
            invSummery.setReference(invoice.getReference());
            invSummery.setStatus(invoice.getStatus().toString());
            invSummery.setType(invoice.getType());
            
            invSummery.setDocumentedAmount(invoice.getDocumentedAmount());
            invSummery.setOtherAmount(invoice.calculateTotalDebitMemo().add(invoice.calculateTotalCreditMemo()));
            invSummery.setPayment((invoice.calculateTotalPayment().add(invoice.calculateTotalRefund())).abs());
            invSummery.setDue(invoice.calculateDueAmount());

            invSummery.setAgent(invoice.getAgent());
            invSummery.setCustomer(invoice.getCustomer());
            invSummery.setInvBy(invoice.getCreatedBy().calculateFullName());
            
            if(!invoice.getStatus().equals(Enums.AcDocStatus.VOID)){
            totalInvAmount = totalInvAmount.add(invoice.getDocumentedAmount());
            totalDMAmount = totalDMAmount.add(invoice.calculateTotalDebitMemo());
            totalCMAmount = totalCMAmount.add(invoice.calculateTotalCreditMemo());
            totalPayment = totalPayment.add(invoice.calculateTotalPayment());
            totalRefund = totalRefund.add(invoice.calculateTotalRefund());
            totalDue = totalDue.add(invoice.calculateDueAmount());
            }

            report.addInvoice(invSummery);
        }
        
        if(from !=null && to!=null){
        report.setDateFrom(DateUtil.dateToString(from));
        report.setDateTo(DateUtil.dateToString(to));
        }
        
        String currency = Application.currency();
        report.setTotalInvAmount(currency + totalInvAmount.toString());
        report.setTotalCMAmount(currency + totalCMAmount.toString());
        report.setTotalDMAmount(currency + totalDMAmount.toString());
        report.setTotalDue(currency + totalDue.toString());
        report.setTotalPayment(currency + totalPayment.abs().toString());
        report.setTotalRefund(currency + totalRefund.abs().toString());

        if (clientid!=null && !invoices.isEmpty()){
           
            Contactable cont = null;

            if (invoices.get(0).getAgent() != null) {
                cont = invoices.get(0).getAgent();
            } else {
                cont = invoices.get(0).getCustomer();
            }
            report.setClientName(cont.calculateFullName());
            report.setEmail(cont.getEmail());
            report.setFax(cont.getFax());
            report.setMobile(cont.getMobile());
            report.setTelNo(cont.getMobile());
            report.setAddressCRSeperated(cont.getAddressCRSeperated());
        }
        report.setTotalInvoice(String.valueOf(report.getInvoices().size()));
        return report;
    }

    public String getTotalInvAmount() {
        return totalInvAmount;
    }

    public void setTotalInvAmount(String totalInvAmount) {
        this.totalInvAmount = totalInvAmount;
    }

    public String getTotalDMAmount() {
        return totalDMAmount;
    }

    public void setTotalDMAmount(String totalDMAmount) {
        this.totalDMAmount = totalDMAmount;
    }

    public String getTotalCMAmount() {
        return totalCMAmount;
    }

    public void setTotalCMAmount(String totalCMAmount) {
        this.totalCMAmount = totalCMAmount;
    }

    public String getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String totalDue) {
        this.totalDue = totalDue;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(String totalRefund) {
        this.totalRefund = totalRefund;
    }

    public List<OtherInvoiceSummery> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<OtherInvoiceSummery> invoices) {
        this.invoices = invoices;
    }

    public void addInvoice(OtherInvoiceSummery invoice) {
        this.invoices.add(invoice);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddressCRSeperated() {
        return addressCRSeperated;
    }

    public void setAddressCRSeperated(String addressCRSeperated) {
        this.addressCRSeperated = addressCRSeperated;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Letterhead getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(Letterhead letterhead) {
        this.letterhead = letterhead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(String totalInvoice) {
        this.totalInvoice = totalInvoice;
    }
}
