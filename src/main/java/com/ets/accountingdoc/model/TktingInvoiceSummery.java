package com.ets.accountingdoc.model;

import com.ets.util.Enums;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class TktingInvoiceSummery implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private Long id;
    @XmlElement
    private Long parentId;
    @XmlElement
    private Long pnr_id;
    @XmlElement
    private Long reference;
    @XmlElement
    private Enums.AcDocType type;
    @XmlElement
    private Enums.AcDocStatus status;
    @XmlElement
    private Enums.ClientType clientType;
    @XmlElement
    private String clientName;
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
    private String gdsPnr;
    @XmlElement
    private Integer noOfPax;
    @XmlElement
    private String outBoundDetails;
    @XmlElement
    private String leadPsgr;
    @XmlElement
    private String airLine;
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

    public Enums.AcDocStatus getStatus() {
        return status;
    }

    public void setStatus(Enums.AcDocStatus status) {
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

    public String getGdsPnr() {
        return gdsPnr;
    }

    public void setGdsPnr(String gdsPnr) {
        this.gdsPnr = gdsPnr;
    }

    public Integer getNoOfPax() {
        return noOfPax;
    }

    public void setNoOfPax(Integer noOfPax) {
        this.noOfPax = noOfPax;
    }

    public String getOutBoundDetails() {
        return outBoundDetails;
    }

    public void setOutBoundDetails(String outBoundDetails) {
        this.outBoundDetails = outBoundDetails;
    }

    public String getLeadPsgr() {
        return leadPsgr;
    }

    public void setLeadPsgr(String leadPsgr) {
        this.leadPsgr = leadPsgr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPnr_id() {
        return pnr_id;
    }

    public void setPnr_id(Long pnr_id) {
        this.pnr_id = pnr_id;
    }

    public String getAirLine() {
        return airLine;
    }

    public void setAirLine(String airLine) {
        this.airLine = airLine;
    }

    public Enums.ClientType getClientType() {
        return clientType;
    }

    public void setClientType(Enums.ClientType clientType) {
        this.clientType = clientType;
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
