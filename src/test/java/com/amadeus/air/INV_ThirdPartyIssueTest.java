package com.amadeus.air;

import com.ets.air.AIRToPNRConverter;
import com.ets.air.AIR;
import com.ets.air.FileToAIRToFileConverter;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums.TicketStatus;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Yusuf
 */
public class INV_ThirdPartyIssueTest {
    
    private AIR air;

    public INV_ThirdPartyIssueTest() {

    }

    @Before
    public void setUp() throws URISyntaxException, IOException {
        System.out.println("Test INV Third party issue...");
        URL url = this.getClass().getResource("/AIR/INV_ThirdParty.txt");
        File file = new File(url.toURI());
        FileToAIRToFileConverter converter = new FileToAIRToFileConverter();
        this.air = converter.convert(file);
    }
    
    @Test
    public void testAirToPNR() throws Exception {
        
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        Pnr expResult = new Pnr();
        expResult.setAirCreationDate(DateUtil.yyMMddToDate("110601"));
        expResult.setBookingAgtOid("LONU123IT");
        expResult.setTicketingAgtOid("LONU121DN");
        expResult.setGdsPnr("X6OVXA");
        expResult.setNoOfPax(3);
        expResult.setPnrCreationDate(DateUtil.yyMMddToDate("110525"));
        expResult.setVendorPNR("EY X6OVXA");

        Pnr result = instance.airToPNR();
        assertEquals(expResult.getAirCreationDate(), result.getAirCreationDate());
        assertEquals(expResult.getBookingAgtOid(), result.getBookingAgtOid());
        assertEquals(expResult.getGdsPnr(), result.getGdsPnr());
        assertEquals(expResult.getNoOfPax(), result.getNoOfPax());
        assertEquals(expResult.getPnrCreationDate(), result.getPnrCreationDate());
        assertEquals(expResult.getTicketingAgtOid(), result.getTicketingAgtOid());
        assertEquals(expResult.getVendorPNR(), result.getVendorPNR());
    }

    @Test
    public void testAirToTicket() throws ParseException {
        System.out.println("airToTicket");
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        List<Ticket> expResult = new ArrayList<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("250.00"));
        t1.setTax(new BigDecimal("291.13"));
        t1.setFee(new BigDecimal("0.00"));
        t1.setNetPurchaseFare(new BigDecimal("541.13"));
        t1.setCurrencyCode("GBP");
        t1.setDocIssuedate(DateUtil.yyMMddToDate("110601"));        
        t1.setNumericAirLineCode("607");
        t1.setOrginalTicketNo(null);
        t1.setPassengerNo(1);
        t1.setForeName("RAYHAN MR");
        t1.setSurName("AHMED");
        t1.setRestrictions("VLD EY ONLY/NON ENDO/CHG-RFND-REF TKT OFF/RSTR APPLY/X-30M");
        t1.setTicketNo("2798289186-87");
        t1.setTktStatus(TicketStatus.ISSUE);

        Ticket t2 = new Ticket();
        t2.setBaseFare(new BigDecimal("250.00"));
        t2.setTax(new BigDecimal("291.13"));
        t2.setFee(new BigDecimal("0.00"));
        t2.setNetPurchaseFare(new BigDecimal("541.13"));
        t2.setCurrencyCode("GBP");
        t2.setDocIssuedate(DateUtil.yyMMddToDate("110601"));        
        t2.setNumericAirLineCode("607");
        t2.setOrginalTicketNo(null);
        t2.setPassengerNo(2);
        t2.setForeName("ABUL MR");
        t2.setSurName("KHAIR");
        t2.setRestrictions("VLD EY ONLY/NON ENDO/CHG-RFND-REF TKT OFF/RSTR APPLY/X-30M");
        t2.setTicketNo("2798289188-89");
        t2.setTktStatus(TicketStatus.ISSUE);

        Ticket t3 = new Ticket();
        t3.setBaseFare(new BigDecimal("250.00"));
        t3.setTax(new BigDecimal("291.13"));
        t3.setFee(new BigDecimal("0.00"));
        t3.setNetPurchaseFare(new BigDecimal("541.13"));
        t3.setCurrencyCode("GBP");
        t3.setDocIssuedate(DateUtil.yyMMddToDate("110601"));        
        t3.setNumericAirLineCode("607");
        t3.setOrginalTicketNo(null);
        t3.setPassengerNo(3);
        t3.setForeName("FATIMA MRS");
        t3.setSurName("KHATUN");
        t3.setRestrictions("VLD EY ONLY/NON ENDO/CHG-RFND-REF TKT OFF/RSTR APPLY/X-30M");
        t3.setTicketNo("2798289190-91");
        t3.setTktStatus(TicketStatus.ISSUE);
        
        expResult.add(t1);
        expResult.add(t2);
        expResult.add(t3);
        
        List<Ticket> result = instance.airToTicket();
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
        
        assertEquals(expResult.get(2).getBaseFare(), result.get(2).getBaseFare());
        assertEquals(expResult.get(2).getTax(), result.get(2).getTax());
        assertEquals(expResult.get(2).getCurrencyCode(), result.get(2).getCurrencyCode());
        assertEquals(expResult.get(2).getDocIssuedate(), result.get(2).getDocIssuedate());
        assertEquals(expResult.get(2).getFee(), result.get(2).getFee());
        assertEquals(expResult.get(2).getNetPurchaseFare(), result.get(2).getNetPurchaseFare());
        assertEquals(expResult.get(2).getNumericAirLineCode(), result.get(2).getNumericAirLineCode());
        assertEquals(expResult.get(2).getOrginalTicketNo(), result.get(2).getOrginalTicketNo());
        assertEquals(expResult.get(2).getPassengerNo(), result.get(2).getPassengerNo());
        assertEquals(expResult.get(2).getForeName(), result.get(2).getForeName());
        assertEquals(expResult.get(2).getSurName(), result.get(2).getSurName());
        assertEquals(expResult.get(2).getRestrictions(), result.get(2).getRestrictions());
        assertEquals(expResult.get(2).getTicketNo(), result.get(2).getTicketNo());
        assertEquals(expResult.get(2).getTktStatus(), result.get(2).getTktStatus());
    }
}
