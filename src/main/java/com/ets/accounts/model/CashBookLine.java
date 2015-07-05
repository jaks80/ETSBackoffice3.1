package com.ets.accounts.model;

import com.ets.util.Enums;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class CashBookLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private Long id;
    @XmlElement
    private String date;
    @XmlElement
    private Enums.PaymentType paymentType;
    @XmlElement
    private String paymentremark;

    @XmlElement
    private String referenceSet;
    @XmlElement
    private String gdsPnrSet;
    @XmlElement
    private Enums.SaleType saleType;
    @XmlElement
    private BigDecimal amount;

    @XmlElement
    private String clientName;
    @XmlElement
    private String cashier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPaymentremark() {
        return paymentremark;
    }

    public void setPaymentremark(String paymentremark) {
        this.paymentremark = paymentremark;
    }

    public Enums.PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Enums.PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Enums.SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(Enums.SaleType saleType) {
        this.saleType = saleType;
    }

    public String getGdsPnrSet() {
        return gdsPnrSet;
    }

    public void setGdsPnrSet(String gdsPnrSet) {
        this.gdsPnrSet = gdsPnrSet;
    }

    public String getReferenceSet() {
        return referenceSet;
    }

    public void setReferenceSet(String referenceSet) {
        this.referenceSet = referenceSet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

}
