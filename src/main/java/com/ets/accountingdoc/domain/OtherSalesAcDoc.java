package com.ets.accountingdoc.domain;

import com.ets.accounts.domain.Payment;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.util.Enums;
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "other_sales_acdoc")
public class OtherSalesAcDoc extends AccountingDocument implements Serializable {

    @XmlElement
    private Agent agent;
    @XmlElement
    private Customer customer;
    @XmlElement
    private Set<AccountingDocumentLine> accountingDocumentLines = new LinkedHashSet<>();
    @XmlElement
    private Set<AdditionalChargeLine> additionalChargeLines = new LinkedHashSet<>();
    @XmlElement
    private Set<OtherSalesAcDoc> relatedDocuments = new LinkedHashSet<>();
    @XmlElement
    private OtherSalesAcDoc parent;

    @XmlElement
    private Payment payment;

    @Override
    public BigDecimal calculateDocumentedAmount() {
        return calculateOtherServiceSubTotal().add(calculateAddChargesSubTotal());
    }

    public BigDecimal calculateOtherServiceSubTotal() {
        BigDecimal subtotal = new BigDecimal("0.00");
        for (AccountingDocumentLine l : accountingDocumentLines) {
            if (l.getOtherService() != null) {
                subtotal = subtotal.add(l.calculateOServiceLineTotal());
            }
        }
        return subtotal;
    }

    @Override
    public BigDecimal calculateAddChargesSubTotal() {
        BigDecimal subtotal = new BigDecimal("0.00");
        for (AdditionalChargeLine l : this.getAdditionalChargeLines()) {
            subtotal = subtotal.add(l.getAmount());
        }
        return subtotal;
    }

    @Override
    public BigDecimal calculateTotalPayment() {
        BigDecimal totalPayment = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (doc.getType().equals(Enums.AcDocType.PAYMENT)) {
                totalPayment = totalPayment.add(doc.getDocumentedAmount());//Can not use calculate
            }
        }
        return totalPayment;
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

    @Override
    public BigDecimal calculateDueAmount() {
        BigDecimal dueAmount = new BigDecimal("0.00");
        BigDecimal invoiceAmount = getDocumentedAmount();

        if (invoiceAmount == null) {
            invoiceAmount = calculateDocumentedAmount();
        }

        if (getType().equals(Enums.AcDocType.INVOICE)) {
            for (AccountingDocument doc : this.relatedDocuments) {

                if (doc.getDocumentedAmount() == null) {
                    doc.setDocumentedAmount(doc.calculateDocumentedAmount());
                }
                dueAmount = dueAmount.add(doc.getDocumentedAmount());
            }
        }
        return invoiceAmount.add(dueAmount);
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

    public BigDecimal calculateRelatedDocBalance() {
        BigDecimal relAmount = new BigDecimal("0.00");
        for (AccountingDocument doc : this.relatedDocuments) {
            if (!doc.getType().equals(Enums.AcDocType.PAYMENT)) {
                if (this.getId() != null) {
                    relAmount = relAmount.add(doc.getDocumentedAmount());
                } else {
                }

            }
        }
        return relAmount;
    }

    @OneToMany(mappedBy = "parent")
    @OrderBy(value = "id")
    public Set<OtherSalesAcDoc> getRelatedDocuments() {
        return relatedDocuments;
    }

    public void setRelatedDocuments(Set<OtherSalesAcDoc> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
    }

    @ManyToOne
    @JoinColumn(name = "parent_fk")
    public OtherSalesAcDoc getParent() {
        return parent;
    }

    public void setParent(OtherSalesAcDoc parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "otherSalesAcDoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<AccountingDocumentLine> getAccountingDocumentLines() {
        return accountingDocumentLines;
    }

    public void setAccountingDocumentLines(Set<AccountingDocumentLine> accountingDocumentLines) {
        this.accountingDocumentLines = accountingDocumentLines;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_fk")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "agentid_fk")
    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customerid_fk")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(mappedBy = "otherSalesAcDoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<AdditionalChargeLine> getAdditionalChargeLines() {
        return additionalChargeLines;
    }

    public void setAdditionalChargeLines(Set<AdditionalChargeLine> additionalChargeLines) {
        this.additionalChargeLines = additionalChargeLines;
    }
    
    @Override
    public BigDecimal calculateTicketedSubTotal() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
