package com.amadeus.air;

import com.ets.air.AIRToPNRConverter;
import com.ets.air.AIR;
import com.ets.air.FileToAIRToFileConverter;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums.TicketStatus;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Yusuf
 */
public class TRFPTest {

    private AIR air;

    public TRFPTest() {

    }

    @Before
    public void setUp() throws URISyntaxException, IOException {
        System.out.println("Test TRFP");
        URL url = this.getClass().getResource("/AIR/TRFP.txt");
        File file = new File(url.toURI());
        FileToAIRToFileConverter converter = new FileToAIRToFileConverter();
        this.air = converter.convert(file);
    }
    
    @Test
    public void testAirToTicket() {
        
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        List<Ticket> expResult = new ArrayList<>();

        Ticket t1 = new Ticket();
        t1.setBaseFare(new BigDecimal("306.00").negate());
        t1.setTax(new BigDecimal("164.60").negate());
        t1.setFee(new BigDecimal("80.00"));
        t1.setNetPurchaseFare(new BigDecimal("390.60").negate());
        //t1.setCurrencyCode("GBP");
        t1.setDocIssuedate(DateUtil.refundDate("02JUL09"));        
        t1.setNumericAirLineCode("098");
        t1.setTicketNo("3943767500");
        t1.setOrginalTicketNo(null);
        t1.setPassengerNo(1);
        t1.setForeName("NABIL MSTR         (CHD)");
        t1.setSurName("MIAH");
        t1.setRestrictions(null);
        t1.setTktStatus(TicketStatus.REFUND);

        expResult.add(t1);

        List<Ticket> result = instance.airToRefundedTicket();
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.get(0).getBaseFare(), result.get(0).getBaseFare());
        assertEquals(expResult.get(0).getTax(), result.get(0).getTax());
        assertEquals(expResult.get(0).getCurrencyCode(), result.get(0).getCurrencyCode());
        assertEquals(expResult.get(0).getDocIssuedate(), result.get(0).getDocIssuedate());
        assertEquals(expResult.get(0).getFee(), result.get(0).getFee());
        assertEquals(expResult.get(0).getNetPurchaseFare(), result.get(0).getNetPurchaseFare());
        assertEquals(expResult.get(0).getBaseFare().add(expResult.get(0).getTax()).add(expResult.get(0).getFee()),
                result.get(0).getBaseFare().add(result.get(0).getTax()).add(result.get(0).getFee()));
        
        assertEquals(expResult.get(0).getNumericAirLineCode(), result.get(0).getNumericAirLineCode());
        //assertEquals(expResult.get(0).getOrginalTicketNo(), result.get(0).getOrginalTicketNo());
        assertEquals(expResult.get(0).getPassengerNo(), result.get(0).getPassengerNo());
        assertEquals(expResult.get(0).getForeName(), result.get(0).getForeName());
        assertEquals(expResult.get(0).getSurName(), result.get(0).getSurName());
        assertEquals(expResult.get(0).getRestrictions(), result.get(0).getRestrictions());
        assertEquals(expResult.get(0).getTicketNo(), result.get(0).getTicketNo());
        assertEquals(expResult.get(0).getTktStatus(), result.get(0).getTktStatus());

    }
}
