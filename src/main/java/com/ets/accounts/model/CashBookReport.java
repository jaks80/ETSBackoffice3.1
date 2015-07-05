package com.ets.accounts.model;

import com.ets.accounts.domain.Payment;
import com.ets.report.model.Letterhead;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class CashBookReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String title = "Cash Book";
    @XmlElement
    private Letterhead letterhead = AppSettingsService.getLetterhead();

    @XmlElement
    private int totalItems;
    @XmlElement
    private String dateFrom;
    @XmlElement
    private String dateTo;
    @XmlElement
    private BigDecimal totalCash = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalCheque = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalCCard = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalDCard = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalBankTransfer = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalOther = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal totalCreditTransfer = new BigDecimal("0.00");
    @XmlElement

    private List<CashBookLine> cashbook_items = new ArrayList<>();

    public static CashBookReport serializeToReport(List<Payment> payments, Date from, Date to) {

        CashBookReport report = new CashBookReport();
        report.setTotalItems(payments.size());
        report.setDateFrom(DateUtil.dateToString(from));
        report.setDateTo(DateUtil.dateToString(to));

        BigDecimal totalCash = new BigDecimal("0.00");
        BigDecimal totalCheque = new BigDecimal("0.00");
        BigDecimal totalCCard = new BigDecimal("0.00");
        BigDecimal totalDCard = new BigDecimal("0.00");
        BigDecimal totalBankTransfer = new BigDecimal("0.00");
        BigDecimal totalOther = new BigDecimal("0.00");
        BigDecimal totalCreditTransfer = new BigDecimal("0.00");

        List<CashBookLine> cashbook_items = new ArrayList<>();
        
        for (Payment p : payments) {
            CashBookLine line = new CashBookLine();

            line.setId(p.getId());
            line.setDate(DateUtil.dateToString(p.getCreatedOn()));
            line.setPaymentType(p.getPaymentType());
            line.setPaymentremark(p.getRemark());
            line.setClientName(p.calculateClientName());
            line.setCashier(p.getCreatedBy().calculateFullName());

            BigDecimal amount = new BigDecimal("0.00");
            if (p.gettSalesAcDocuments() != null) {
                line.setReferenceSet(p.calculateTSalesDocumentRefferences());
                line.setGdsPnrSet(p.calculateTSalesDocumentPnrs());
                line.setSaleType(Enums.SaleType.TKTSALES);
                amount = p.calculateTotalSalesPayment();

            } else if (p.gettPurchaseAcDocuments() != null) {
                line.setReferenceSet(p.calculateTPurchaseDocumentRefferences());
                line.setGdsPnrSet(p.calculateTPurchaseDocumentPnrs());
                line.setSaleType(Enums.SaleType.TKTPURCHASE);
                amount = p.calculateTotalPurchasePayment();

            } else if (p.getoSalesAcDocuments() != null) {
                line.setReferenceSet(p.calculateOSalesDocumentRefferences());
                line.setSaleType(Enums.SaleType.OTHERSALES);
                amount = p.calculateTotalOtherPayment();
            }
            
            line.setAmount(amount.multiply(new BigDecimal("-1")));//Show payment as positive and refund negetive
            
            if (p.getPaymentType().equals(Enums.PaymentType.CASH)) {
                totalCash = totalCash.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.CHEQUE)) {
                totalCheque = totalCheque.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.CREDIT_CARD)) {
                totalCCard = totalCCard.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.DEBIT_CARD)) {
                totalDCard = totalDCard.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.BANKT_TANSFER)) {
                totalBankTransfer = totalBankTransfer.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.OTHER)) {
                totalOther = totalOther.add(amount);
            } else if (p.getPaymentType().equals(Enums.PaymentType.CREDIT_TRANSFER)) {
                totalCreditTransfer = totalCreditTransfer.add(amount);
            }
            
            cashbook_items.add(line);
        }
        report.setCashbook_items(cashbook_items);
        report.setTotalCash(totalCash.abs());
        report.setTotalCheque(totalCheque.abs());
        report.setTotalBankTransfer(totalBankTransfer.abs());
        report.setTotalCCard(totalCCard.abs());
        report.setTotalDCard(totalDCard.abs());
        report.setTotalOther(totalOther.abs());
        report.setTotalCreditTransfer(totalCreditTransfer.abs());
        
        return report;
    }

    public Letterhead getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(Letterhead letterhead) {
        this.letterhead = letterhead;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
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

    public List<CashBookLine> getCashbook_items() {
        return cashbook_items;
    }

    public void setCashbook_items(List<CashBookLine> cashbook_items) {
        this.cashbook_items = cashbook_items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

    public BigDecimal getTotalCheque() {
        return totalCheque;
    }

    public void setTotalCheque(BigDecimal totalCheque) {
        this.totalCheque = totalCheque;
    }

    public BigDecimal getTotalCCard() {
        return totalCCard;
    }

    public void setTotalCCard(BigDecimal totalCCard) {
        this.totalCCard = totalCCard;
    }

    public BigDecimal getTotalDCard() {
        return totalDCard;
    }

    public void setTotalDCard(BigDecimal totalDCard) {
        this.totalDCard = totalDCard;
    }

    public BigDecimal getTotalBankTransfer() {
        return totalBankTransfer;
    }

    public void setTotalBankTransfer(BigDecimal totalBankTransfer) {
        this.totalBankTransfer = totalBankTransfer;
    }

    public BigDecimal getTotalOther() {
        return totalOther;
    }

    public void setTotalOther(BigDecimal totalOther) {
        this.totalOther = totalOther;
    }

    public BigDecimal getTotalCreditTransfer() {
        return totalCreditTransfer;
    }

    public void setTotalCreditTransfer(BigDecimal totalCreditTransfer) {
        this.totalCreditTransfer = totalCreditTransfer;
    }
}
