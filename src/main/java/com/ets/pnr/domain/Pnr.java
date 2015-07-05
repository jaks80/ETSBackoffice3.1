package com.ets.pnr.domain;

import com.ets.PersistentObject;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.settings.service.AppSettingsService;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "pnr")
public class Pnr extends PersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private String gdsPnr;
    @XmlElement
    private Integer noOfPax;
    @XmlElement
    private String bookingAgtOid;
    @XmlElement
    private String ticketingAgtOid;
    @XmlElement
    private Date pnrCreationDate;
    @XmlElement
    private Date pnrCancellationDate;
    @XmlElement
    private Date airCreationDate;
    @XmlElement
    private String PnrCreatorAgentSine;
    @XmlElement
    private String ticketingAgentSine;
    @XmlElement
    private String vendorPNR;
    @XmlElement
    private String airLineCode;

    @XmlElement
    private Set<Ticket> tickets = new LinkedHashSet<>();
    @XmlElement
    private Set<Itinerary> segments = new LinkedHashSet<>();
    @XmlElement
    private Set<Remark> remarks = new LinkedHashSet<>();
    @XmlElement
    private Agent agent;
    @XmlElement
    private Customer customer;
    @XmlElement
    private Agent ticketing_agent;

    public Pnr() {
    }

    @Column(length = 8)
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

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getPnrCreationDate() {
        return pnrCreationDate;
    }

    public void setPnrCreationDate(Date pnrCreationDate) {
        this.pnrCreationDate = pnrCreationDate;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getAirCreationDate() {
        return airCreationDate;
    }

    public void setAirCreationDate(Date airCreationDate) {
        this.airCreationDate = airCreationDate;
    }

    public String getVendorPNR() {
        return vendorPNR;
    }

    public void setVendorPNR(String vendorPNR) {
        this.vendorPNR = vendorPNR;
    }

    @OneToMany(mappedBy = "pnr", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    @OneToMany(mappedBy = "pnr", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<Itinerary> getSegments() {
        return segments;
    }

    public void setSegments(Set<Itinerary> segments) {
        this.segments = segments;
    }

    @OneToMany(mappedBy = "pnr", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    public Set<Remark> getRemarks() {
        return remarks;
    }

    public void setRemarks(Set<Remark> remarks) {
        this.remarks = remarks;
    }

    @Column(length = 9)
    public String getBookingAgtOid() {
        return bookingAgtOid;
    }

    public void setBookingAgtOid(String bookingAgtOid) {
        this.bookingAgtOid = bookingAgtOid;
    }

    @Column(length = 9)
    public String getTicketingAgtOid() {
        return ticketingAgtOid;
    }

    public void setTicketingAgtOid(String ticketingAgtOid) {
        this.ticketingAgtOid = ticketingAgtOid;
    }

    public String getPnrCreatorAgentSine() {
        return PnrCreatorAgentSine;
    }

    public void setPnrCreatorAgentSine(String PnrCreatorAgentSine) {
        this.PnrCreatorAgentSine = PnrCreatorAgentSine;
    }

    public String getTicketingAgentSine() {
        return ticketingAgentSine;
    }

    public void setTicketingAgentSine(String ticketingAgentSine) {
        this.ticketingAgentSine = ticketingAgentSine;
    }

    @Column(length = 3)
    public String getAirLineCode() {
        return airLineCode;
    }

    public void setAirLineCode(String airLineCode) {
        this.airLineCode = airLineCode;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tkagentid_fk")
    public Agent getTicketing_agent() {
        return ticketing_agent;
    }

    public void setTicketing_agent(Agent ticketing_agent) {
        this.ticketing_agent = ticketing_agent;
    }

    public boolean selfTicketing() {
        return this.ticketingAgtOid.equals(AppSettingsService.mainAgent.getOfficeID());
    }

    public Date getPnrCancellationDate() {
        return pnrCancellationDate;
    }

    public void setPnrCancellationDate(Date pnrCancellationDate) {
        this.pnrCancellationDate = pnrCancellationDate;
    }
}
