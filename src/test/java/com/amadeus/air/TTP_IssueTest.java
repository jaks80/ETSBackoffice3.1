package com.amadeus.air;

import com.ets.air.AIRToPNRConverter;
import com.ets.air.AIR;
import com.ets.air.FileToAIRToFileConverter;
import com.ets.pnr.domain.Airline;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import com.ets.util.Enums.TicketStatus;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Yusuf
 */
public class TTP_IssueTest {

    private AIR air;

    public TTP_IssueTest() {

    }

    @Before
    public void setUp() throws URISyntaxException, IOException {
        System.out.println("Test TTP Issue");
        
        URL url = this.getClass().getResource("/AIR/TTP_Issue.txt");
        File file = new File(url.toURI());
        FileToAIRToFileConverter converter = new FileToAIRToFileConverter();
        this.air = converter.convert(file);
    }

    @Test
    public void testAirToCareer() {
        //System.out.println("Test career...");
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);

        Airline result = instance.airToCareer();

        assertEquals("NACIL AIR INDIA", result.getName());
        assertEquals("AI", result.getCode());
    }

    @Test
    public void testAirToPNR() throws Exception {
        
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        Pnr expResult = new Pnr();
        expResult.setAirCreationDate(DateUtil.yyMMddToDate("090316"));
        expResult.setBookingAgtOid("BONU123IK");
        expResult.setTicketingAgtOid("LONU123IT");
        expResult.setAirLineCode("AI");
        expResult.setGdsPnr("5HH342");
        expResult.setNoOfPax(2);
        expResult.setPnrCreationDate(DateUtil.yyMMddToDate("090313"));
        expResult.setVendorPNR("AI HJ3HV");

        Pnr result = instance.airToPNR();
        assertEquals(expResult.getAirCreationDate(), result.getAirCreationDate());
        assertEquals(expResult.getBookingAgtOid(), result.getBookingAgtOid());
        assertEquals(expResult.getGdsPnr(), result.getGdsPnr());
        assertEquals(expResult.getAirLineCode(), result.getAirLineCode());
        assertEquals(expResult.getNoOfPax(), result.getNoOfPax());
        assertEquals(expResult.getPnrCreationDate(), result.getPnrCreationDate());
        assertEquals(expResult.getTicketingAgtOid(), result.getTicketingAgtOid());
        assertEquals(expResult.getVendorPNR(), result.getVendorPNR());
    }

    @Test
    public void testAirToTicket() throws ParseException {
        
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        List<Ticket> expResult = new ArrayList<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("90.00"));
        t1.setTax(new BigDecimal("296.10"));
        t1.setNetPurchaseFare(new BigDecimal("386.10"));
        t1.setCurrencyCode("GBP");
        t1.setDocIssuedate(DateUtil.yyMMddToDate("090316"));
        t1.setFee(new BigDecimal("0.00"));
        t1.setNumericAirLineCode("098");
        t1.setOrginalTicketNo(null);
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR(CHD)(ID05MAR03)");
        t1.setSurName("MIAH");
        t1.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t1.setTicketNo("3535898495");
        t1.setTktStatus(TicketStatus.ISSUE);

        Ticket t2 = new Ticket();
        t2.setBaseFare(new BigDecimal("90.00"));
        t2.setTax(new BigDecimal("296.10"));
        t2.setNetPurchaseFare(new BigDecimal("386.10"));
        t2.setCurrencyCode("GBP");
        t2.setDocIssuedate(DateUtil.yyMMddToDate("090316"));
        t2.setFee(new BigDecimal("0.00"));
        t2.setNumericAirLineCode("098");
        t2.setOrginalTicketNo(null);
        t2.setPassengerNo(2);
        t2.setForeName("KABIL MSTR");
        t2.setSurName("MIAH");
        t2.setRestrictions("VALID ON AI ONLY PNR HJ3HV, NON-END/RER/CONV INTO MCO INBOUND 1ST CHANGE FREE/DC/");
        t2.setTicketNo("3535898496");
        t2.setTktStatus(TicketStatus.ISSUE);

        expResult.add(t1);
        expResult.add(t2);
        List<Ticket> result = new ArrayList(instance.airToTicket());
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getBaseFare(), result.get(0).getBaseFare());
        assertEquals(expResult.get(0).getTax(), result.get(0).getTax());
        assertEquals(expResult.get(0).getCurrencyCode(), result.get(0).getCurrencyCode());
        assertEquals(expResult.get(0).getDocIssuedate(), result.get(0).getDocIssuedate());
        assertEquals(expResult.get(0).getFee(), result.get(0).getFee());
        assertEquals(expResult.get(0).getNetPurchaseFare(), result.get(0).getNetPurchaseFare());
        assertEquals(expResult.get(0).getNumericAirLineCode(), result.get(0).getNumericAirLineCode());
        assertEquals(expResult.get(0).getOrginalTicketNo(), result.get(0).getOrginalTicketNo());
        assertEquals(expResult.get(0).getPassengerNo(), result.get(0).getPassengerNo());
        assertEquals(expResult.get(0).getForeName(), result.get(0).getForeName());
        assertEquals(expResult.get(0).getSurName(), result.get(0).getSurName());
        assertEquals(expResult.get(0).getRestrictions(), result.get(0).getRestrictions());
        assertEquals(expResult.get(0).getTicketNo(), result.get(0).getTicketNo());
        assertEquals(expResult.get(0).getTktStatus(), result.get(0).getTktStatus());
        assertEquals(expResult.get(0).getTktStatusString(), result.get(0).getTktStatusString());
        
        assertEquals(expResult.get(1).getBaseFare(), result.get(1).getBaseFare());
        assertEquals(expResult.get(1).getTax(), result.get(1).getTax());
        assertEquals(expResult.get(1).getCurrencyCode(), result.get(1).getCurrencyCode());
        assertEquals(expResult.get(1).getDocIssuedate(), result.get(1).getDocIssuedate());
        assertEquals(expResult.get(1).getFee(), result.get(1).getFee());
        assertEquals(expResult.get(1).getNetPurchaseFare(), result.get(1).getNetPurchaseFare());
        assertEquals(expResult.get(1).getNumericAirLineCode(), result.get(1).getNumericAirLineCode());
        assertEquals(expResult.get(1).getOrginalTicketNo(), result.get(1).getOrginalTicketNo());
        assertEquals(expResult.get(1).getPassengerNo(), result.get(1).getPassengerNo());
        assertEquals(expResult.get(1).getForeName(), result.get(1).getForeName());
        assertEquals(expResult.get(1).getSurName(), result.get(1).getSurName());
        assertEquals(expResult.get(1).getRestrictions(), result.get(1).getRestrictions());
        assertEquals(expResult.get(1).getTicketNo(), result.get(1).getTicketNo());
        assertEquals(expResult.get(1).getTktStatus(), result.get(1).getTktStatus());
    }
}
