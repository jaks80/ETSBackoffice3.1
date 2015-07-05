package com.ets.pnr.model;

import com.ets.pnr.domain.Itinerary;
import com.ets.util.DateUtil;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SegmentModel {

    @XmlElement
    private long id;
    @XmlElement
    private int segmentNo;
    @XmlElement
    private String deptFrom;
    @XmlElement
    private String deptTo;
    @XmlElement
    private String deptDate;
    @XmlElement
    private String deptTime;
    @XmlElement
    private String arvDate;
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
    private String flightDuration;

    public static SegmentModel segmentToModel(Itinerary i) {

        SegmentModel model = new SegmentModel();
        model.setId(i.getId());
        model.setSegmentNo(i.getSegmentNo());
        
        model.setDeptDate(DateUtil.dateTOddmm(i.getDeptDate()));
        model.setDeptFrom(i.getDeptFrom());
        model.setDeptTime(i.getDeptTime());
        model.setDeptTo(i.getDeptTo());
        model.setArvDate(DateUtil.dateTOddmm(i.getArvDate()));
        model.setArvTime(i.getArvTime());
        
        model.setAirLineCode(i.getAirLineCode());        
        model.setBaggage(i.getBaggage());        
        model.setFlightDuration(i.getFlightDuration());
        model.setFlightNo(i.getFlightNo());
        model.setTicketClass(i.getTicketClass());
        model.setTktStatus(i.getTktStatus());
        
        return model;
    }

    public int getSegmentNo() {
        return segmentNo;
    }

    public void setSegmentNo(int segmentNo) {
        this.segmentNo = segmentNo;
    }

    public String getDeptFrom() {
        return deptFrom;
    }

    public void setDeptFrom(String deptFrom) {
        this.deptFrom = deptFrom;
    }

    public String getDeptTo() {
        return deptTo;
    }

    public void setDeptTo(String deptTo) {
        this.deptTo = deptTo;
    }

    public String getDeptDate() {
        return deptDate;
    }

    public void setDeptDate(String deptDate) {
        this.deptDate = deptDate;
    }

    public String getDeptTime() {
        return deptTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    public String getArvDate() {
        return arvDate;
    }

    public void setArvDate(String arvDate) {
        this.arvDate = arvDate;
    }

    public String getArvTime() {
        return arvTime;
    }

    public void setArvTime(String arvTime) {
        this.arvTime = arvTime;
    }

    public String getAirLineCode() {
        return airLineCode;
    }

    public void setAirLineCode(String airLineCode) {
        this.airLineCode = airLineCode;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public String getTktStatus() {
        return tktStatus;
    }

    public void setTktStatus(String tktStatus) {
        this.tktStatus = tktStatus;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
