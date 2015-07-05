package com.ets.mockdata;

import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import com.ets.util.Enums.TicketStatus;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Yusuf
 */
public class MockPnrData {

    private final Pnr mockPnr;
    private final Set<Ticket> mockBookedTickets;
    private final Set<Ticket> mockTTPIssuedTickets;
    private final Set<Ticket> mockTTPReIssuedTickets;
    private final Set<Ticket> mockRefundTickets;
    private final Set<Itinerary> mockSegments;

    public MockPnrData() {
        this.mockPnr = createMockPnr();
        this.mockBookedTickets = createMockBookedTickets();
        this.mockSegments = createMockSegments();
        this.mockTTPIssuedTickets = createMockTTPIssuedTickets();
        this.mockTTPReIssuedTickets = createMockTTPReIssuedTickets();
        this.mockRefundTickets = createMockRefundTickets();
    }

    public Pnr getMockPnr() {
        return mockPnr;
    }

    public Set<Ticket> getMockBookedTickets() {
        return mockBookedTickets;
    }

    public Set<Itinerary> getMockSegments() {
        return mockSegments;
    }

    public Set<Ticket> getMockTTPIssuedTickets() {
        return mockTTPIssuedTickets;
    }

    public Set<Ticket> getMockTTPReIssuedTickets() {
        return mockTTPReIssuedTickets;
    }

    public Set<Ticket> getMockRefundTickets() {
        return mockRefundTickets;
    }

    private Pnr createMockPnr() {

        Pnr pnr = new Pnr();
        pnr.setPnrCreationDate(DateUtil.yyMMddToDate("090313"));
        pnr.setAirCreationDate(DateUtil.yyMMddToDate("090316"));
        pnr.setBookingAgtOid("LONU123IJ");
        pnr.setTicketingAgtOid("LONU123IT");
        pnr.setGdsPnr("5HH342");
        pnr.setNoOfPax(3);
        pnr.setVendorPNR("AI HJ3HV");

        return pnr;
    }

    private Set<Ticket> createMockBookedTickets() {

        Set<Ticket> tickets = new LinkedHashSet<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("90.00"));
        t1.setTax(new BigDecimal("296.10"));
        t1.setNetPurchaseFare(new BigDecimal("386.10"));
        t1.setCurrencyCode("GBP");
        t1.setFee(new BigDecimal("0.00"));
        
        t1.setGrossFare(new BigDecimal("396.10"));
        t1.setDiscount(new BigDecimal("5.00").negate());
        t1.setAtolChg(new BigDecimal("1.00"));
        t1.setNetSellingFare(new BigDecimal("392.10"));
        
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR(CHD)(ID05MAR03)");
        t1.setSurName("MIAH");
        t1.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t1.setTktStatus(TicketStatus.BOOK);

        Ticket t2 = new Ticket();
        t2.setBaseFare(new BigDecimal("90.00"));
        t2.setTax(new BigDecimal("296.10"));
        t2.setNetPurchaseFare(new BigDecimal("386.10"));
        t2.setGrossFare(new BigDecimal("396.10"));
        t2.setDiscount(new BigDecimal("5.00").negate());
        t2.setAtolChg(new BigDecimal("1.00"));
        t2.setNetSellingFare(new BigDecimal("392.10"));
        t2.setCurrencyCode("GBP");
        t2.setFee(new BigDecimal("0.00"));
        t2.setPassengerNo(2);
        t2.setForeName("KABIL MSTR");
        t2.setSurName("MIAH");
        t2.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t2.setTktStatus(TicketStatus.BOOK);

        Ticket t3 = new Ticket();
        t3.setBaseFare(new BigDecimal("90.00"));
        t3.setTax(new BigDecimal("296.10"));
        t3.setNetPurchaseFare(new BigDecimal("386.10"));
        t3.setCurrencyCode("GBP");
        t3.setFee(new BigDecimal("0.00"));
        t3.setPassengerNo(3);
        t3.setForeName("JOSEPH MSTR");
        t3.setSurName("AKANDA");
        t3.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t3.setTktStatus(TicketStatus.BOOK);

        tickets.add(t1);
        tickets.add(t2);
        tickets.add(t3);

        return tickets;
    }

    private Set<Ticket> createMockTTPIssuedTickets() {

        Set<Ticket> tickets = new LinkedHashSet<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("90.00"));
        t1.setTax(new BigDecimal("296.10"));
        t1.setCommission(new BigDecimal("5.00"));
        t1.setNetPurchaseFare(new BigDecimal("386.10"));        
        t1.setFee(new BigDecimal("0.00"));
        
        t1.setGrossFare(new BigDecimal("396.10"));
        t1.setDiscount(new BigDecimal("5.00").negate());
        t1.setAtolChg(new BigDecimal("1.00"));
        t1.setNetSellingFare(new BigDecimal("392.10"));
        
        t1.setCurrencyCode("GBP");
        t1.setDocIssuedate(DateUtil.yyMMddToDate("090316"));
        t1.setNumericAirLineCode("098");
        t1.setTicketNo("3535898495");
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR(CHD)(ID05MAR03)");
        t1.setSurName("MIAH");
        t1.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t1.setTktStatus(TicketStatus.ISSUE);

        Ticket t2 = new Ticket();
        t2.setBaseFare(new BigDecimal("90.00"));
        t2.setTax(new BigDecimal("296.10"));
        t2.setCommission(new BigDecimal("5.00"));
        t2.setNetPurchaseFare(new BigDecimal("386.10"));
        t2.setCurrencyCode("GBP");
        t2.setFee(new BigDecimal("0.00"));
        t2.setGrossFare(new BigDecimal("396.10"));
        t2.setDiscount(new BigDecimal("5.00").negate());
        t2.setAtolChg(new BigDecimal("1.00"));
        t2.setNetSellingFare(new BigDecimal("392.10"));
        t2.setDocIssuedate(DateUtil.yyMMddToDate("090316"));
        t2.setNumericAirLineCode("098");
        t2.setTicketNo("3535898496");
        t2.setPassengerNo(2);
        t2.setForeName("KABIL MSTR");
        t2.setSurName("MIAH");
        t2.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t2.setTktStatus(TicketStatus.ISSUE);

//        Ticket t3 = new Ticket();
//        t3.setBaseFare(new BigDecimal("90.00"));
//        t3.setTax(new BigDecimal("296.10"));
//        t3.setTotalFare(new BigDecimal("386.10"));
//        t3.setCurrencyCode("GBP");
//        t3.setFee(new BigDecimal("0.00"));
//        t3.setDocIssuedate(DateUtil.yyMMddToDate("090316"));
//        t3.setNumericAirLineCode("098");
//        t3.setTicketNo("3535898496");
//        t3.setPassengerNo("03");
//        t3.setPaxForeName("JOSEPH MSTR");
//        t3.setPaxSurName("AKANDA");
//        t3.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
//        t3.setTktStatus(2);
        tickets.add(t1);
        tickets.add(t2);
        //tickets.add(t3);

        return tickets;
    }

    private Set<Ticket> createMockRefundTickets() {

        Set<Ticket> tickets = new LinkedHashSet<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("90.00").negate());
        t1.setTax(new BigDecimal("296.10").negate());
        t1.setCommission(new BigDecimal("5.00"));
        t1.setFee(new BigDecimal("90.00"));
        t1.setNetPurchaseFare(new BigDecimal("296.10").negate());
        
        t1.setGrossFare(new BigDecimal("286.10").negate());
        t1.setDiscount(new BigDecimal("0.00"));
        t1.setAtolChg(new BigDecimal("1.00").negate());
        t1.setNetSellingFare(new BigDecimal("287.10").negate());
        
        t1.setCurrencyCode("GBP");
        t1.setFee(new BigDecimal("0.00"));
        t1.setDocIssuedate(DateUtil.refundDate("02JUL09"));
        t1.setNumericAirLineCode("098");
        t1.setTicketNo("3943767500");
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR         (CHD)");
        t1.setSurName("MIAH");
        t1.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t1.setTktStatus(TicketStatus.REFUND);

        tickets.add(t1);

        return tickets;
    }

    private Set<Ticket> createMockTTPReIssuedTickets() {

        Set<Ticket> tickets = new LinkedHashSet<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("0.00"));
        t1.setTax(new BigDecimal("0.00"));
        t1.setNetPurchaseFare(new BigDecimal("0.00"));
        t1.setCurrencyCode("GBP");
        t1.setFee(new BigDecimal("0.00"));
        t1.setDocIssuedate(DateUtil.yyMMddToDate("090530"));
        t1.setNumericAirLineCode("098");
        t1.setOrginalTicketNo("3535898495");
        t1.setTicketNo("3943767500");
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR(CHD)(ID05MAR03)");
        t1.setSurName("MIAH");
        t1.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t1.setTktStatus(TicketStatus.ISSUE);

        tickets.add(t1);

        return tickets;
    }

    private Set<Itinerary> createMockSegments() {
        Set<Itinerary> segments = new LinkedHashSet<>();

        Itinerary seg1 = new Itinerary();
        seg1.setAirLineCode("AI");
        seg1.setDeptFrom("LONDON LHR");
        seg1.setDeptDate(DateUtil.ddmmToDate("05JUL"));
        seg1.setDeptTime("0945");
        seg1.setArvDate(DateUtil.ddmmToDate("05JUL"));
        seg1.setArvTime("2330");
        seg1.setDeptTo("MUMBAI");

        Itinerary seg2 = new Itinerary();
        seg2.setAirLineCode("AI");
        seg2.setDeptFrom("MUMBAI");
        seg2.setDeptDate(DateUtil.ddmmToDate("06JUL"));
        seg2.setDeptTime("0115");
        seg2.setArvDate(DateUtil.ddmmToDate("06JUL"));
        seg2.setArvTime("0430");
        seg2.setDeptTo("DHAKA");

        Itinerary seg3 = new Itinerary();
        seg3.setAirLineCode("AI");
        seg3.setDeptFrom("DHAKA");
        seg3.setDeptDate(DateUtil.ddmmToDate("24AUG"));
        seg3.setDeptTime("2010");
        seg3.setArvDate(DateUtil.ddmmToDate("06JUL"));
        seg3.setArvTime("2230");
        seg3.setDeptTo("MUMBAI");

        Itinerary seg4 = new Itinerary();
        seg4.setAirLineCode("AI");
        seg4.setDeptFrom("MUMBAI");
        seg4.setDeptDate(DateUtil.ddmmToDate("25AUG"));
        seg4.setDeptTime("0220");
        seg4.setArvDate(DateUtil.ddmmToDate("25AUG"));
        seg4.setArvTime("0730");
        seg4.setDeptTo("LONDON LHR");

        segments.add(seg1);
        segments.add(seg2);
        segments.add(seg3);
        segments.add(seg4);

        return segments;
    }
}
