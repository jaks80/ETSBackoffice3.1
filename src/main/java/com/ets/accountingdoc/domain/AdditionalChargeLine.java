package com.ets.accountingdoc.domain;

import com.ets.PersistentObject;
import com.ets.otherservice.domain.AdditionalCharge;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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
@Table(name = "additional_charge_line")
public class AdditionalChargeLine extends PersistentObject implements Serializable {

    @XmlElement
    private BigDecimal amount = new BigDecimal("0.00");    
    @XmlElement
    private AdditionalCharge additionalCharge;

    @XmlElement
    private TicketingSalesAcDoc ticketingSalesAcDoc;
    @XmlElement
    private TicketingPurchaseAcDoc ticketingPurchaseAcDoc;
    @XmlElement
    private OtherSalesAcDoc otherSalesAcDoc;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @OneToOne
    @JoinColumn(name = "add_charge_fk")
    public AdditionalCharge getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(AdditionalCharge additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tsacdoc_fk")
    public TicketingSalesAcDoc getTicketingSalesAcDoc() {
        return ticketingSalesAcDoc;
    }

    public void setTicketingSalesAcDoc(TicketingSalesAcDoc ticketingSalesAcDoc) {
        this.ticketingSalesAcDoc = ticketingSalesAcDoc;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tpacdoc_fk")
    public TicketingPurchaseAcDoc getTicketingPurchaseAcDoc() {
        return ticketingPurchaseAcDoc;
    }

    public void setTicketingPurchaseAcDoc(TicketingPurchaseAcDoc ticketingPurchaseAcDoc) {
        this.ticketingPurchaseAcDoc = ticketingPurchaseAcDoc;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "osacdoc_fk")
    public OtherSalesAcDoc getOtherSalesAcDoc() {
        return otherSalesAcDoc;
    }

    public void setOtherSalesAcDoc(OtherSalesAcDoc otherSalesAcDoc) {
        this.otherSalesAcDoc = otherSalesAcDoc;
    } 
}
