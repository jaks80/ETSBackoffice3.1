package com.ets.pnr.domain;

import com.ets.PersistentObject;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "itinerary")
public class Itinerary extends PersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private int segmentNo;
    @XmlElement
    private String deptFrom;
    @XmlElement
    private String deptTo;
    @XmlElement
    private Date deptDate;
    @XmlElement
    private String deptTime;
    @XmlElement
    private Date arvDate;
    @XmlElement
    private String arvTime;
    @XmlElement
    private String airLineCode;
    @XmlElement
    private String flightNo;
    @XmlElement
    private String ticketClass;
    @XmlElement
    private String tktStatus;
    @XmlElement
    private String baggage;
    @XmlElement
    private String mealCode;
    @XmlElement
    private String checkInTerminal;
    @XmlElement
    private String checkInTime;
    @XmlElement
    private String flightDuration;
    @XmlElement
    private String mileage;
    @XmlElement
    private String stopOver;//(X is no stopover permitted, O is stopover permitted) 

    @XmlElement
    private Pnr pnr;

    public Itinerary() {

    }

    @Column(nullable = false)
    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    @Column(length = 10)
    public String getDeptFrom() {
        return deptFrom;
    }

    public void setDeptFrom(String deptFrom) {
        this.deptFrom = deptFrom;
    }

    @Column(length = 10)
    public String getDeptTo() {
        return deptTo;
    }

    public void setDeptTo(String deptTo) {
        this.deptTo = deptTo;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(nullable = false)
    public Date getDeptDate() {
        return deptDate;
    }

    public void setDeptDate(Date deptDate) {
        this.deptDate = deptDate;
    }

    @Column(length = 10)
    public String getDeptTime() {
        return deptTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getArvDate() {
        return arvDate;
    }

    public void setArvDate(Date arvDate) {
        this.arvDate = arvDate;
    }

    @Column(length = 10)
    public String getArvTime() {
        return arvTime;
    }

    public void setArvTime(String arvTime) {
        this.arvTime = arvTime;
    }

    @Column(length = 10)
    public String getAirLineCode() {
        return airLineCode;
    }

    public void setAirLineCode(String airLineCode) {
        this.airLineCode = airLineCode;
    }

    @Column(length = 10)
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    @Column(length = 5)
    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    @Column(length = 5)
    public String getTktStatus() {
        return tktStatus;
    }

    public void setTktStatus(String tktStatus) {
        this.tktStatus = tktStatus;
    }

    @Column(length = 10)
    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    @Column(length = 4)
    public String getMealCode() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    @Column(length = 10)
    public String getCheckInTerminal() {
        return checkInTerminal;
    }

    public void setCheckInTerminal(String checkInTerminal) {
        this.checkInTerminal = checkInTerminal;
    }

    @Column(length = 10)
    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    @Column(length = 10)
    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Column(length = 10)
    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    @Column(length = 1)
    public String getStopOver() {
        return stopOver;
    }

    public void setStopOver(String stopOver) {
        this.stopOver = stopOver;
    }

    @ManyToOne
    @JoinColumn(name = "pnr_fk")
    public Pnr getPnr() {
        return pnr;
    }

    public void setPnr(Pnr pnr) {
        this.pnr = pnr;
    }
}
