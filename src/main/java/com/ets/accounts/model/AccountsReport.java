package com.ets.accounts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class AccountsReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String reportTitle;
    @XmlElement
    private String dateFrom;
    @XmlElement
    private String dateTo;
    
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
    private List<AccountsLine> lines = new ArrayList<>();
    @XmlElement
    private String openingBalance = "0.00";
    @XmlElement
    private String closingBalance = "0.00";
    @XmlElement
    private String totalInvAmount = "0.00";
    @XmlElement
    private String totalDMAmount = "0.00";
    @XmlElement
    private String totalCMAmount = "0.00";
    @XmlElement
    private String totalPayment = "0.00";
    @XmlElement
    private String totalRefund = "0.00";

    public AccountsReport() {
        
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
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

    public List<AccountsLine> getLines() {
        return lines;
    }

    public void setLines(List<AccountsLine> lines) {
        this.lines = lines;
    }

    public void addLine(AccountsLine line) {

        this.getLines().add(line);
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

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
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

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement
    public static class AccountsLine {

        @XmlElement
        private Long id;
        @XmlElement
        private String date;
        @XmlElement
        private String docType;
        @XmlElement
        private String line_desc;
        @XmlElement
        private String debit_amount;
        @XmlElement
        private String credit_amount;
        @XmlElement
        private String line_balance;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public String getLine_desc() {
            return line_desc;
        }

        public void setLine_desc(String line_desc) {
            this.line_desc = line_desc;
        }

        public String getDebit_amount() {
            return debit_amount;
        }

        public void setDebit_amount(String debit_amount) {
            this.debit_amount = debit_amount;
        }

        public String getCredit_amount() {
            return credit_amount;
        }

        public void setCredit_amount(String credit_amount) {
            this.credit_amount = credit_amount;
        }

        public String getLine_balance() {
            return line_balance;
        }

        public void setLine_balance(String line_balance) {
            this.line_balance = line_balance;
        }
    }

}
