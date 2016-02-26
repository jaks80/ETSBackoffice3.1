package com.ets.accounts.model;

import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accountingdoc.model.TktingInvoiceSummery;
import com.ets.accountingdoc_o.model.OtherInvoiceSummery;
import com.ets.accounts.domain.Payment;
import com.ets.client.domain.Contactable;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.pnr.logic.PnrBusinessLogic;
import com.ets.report.model.Letterhead;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.DateUtil;
import com.ets.pnr.logic.PnrUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class TransactionReceipt {

    @XmlElement
    private Letterhead letterhead = AppSettingsService.getLetterhead();
    @XmlElement
    private Long id;
    @XmlElement
    private String reportTitle;
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
    private String remark;
    @XmlElement
    private String paymentType;
    @XmlElement
    private String totalAmount;
    @XmlElement
    private int totalItems;
    @XmlElement
    private String paymentDate;
    @XmlElement
    private String reportDate;
    @XmlElement
    private String cashier;
    @XmlElement
    private List<TktingInvoiceSummery> lines = new ArrayList<>();
    @XmlElement
    private List<OtherInvoiceSummery> olines = new ArrayList<>();

    public TransactionReceipt() {
    }

    public TransactionReceipt(Payment payment) {
        this.id = payment.getId();
        this.remark = payment.getRemark();
        this.paymentType = payment.getPaymentType().toString();
        this.paymentDate = DateUtil.dateToString(payment.getCreatedOn());
        this.reportDate = DateUtil.dateToString(new java.util.Date());
        this.cashier = payment.getCreatedBy().calculateFullName();

        BigDecimal total = new BigDecimal("0.00");

        Set<TicketingSalesAcDoc> sdocs = payment.gettSalesAcDocuments();

        if (sdocs != null && !sdocs.isEmpty()) {
            reportTitle = "Sales Receipt";
            for (TicketingSalesAcDoc d : sdocs) {
                TktingInvoiceSummery sum = new TktingInvoiceSummery();
                sum.setId(d.getId());
                sum.setReference(d.getReference());
                sum.setGdsPnr(d.getPnr().getGdsPnr());
                sum.setAirLine(d.getPnr().getAirLineCode());
                sum.setNoOfPax(d.getPnr().getNoOfPax());
                sum.setDocumentedAmount(d.getDocumentedAmount().abs());
                sum.setStatus(d.getStatus());
                sum.setType(d.getType());

                if (d.getParent() != null) {
                    sum.setParentId(d.getParent().getId());
                    Set<Ticket> tickets = d.getParent().getTickets();
                    if (tickets != null && !tickets.isEmpty()) {
                        Ticket leadPax = PnrBusinessLogic.calculateLeadPaxTicket(tickets);
                        sum.setLeadPsgr(leadPax.getFullPaxName() + "/" + leadPax.getFullTicketNo());
                    }
                }

                sum.setOutBoundDetails(d.getPnr().getFlightSummery());

                lines.add(sum);
                total = total.add(d.getDocumentedAmount().abs());

                if (d.getPnr() != null && clientName == null) {
                    Pnr pnr = d.getPnr();
                    Contactable cont = null;

                    if (pnr.getAgent() != null) {
                        cont = pnr.getAgent();
                    } else {
                        cont = pnr.getCustomer();
                    }
                    clientName = cont.calculateFullName();
                    email = cont.getEmail();
                    fax = cont.getFax();
                    mobile = cont.getMobile();
                    telNo = cont.getMobile();
                    addressCRSeperated = cont.getAddressCRSeperated();
                }
            }
            this.totalItems = sdocs.size();
            this.totalAmount = total.abs().toString();
        }

        Set<TicketingPurchaseAcDoc> pdocs = payment.gettPurchaseAcDocuments();

        if (pdocs != null && !pdocs.isEmpty()) {
            reportTitle = "Billing Receipt";
            for (TicketingPurchaseAcDoc d : pdocs) {
                TktingInvoiceSummery sum = new TktingInvoiceSummery();
                sum.setId(d.getId());
                sum.setReference(d.getReference());
                sum.setGdsPnr(d.getPnr().getGdsPnr());
                sum.setAirLine(d.getPnr().getAirLineCode());
                sum.setNoOfPax(d.getPnr().getNoOfPax());
                sum.setDocumentedAmount(d.getDocumentedAmount().abs());
                sum.setStatus(d.getStatus());
                sum.setType(d.getType());
                lines.add(sum);
                total = total.add(d.getDocumentedAmount());

                if (d.getParent() != null) {
                    sum.setParentId(d.getParent().getId());
                    Set<Ticket> tickets = d.getParent().getTickets();
                    if (tickets != null && !tickets.isEmpty()) {
                        Ticket leadPax = PnrBusinessLogic.calculateLeadPaxTicket(tickets);
                        sum.setLeadPsgr(leadPax.getFullPaxName() + "/" + leadPax.getFullTicketNo());
                    }
                }

                if (d.getPnr() != null && clientName == null) {
                    Pnr pnr = d.getPnr();
                    if (pnr.getTicketing_agent() != null) {
                        clientName = pnr.getTicketing_agent().getName();
                        addressCRSeperated = pnr.getTicketing_agent().getFullAddressCRSeperated();

                        email = pnr.getTicketing_agent().getEmail();
                        fax = pnr.getTicketing_agent().getFax();
                        mobile = pnr.getTicketing_agent().getMobile();
                        telNo = pnr.getTicketing_agent().getMobile();

                    }
                }
                this.totalItems = pdocs.size();
                this.totalAmount = total.abs().toString();
            }
        }

        Set<OtherSalesAcDoc> odocs = payment.getoSalesAcDocuments();

        if (odocs != null && !odocs.isEmpty()) {
            reportTitle = "Sales Receipt";
            for (OtherSalesAcDoc d : odocs) {
                OtherInvoiceSummery sum = new OtherInvoiceSummery();
                sum.setId(d.getId());
                if (d.getParent() != null) {
                    sum.setParentId(d.getParent().getId());
                }
                sum.setReference(d.getReference());
                sum.setRemark(d.getRemark());
                sum.setDocumentedAmount(d.getDocumentedAmount().abs());
                sum.setStatus(d.getStatus().toString());
                sum.setType(d.getType());
                olines.add(sum);
                total = total.add(d.getDocumentedAmount());

                Contactable cont = null;

                if (d.getAgent() != null) {
                    cont = d.getAgent();
                } else if (d.getCustomer() != null) {
                    cont = d.getCustomer();
                }

                if (cont != null) {
                    clientName = cont.calculateFullName();
                    email = cont.getEmail();
                    fax = cont.getFax();
                    mobile = cont.getMobile();
                    telNo = cont.getMobile();
                    addressCRSeperated = cont.getAddressCRSeperated();
                }

                this.totalItems = odocs.size();
                this.totalAmount = total.abs().toString();
            }
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddressCRSeperated() {
        return addressCRSeperated;
    }

    public void setAddressCRSeperated(String addressCRSeperated) {
        this.addressCRSeperated = addressCRSeperated;
    }

    public Letterhead getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(Letterhead letterhead) {
        this.letterhead = letterhead;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public List<TktingInvoiceSummery> getLines() {
        return lines;
    }

    public void setLines(List<TktingInvoiceSummery> lines) {
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<OtherInvoiceSummery> getOlines() {
        return olines;
    }

    public void setOlines(List<OtherInvoiceSummery> olines) {
        this.olines = olines;
    }
}
