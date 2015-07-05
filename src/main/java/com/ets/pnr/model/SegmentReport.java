package com.ets.pnr.model;

import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import com.ets.util.Enums.TicketStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SegmentReport implements Serializable{

    @XmlElement
    private List<Segment> list = new ArrayList<>();

    @XmlElement
    private int totalIssuedSegment = 0;
    @XmlElement
    private int totalReissuedSegment = 0;
    @XmlElement
    private int totalRefundedSegment = 0;
    @XmlElement
    private int totalVoidSegment = 0;
    @XmlElement
    private int totalOpenSegment = 0;
    @XmlElement
    private int segBalance = 0;

    public SegmentReport() {

    }

    public void addSegment(Itinerary itinerary, Pnr pnr, Ticket ticket) {
        Segment segment = new Segment(itinerary, pnr, ticket);
        getList().add(segment);
    }

    public void generateSummery() {
        for (Segment s : list) {

            if (s.getTktStatus() == "ISSUE") {
                totalIssuedSegment++;
            } else if (s.getTktStatus() == "REISSUE") {
                totalReissuedSegment++;
            } else if (s.getTktStatus() == "REFUND") {
                totalRefundedSegment++;
            } else if (s.getTktStatus() == "VOID" || s.getAirLine().equals("VOID")) {
                totalVoidSegment++;
            }

            if (s.getFlightNo() != null) {
                if (s.getFlightNo().equals("OPEN")) {
                    totalOpenSegment++;
                }
            }
        }
        segBalance = totalIssuedSegment - totalRefundedSegment - totalVoidSegment - totalOpenSegment;
    }

    public List<Segment> getList() {
        return list;
    }

    public int getTotalIssuedSegment() {
        return totalIssuedSegment;
    }

    public int getTotalReissuedSegment() {
        return totalReissuedSegment;
    }

    public int getTotalRefundedSegment() {
        return totalRefundedSegment;
    }

    public int getTotalVoidSegment() {
        return totalVoidSegment;
    }

    public int getTotalOpenSegment() {
        return totalOpenSegment;
    }

    public int getSegBalance() {
        return segBalance;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    private static class Segment {

        @XmlElement
        private String pnr;
        @XmlElement
        private String bookingOid;
        @XmlElement
        private String ticketingOid;
        @XmlElement
        private String ticketNo;
        @XmlElement
        private String ticketClass;
        @XmlElement
        private String flightNo;
        @XmlElement
        private String airLine;
        @XmlElement
        private String travelDate;
        @XmlElement
        private String inCity;
        @XmlElement
        private String outCity;
        @XmlElement
        private int count;
        @XmlElement
        private String tktStatus;

        private Segment(Itinerary itinerary, Pnr pnr, Ticket ticket) {
            this.pnr = pnr.getGdsPnr();
            this.bookingOid = pnr.getBookingAgtOid();
            this.ticketingOid = pnr.getTicketingAgtOid();
            this.ticketNo = ticket.getFullTicketNo();            
            this.tktStatus = ticket.getTktStatusString();
            this.ticketClass = itinerary.getTicketClass();
            this.flightNo = itinerary.getFlightNo();
            this.airLine = itinerary.getAirLineCode();
            this.travelDate = DateUtil.dateTOddmm(itinerary.getDeptDate());
            this.inCity = itinerary.getDeptFrom();
            this.outCity = itinerary.getDeptTo();

            if (ticket.getTktStatus() == TicketStatus.ISSUE) {
                count = 1;
            } else if (ticket.getTktStatus() == TicketStatus.REISSUE) {
                count = 0;
            } else if (ticket.getTktStatus() == TicketStatus.REFUND) {
                count = -1;
            } else if (ticket.getTktStatus() == TicketStatus.VOID) {
                count = 0;
            }
        }

        public String getPnr() {
            return pnr;
        }

        public void setPnr(String pnr) {
            this.pnr = pnr;
        }

        public String getBookingOid() {
            return bookingOid;
        }

        public void setBookingOid(String bookingOid) {
            this.bookingOid = bookingOid;
        }

        public String getTicketingOid() {
            return ticketingOid;
        }

        public void setTicketingOid(String ticketingOid) {
            this.ticketingOid = ticketingOid;
        }

        public String getTicketNo() {
            return ticketNo;
        }

        public void setTicketNo(String ticketNo) {
            this.ticketNo = ticketNo;
        }

        public String getTicketClass() {
            return ticketClass;
        }

        public void setTicketClass(String ticketClass) {
            this.ticketClass = ticketClass;
        }

        public String getAirLine() {
            return airLine;
        }

        public void setAirLine(String airLine) {
            this.airLine = airLine;
        }

        public String getTravelDate() {
            return travelDate;
        }

        public void setTravelDate(String travelDate) {
            this.travelDate = travelDate;
        }

        public String getInCity() {
            return inCity;
        }

        public void setInCity(String inCity) {
            this.inCity = inCity;
        }

        public String getOutCity() {
            return outCity;
        }

        public void setOutCity(String outCity) {
            this.outCity = outCity;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getFlightNo() {
            return flightNo;
        }

        public void setFlightNo(String flightNo) {
            this.flightNo = flightNo;
        }

        public String getTktStatus() {
            return tktStatus;
        }

        public void setTktStatus(String tktStatus) {
            this.tktStatus = tktStatus;
        }
    }
}
