package com.ets.accountingdoc.domain;

import com.ets.PersistentObject;
import com.ets.otherservice.domain.OtherService;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "acdoc_line")
public class AccountingDocumentLine extends PersistentObject implements Serializable {

    @XmlElement
    private String remark;
    @XmlElement
    private BigDecimal purchaseAmount = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal amount = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal discount = new BigDecimal("0.00");
    @XmlElement
    private int qty = 1;
    @XmlElement
    private OtherService otherService;

    @XmlElement
    private OtherSalesAcDoc otherSalesAcDoc;

    public BigDecimal calculateOServiceLineTotal() {
        return this.amount.add(this.discount).multiply(new BigDecimal(qty));
    }

    public BigDecimal calculateOServiceCostTotal() {
        return this.purchaseAmount.multiply(new BigDecimal(qty));
    }
    
    public BigDecimal calculateRevenue() {
        return calculateOServiceLineTotal().subtract(calculateOServiceCostTotal());
    }
    /**
     * Additional charge has no discount nor quantity
     * @return 
     */
    public BigDecimal calculateAChargeLineTotal() {
        return this.amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
    
    @OneToOne
    @JoinColumn(name = "other_service_fk")
    public OtherService getOtherService() {
        return otherService;
    }

    public void setOtherService(OtherService otherService) {
        this.otherService = otherService;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tsacdoc_fk")
//    public TicketingSalesAcDoc getTicketingSalesAcDoc() {
//        return ticketingSalesAcDoc;
//    }
//
//    public void setTicketingSalesAcDoc(TicketingSalesAcDoc ticketingSalesAcDoc) {
//        this.ticketingSalesAcDoc = ticketingSalesAcDoc;
//    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osacdoc_fk")
    public OtherSalesAcDoc getOtherSalesAcDoc() {
        return otherSalesAcDoc;
    }

    public void setOtherSalesAcDoc(OtherSalesAcDoc otherSalesAcDoc) {
        this.otherSalesAcDoc = otherSalesAcDoc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
