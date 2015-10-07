package com.ets.accountingdoc.model;

import com.ets.Application;
import com.ets.accountingdoc.domain.*;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.client.domain.Contactable;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.pnr.logic.PnrBusinessLogic;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import com.ets.pnr.logic.PnrUtil;
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
public class InvoiceReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String title;
    @XmlElement
    private String totalInvoice;
    @XmlElement
    private String totalInvAmount = "0.00";
    @XmlElement
    private String totalDMAmount = "0.00";
    @XmlElement
    private String totalCMAmount = "0.00";
    @XmlElement
    private String totalDue = "0.00";
    @XmlElement
    private String totalPayment = "0.00";
    @XmlElement
    private String totalRefund = "0.00";
    @XmlElement
    private List<TktingInvoiceSummery> invoices = new ArrayList<>();

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

    public static InvoiceReport serializeToSalesSummery(Long clientid, List<TicketingSalesAcDoc> invoices, Date from, Date to) {
        BigDecimal totalInvAmount = new BigDecimal("0.00");
        BigDecimal totalDMAmount = new BigDecimal("0.00");
        BigDecimal totalCMAmount = new BigDecimal("0.00");
        BigDecimal totalDue = new BigDecimal("0.00");
        BigDecimal totalPayment = new BigDecimal("0.00");
        BigDecimal totalRefund = new BigDecimal("0.00");

        InvoiceReport report = new InvoiceReport();
        for (TicketingSalesAcDoc invoice : invoices) {

            if (invoice.getRelatedDocuments() != null) {
                Set<TicketingSalesAcDoc> relatedDocs = AcDocUtil.filterVoidRelatedDocuments(invoice.getRelatedDocuments());
                invoice.setRelatedDocuments(relatedDocs);
            }

            TktingInvoiceSummery invSummery = new TktingInvoiceSummery();

            Pnr pnr = invoice.getPnr();

            if (pnr.getAgent() != null) {
                invSummery.setClientType(Enums.ClientType.AGENT);
                invSummery.setClientName(pnr.getAgent().getName());
            } else {
                invSummery.setClientType(Enums.ClientType.CUSTOMER);
                invSummery.setClientName(pnr.getCustomer().calculateFullName());
            }

            invSummery.setId(invoice.getId());
            invSummery.setDocIssueDate(DateUtil.dateToString(invoice.getDocIssueDate()));
            invSummery.setGdsPnr(pnr.getGdsPnr());
            invSummery.setNoOfPax(pnr.getNoOfPax());

            Set<Ticket> tickets = invoice.getTickets();
            if (tickets != null && !tickets.isEmpty()) {
                Ticket leadPax = PnrBusinessLogic.calculateLeadPaxTicket(tickets);
                invSummery.setLeadPsgr(leadPax.getFullPaxName() + "/" + leadPax.getFullTicketNo());
            }

            invSummery.setAirLine(pnr.getAirLineCode());
            invSummery.setPnr_id(pnr.getId());
            invSummery.setReference(invoice.getReference());
            invSummery.setStatus(invoice.getStatus());
            invSummery.setType(invoice.getType());
            invSummery.setOutBoundDetails(invoice.getPnr().getFlightSummery());

            invSummery.setDocumentedAmount(invoice.getDocumentedAmount());
            invSummery.setOtherAmount(invoice.calculateTotalDebitMemo().add(invoice.calculateTotalCreditMemo()));
            invSummery.setPayment((invoice.calculateTotalPayment().add(invoice.calculateTotalRefund())).abs());
            invSummery.setDue(invoice.calculateDueAmount());

            invSummery.setInvBy(invoice.getCreatedBy().calculateFullName());

            totalInvAmount = totalInvAmount.add(invoice.getDocumentedAmount());
            totalDMAmount = totalDMAmount.add(invoice.calculateTotalDebitMemo());
            totalCMAmount = totalCMAmount.add(invoice.calculateTotalCreditMemo());
            totalPayment = totalPayment.add(invoice.calculateTotalPayment());
            totalRefund = totalRefund.add(invoice.calculateTotalRefund());
            totalDue = totalDue.add(invoice.calculateDueAmount());

            report.addInvoice(invSummery);
        }

        if (from != null && to != null) {
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

        if (clientid != null && !invoices.isEmpty() && invoices.get(0).getPnr() != null) {
            Pnr pnr = invoices.get(0).getPnr();
            Contactable cont = null;

            if (pnr.getAgent() != null) {
                cont = pnr.getAgent();
            } else {
                cont = pnr.getCustomer();
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

    public static InvoiceReport serializeToPurchaseSummery(Long clientid, List<TicketingPurchaseAcDoc> invoices, Date from, Date to) {
        BigDecimal totalInvAmount = new BigDecimal("0.00");
        BigDecimal totalDMAmount = new BigDecimal("0.00");
        BigDecimal totalCMAmount = new BigDecimal("0.00");
        BigDecimal totalDue = new BigDecimal("0.00");
        BigDecimal totalPayment = new BigDecimal("0.00");
        BigDecimal totalRefund = new BigDecimal("0.00");

        InvoiceReport report = new InvoiceReport();
        for (TicketingPurchaseAcDoc invoice : invoices) {

            if (invoice.getRelatedDocuments() != null) {
                Set<TicketingPurchaseAcDoc> relatedDocs = AcDocUtil.filterPVoidRelatedDocuments(invoice.getRelatedDocuments());
                invoice.setRelatedDocuments(relatedDocs);
            }

            TktingInvoiceSummery invSummery = new TktingInvoiceSummery();

            invSummery.setId(invoice.getId());
            invSummery.setDocIssueDate(DateUtil.dateToString(invoice.getDocIssueDate()));
            invSummery.setGdsPnr(invoice.getPnr().getGdsPnr());
            invSummery.setNoOfPax(invoice.getPnr().getNoOfPax());
            
            String leadPax = invoice.getPnr().getLeadPax();
            invSummery.setLeadPsgr(PnrUtil.calculatePartialName(leadPax));
            
            invSummery.setAirLine(invoice.getPnr().getAirLineCode());
            invSummery.setPnr_id(invoice.getPnr().getId());
            invSummery.setReference(invoice.getReference());
            invSummery.setStatus(invoice.getStatus());
            invSummery.setType(invoice.getType());
            invSummery.setOutBoundDetails(invoice.getPnr().getFlightSummery());
            invSummery.setDocumentedAmount(invoice.getDocumentedAmount());
            invSummery.setOtherAmount(invoice.calculateTotalDebitMemo().add(invoice.calculateTotalCreditMemo()));
            invSummery.setPayment(invoice.calculateTotalPayment().add(invoice.calculateTotalRefund()).abs());
            invSummery.setDue(invoice.calculateDueAmount());

            totalInvAmount = totalInvAmount.add(invoice.getDocumentedAmount());
            totalDMAmount = totalDMAmount.add(invoice.calculateTotalDebitMemo());
            totalCMAmount = totalCMAmount.add(invoice.calculateTotalCreditMemo());
            totalPayment = totalPayment.add(invoice.calculateTotalPayment());
            totalRefund = totalRefund.add(invoice.calculateTotalRefund());
            totalDue = totalDue.add(invoice.calculateDueAmount());

            report.addInvoice(invSummery);
        }

        if (from != null && to != null) {
        report.setDateFrom(DateUtil.dateToString(from));
        report.setDateTo(DateUtil.dateToString(to));
        }
        String currency = Application.currency();
        report.setTotalInvAmount(currency + totalInvAmount.toString());
        report.setTotalCMAmount(currency + totalCMAmount.toString());
        report.setTotalDMAmount(currency + totalDMAmount.toString());
        report.setTotalDue(currency + totalDue.toString());
        report.setTotalPayment(currency + totalPayment.toString());
        report.setTotalRefund(currency + totalRefund.toString());

        if (clientid != null && !invoices.isEmpty() && invoices.get(0).getPnr() != null) {
            Pnr pnr = invoices.get(0).getPnr();
            Contactable cont = null;

            if (pnr.getTicketing_agent() != null) {
                cont = pnr.getTicketing_agent();
                report.setClientName(cont.calculateFullName());
                report.setEmail(cont.getEmail());
                report.setFax(cont.getFax());
                report.setMobile(cont.getMobile());
                report.setTelNo(cont.getMobile());
                report.setAddressCRSeperated(cont.getAddressCRSeperated());
            }
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

    public List<TktingInvoiceSummery> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<TktingInvoiceSummery> invoices) {
        this.invoices = invoices;
    }

    public void addInvoice(TktingInvoiceSummery invoice) {
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
