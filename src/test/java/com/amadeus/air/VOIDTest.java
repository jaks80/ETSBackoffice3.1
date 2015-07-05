package com.amadeus.air;

import com.ets.air.AIRToPNRConverter;
import com.ets.air.AIR;
import com.ets.air.FileToAIRToFileConverter;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums;
import com.ets.util.Enums.TicketStatus;
import java.io.File;
import java.io.IOException;
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
public class VOIDTest {

    private AIR air;

    public VOIDTest() {

    }

    @Before
    public void setUp() throws URISyntaxException, IOException {
        System.out.println("Test VOID Ticket");
        URL url = this.getClass().getResource("/AIR/Void.txt");
        File file = new File(url.toURI());
        FileToAIRToFileConverter converter = new FileToAIRToFileConverter();
        this.air = converter.convert(file);
    }

    @Test
    public void testAirToVOIDTicket() throws ParseException {
        
        AIRToPNRConverter instance = new AIRToPNRConverter(this.air);
        List<Ticket> expResult = new ArrayList<>();
        
        Ticket t1 = new Ticket();
        
        t1.setNumericAirLineCode("229");
        t1.setTicketNo("2797312568-69");        
        t1.setPassengerNo(1);
        t1.setForeName("RAHENA MRS");
        t1.setSurName("BEGUM CHOWDHURY");        
        t1.setTktStatus(TicketStatus.VOID);                
        expResult.add(t1);        
        
        List<Ticket> result = instance.airToVoidTicket();
        
        assertEquals(expResult.get(0).getNumericAirLineCode(), result.get(0).getNumericAirLineCode());        
        assertEquals(expResult.get(0).getPassengerNo(), result.get(0).getPassengerNo());
        assertEquals(expResult.get(0).getForeName(), result.get(0).getForeName());
        assertEquals(expResult.get(0).getSurName(), result.get(0).getSurName());        
        assertEquals(expResult.get(0).getTicketNo(), result.get(0).getTicketNo());
        assertEquals(expResult.get(0).getTktStatus(), result.get(0).getTktStatus());        
    }
}
