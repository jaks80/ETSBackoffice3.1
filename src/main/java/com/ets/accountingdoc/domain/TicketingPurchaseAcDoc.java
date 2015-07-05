package com.ets.accountingdoc.domain;

import com.ets.accounts.domain.Payment;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums;
import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "tkt_purch_acdoc")
public class TicketingPurchaseAcDoc extends AccountingDocument implements Serializable {

    @XmlElement
    private String vendorRef;
    @XmlElement
    private Set<Ticket> tickets = new LinkedHashSet<>();
    @XmlElement
    private Pnr pnr;
    @XmlElement
    private Set<AdditionalChargeLine> additionalChargeLines = new LinkedHashSet<>();
    @XmlElement
    private Set<TicketingPurchaseAcDoc> relatedDocuments = new LinkedHashSet<>();
    @XmlElement
    private TicketingPurchaseAcDoc parent;

    @XmlElement
    private Payment payment;

    @Override
    public BigDecimal calculateDocumentedAmount() {
        return calculateTicketedSubTotal().add(calculateAddChargesSubTotal());
    }

    @Override
    public BigDecimal calculateTicketedSubTotal() {
        BigDecimal subtotal = new BigDecimal("0.00");
        for (Ticket t : getTickets()) {
            subtotal = subtotal.add(t.calculateNetPurchaseFare());
        }
        return subtotal;
    }

    @Override
    public BigDecimal calculateAddChargesSubTotal() {
        BigDecimal subtotal = new BigDecimal("0.00");
        for (AdditionalChargeLine l : getAdditionalChargeLines()) {
            subtotal = subtotal.add(l.getAmount());
        }
        return subtotal;
    }

    @Override
    public BigDecimal calculateTotalPayment() {
        BigDecimal total = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (doc.getType().equals(Enums.AcDocType.PAYMENT)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    @Override
    public BigDecimal calculateDueAmount() {
        BigDecimal invoiceAmount = this.getDocumentedAmount();

        if (getType().equals(Enums.AcDocType.INVOICE)) {
            for (AccountingDocument doc : this.relatedDocuments) {
                invoiceAmount = invoiceAmount.add(doc.getDocumentedAmount());
            }
        }
        return invoiceAmount;
    }

    @Override
    public BigDecimal calculateTotalDebitMemo() {
        BigDecimal total = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (doc.getType().equals(Enums.AcDocType.DEBITMEMO)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    @Override
    public BigDecimal calculateTotalCreditMemo() {
        BigDecimal total = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (doc.getType().equals(Enums.AcDocType.CREDITMEMO)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    @Override
    public BigDecimal calculateTotalRefund() {
        BigDecimal total = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (doc.getType().equals(Enums.AcDocType.REFUND)) {
                total = total.add(doc.getDocumentedAmount());
            }
        }
        return total;
    }

    public String getVendorRef() {
        return vendorRef;
    }

    public void setVendorRef(String vendorRef) {
        this.vendorRef = vendorRef;
    }

    @OneToMany(mappedBy = "ticketingPurchaseAcDoc",fetch = FetchType.LAZY)
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pnr_fk",nullable=false)
    public Pnr getPnr() {
        return pnr;
    }

    public void setPnr(Pnr pnr) {
        this.pnr = pnr;
    }

    @OneToMany(mappedBy = "parent")
    @OrderBy(value = "id")
    public Set<TicketingPurchaseAcDoc> getRelatedDocuments() {
        return relatedDocuments;
    }

    public void setRelatedDocuments(Set<TicketingPurchaseAcDoc> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
    }

    @ManyToOne
    @JoinColumn(name = "parent_fk")
    public TicketingPurchaseAcDoc getParent() {
        return parent;
    }

    public void setParent(TicketingPurchaseAcDoc parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "ticketingPurchaseAcDoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<AdditionalChargeLine> getAdditionalChargeLines() {
        return additionalChargeLines;
    }

    public void setAdditionalChargeLines(Set<AdditionalChargeLine> additionalChargeLines) {
        this.additionalChargeLines = additionalChargeLines;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_fk")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    public void addTicket(Ticket ticket){
     this.tickets.add(ticket);
    }
}
