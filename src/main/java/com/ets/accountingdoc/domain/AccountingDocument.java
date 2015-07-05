package com.ets.accountingdoc.domain;

import com.ets.PersistentObject;
import com.ets.util.Enums.AcDocStatus;
import com.ets.util.Enums.AcDocType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.Version;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AccountingDocument extends PersistentObject implements Serializable {

    @XmlElement
    private Date docIssueDate;
    @XmlElement
    private AcDocType type;
    @XmlElement
    private String terms;
    @XmlElement
    private Long reference;
    @XmlElement
    private Integer version;
    @XmlElement
    private AcDocStatus status;
    @XmlElement
    private BigDecimal documentedAmount;
    @XmlElement
    private String remark;
    
    public abstract BigDecimal calculateTicketedSubTotal();    

    public abstract BigDecimal calculateAddChargesSubTotal();
    
    public abstract BigDecimal calculateDocumentedAmount();

    public abstract BigDecimal calculateTotalDebitMemo();
    
    public abstract BigDecimal calculateTotalCreditMemo();
        
    public abstract BigDecimal calculateTotalPayment();
    
    public abstract BigDecimal calculateTotalRefund();

    public abstract BigDecimal calculateDueAmount();  

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(nullable = false)
    public BigDecimal getDocumentedAmount() {
        return documentedAmount;
    }

    public void setDocumentedAmount(BigDecimal documentedAmount) {
        this.documentedAmount = documentedAmount;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDocIssueDate() {
        return docIssueDate;
    }

    public void setDocIssueDate(Date docIssueDate) {
        this.docIssueDate = docIssueDate;
    }

    @Column(nullable = false)
    public AcDocType getType() {
        return type;
    }

    public void setType(AcDocType type) {
        this.type = type;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    @Column(nullable = false)
    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    @Column(nullable = false)
    public AcDocStatus getStatus() {
        return status;
    }

    public void setStatus(AcDocStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
