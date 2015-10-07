package com.ets.accountingdoc_o.model;

import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
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
public class OtherInvoiceSummery implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private Long id;
    @XmlElement
    private Long parentId;
    @XmlElement
    private Long reference;
    @XmlElement
    private Enums.AcDocType type;
    @XmlElement
    private String status;
    @XmlElement
    private BigDecimal documentedAmount;
    @XmlElement
    private BigDecimal payment;
    @XmlElement
    private BigDecimal otherAmount;
    @XmlElement
    private BigDecimal due;
    @XmlElement
    private String docIssueDate;
    @XmlElement
    private Integer noOfItems;
    @XmlElement
    private String category;
    @XmlElement
    private String remark;
    @XmlElement
    private Agent agent;
    @XmlElement
    private Customer customer;
    @XmlElement
    private String clientName;
    @XmlElement
    private String invBy; 

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public Enums.AcDocType getType() {
        return type;
    }

    public void setType(Enums.AcDocType type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDocumentedAmount() {
        return documentedAmount;
    }

    public void setDocumentedAmount(BigDecimal documentedAmount) {
        this.documentedAmount = documentedAmount;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }

    public String getDocIssueDate() {
        return docIssueDate;
    }

    public void setDocIssueDate(String docIssueDate) {
        this.docIssueDate = docIssueDate;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(Integer noOfItems) {
        this.noOfItems = noOfItems;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getInvBy() {
        return invBy;
    }

    public void setInvBy(String invBy) {
        this.invBy = invBy;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
