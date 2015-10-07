package com.ets.accounts.domain;

import com.ets.PersistentObject;
import com.ets.accountingdoc.domain.*;
import com.ets.pnr.domain.Pnr;
import com.ets.util.Enums;
import com.ets.util.Enums.PaymentType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "payment")
public class Payment extends PersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XmlElement
    private String remark;
    @XmlElement
    private Set<TicketingSalesAcDoc> tSalesAcDocuments = new LinkedHashSet<>();
    @XmlElement
    private Set<OtherSalesAcDoc> oSalesAcDocuments = new LinkedHashSet<>();
    @XmlElement
    private Set<TicketingPurchaseAcDoc> tPurchaseAcDocuments = new LinkedHashSet<>();
    @XmlElement
    private PaymentType paymentType;

    public Payment() {

    }

    public String calculateClientName() {
        String clientName = "";

        if (this.tSalesAcDocuments != null && !this.tSalesAcDocuments.isEmpty()) {
            Pnr pnr = tSalesAcDocuments.iterator().next().getPnr();
            if (pnr.getAgent() != null) {
                clientName = pnr.getAgent().getName();
            } else {
                clientName = pnr.getCustomer().calculateFullName();
            }
        } else if (this.tPurchaseAcDocuments != null && !this.tPurchaseAcDocuments.isEmpty()) {
            Pnr pnr = tPurchaseAcDocuments.iterator().next().getPnr();
            if (pnr.getAgent() != null) {
                clientName = pnr.getAgent().getName();
            } else {
                clientName = pnr.getCustomer().calculateFullName();
            }
        } else if (this.oSalesAcDocuments != null && !this.oSalesAcDocuments.isEmpty()) {
            OtherSalesAcDoc doc = oSalesAcDocuments.iterator().next();
            if (doc.getAgent() != null) {
                clientName = doc.getAgent().getName();
            } else if(doc.getCustomer() !=null){
                clientName = doc.getCustomer().calculateFullName();
            }
        }
        return clientName;
    }

    public BigDecimal calculateTotalSalesPayment() {
        BigDecimal total = new BigDecimal("0.00");

        for (TicketingSalesAcDoc doc : tSalesAcDocuments) {
            if (!doc.getStatus().equals(Enums.AcDocStatus.VOID)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    public BigDecimal calculateTotalPurchasePayment() {
        BigDecimal total = new BigDecimal("0.00");

        for (TicketingPurchaseAcDoc doc : tPurchaseAcDocuments) {
            if (!doc.getStatus().equals(Enums.AcDocStatus.VOID)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    public BigDecimal calculateTotalOtherPayment() {
        BigDecimal total = new BigDecimal("0.00");

        for (OtherSalesAcDoc doc : oSalesAcDocuments) {
            if (!doc.getStatus().equals(Enums.AcDocStatus.VOID)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    public String calculateTSalesDocumentRefferences() {
        StringBuilder sb = new StringBuilder();
        for (TicketingSalesAcDoc doc : this.tSalesAcDocuments) {
            sb.append(doc.getReference());
            sb.append(",");
        }
        return sb.toString();
    }

    public String calculateTPurchaseDocumentRefferences() {
        StringBuilder sb = new StringBuilder();
        for (TicketingPurchaseAcDoc doc : this.tPurchaseAcDocuments) {
            sb.append(doc.getReference());
            sb.append(",");
        }
        return sb.toString();
    }

    public String calculateTSalesDocumentPnrs() {
        StringBuilder sb = new StringBuilder();
        for (TicketingSalesAcDoc doc : this.tSalesAcDocuments) {
            sb.append(doc.getPnr().getGdsPnr());
            sb.append(",");
        }
        return sb.toString();
    }

    public String calculateTPurchaseDocumentPnrs() {
        StringBuilder sb = new StringBuilder();
        for (TicketingPurchaseAcDoc doc : this.tPurchaseAcDocuments) {
            sb.append(doc.getPnr().getGdsPnr());
            sb.append(",");
        }
        return sb.toString();
    }

    public String calculateOSalesDocumentRefferences() {
        StringBuilder sb = new StringBuilder();
        for (OtherSalesAcDoc doc : this.oSalesAcDocuments) {
            sb.append(doc.getReference());
            sb.append(",");
        }
        return sb.toString();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<TicketingSalesAcDoc> gettSalesAcDocuments() {
        return tSalesAcDocuments;
    }

    public void settSalesAcDocuments(Set<TicketingSalesAcDoc> tSalesAcDocuments) {
        this.tSalesAcDocuments = tSalesAcDocuments;
    }

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<OtherSalesAcDoc> getoSalesAcDocuments() {
        return oSalesAcDocuments;
    }

    public void setoSalesAcDocuments(Set<OtherSalesAcDoc> oSalesAcDocuments) {
        this.oSalesAcDocuments = oSalesAcDocuments;
    }

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<TicketingPurchaseAcDoc> gettPurchaseAcDocuments() {
        return tPurchaseAcDocuments;
    }

    public void settPurchaseAcDocuments(Set<TicketingPurchaseAcDoc> tPurchaseAcDocuments) {
        this.tPurchaseAcDocuments = tPurchaseAcDocuments;
    }

    public void addTSalesDocument(TicketingSalesAcDoc ticketingSalesAcDoc) {
        this.tSalesAcDocuments.add(ticketingSalesAcDoc);
    }
    
    public void addTPurchaseDocument(TicketingPurchaseAcDoc ticketingPurchaseAcDoc) {
        this.tPurchaseAcDocuments.add(ticketingPurchaseAcDoc);
    }

    public void addOtherDocument(OtherSalesAcDoc otherSalesAcDoc) {
        this.oSalesAcDocuments.add(otherSalesAcDoc);
    }
}
