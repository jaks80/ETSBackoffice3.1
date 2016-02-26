package com.ets.air;

import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Remark;
import com.ets.pnr.domain.Ticket;
import com.ets.air.service.AirService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("aIRReader")
public class AIRReader {

    @Resource(name = "airService")
    private AirService service;

    private AIR air;

    public AIRReader() {
    }

    public void startReading() {

        AIRToPNRConverter converter = new AIRToPNRConverter(this.air);

        if (null != air.getType()) {
            switch (air.getType()) {
                case "TTP": {
                    Pnr pnr = converter.airToPNR();
                    List<Ticket> tickets = converter.airToTicket();
                    List<Itinerary> segments = converter.airToItinerary();
                    List<Remark> remarks = converter.airToPNRRemarks();
                    for (AIR a : air.getMorePages()) {
                        converter = new AIRToPNRConverter(a);
                        tickets.addAll(converter.airToTicket());
                    }

                    pnr.setTickets(new LinkedHashSet(tickets));
                    pnr.setSegments(new LinkedHashSet(segments));
                    //pnr.setRemarks(new LinkedHashSet(remarks));
                    Pnr persistedPnr = service.savePnr(pnr,air.getType());
                    service.saveRemarks(remarks,persistedPnr);
                    break;
                }
                case "TRFP": {
                    List<Ticket> tickets = converter.airToRefundedTicket();
                    for (AIR a : air.getMorePages()) {
                        converter = new AIRToPNRConverter(a);
                        tickets.addAll(converter.airToTicket());
                    }
                    service.refundTicket(tickets);
                    break;
                }
                case "VOID": {
                    Pnr pnr = converter.airToPNR();
                    List<Ticket> tickets = converter.airToVoidTicket();
                    for (AIR a : air.getMorePages()) {
                        converter = new AIRToPNRConverter(a);
                        tickets.addAll(converter.airToTicket());
                    }
                    service.voidTicket(tickets, pnr);
                    break;
                }
                case "INV":
                case "BT":
                case "ET":
                case "TTP/BTK":{
                    Pnr pnr = converter.airToPNR();
                    List<Ticket> tickets = new ArrayList<>();
                    
                    if(!"BT".equals(air.getType())){
                     tickets = converter.airToTicket();
                    }else{
                      tickets = converter.airToTicketForBTFile();
                    }
                    
                    List<Itinerary> segments = converter.airToItinerary();
                    List<Remark> remarks = converter.airToPNRRemarks();
                    for (AIR a : air.getMorePages()) {
                        converter = new AIRToPNRConverter(a);
                        tickets.addAll(converter.airToTicket());
                    }
                    pnr.setTickets(new LinkedHashSet(tickets));
                    pnr.setSegments(new LinkedHashSet(segments));
                    //pnr.setRemarks(new LinkedHashSet(remarks));
                    boolean issued = false;

                    for (Ticket t : tickets) {
                        //t.setBaseFare(new BigDecimal("0.00"));//For entry other then TTP fare entry is not accurate, Hence it is set to 0 for manual entry by user.
                        if ("ISSUE".equals(t.getTktStatusString()) || "REISSUE".equals(t.getTktStatusString())) {
                            issued = true;
                            break;
                        }
                    }

                    //Third party fare make 0.00
//                    if (air.getType().equals("BT")||air.getType().equals("ET")||
//                            !AppSettingsService.mainAgent.getOfficeID().contains(pnr.getTicketingAgtOid())) {
//                        //Its third party or BT, so force purchase fare 0.00
//                        for (Ticket t : tickets) {
//                            t.setBaseFare(new BigDecimal("0.00"));
//                            t.setTax(new BigDecimal("0.00"));
//                            t.setCommission(new BigDecimal("0.00"));
//                            t.setFee(new BigDecimal("0.00"));
//                        }
//                    }

                    if (issued) {
                        service.savePnr(pnr,air.getType());
                    } else {
                        pnr.setTicketingAgtOid(null);
                        pnr.setTicketingAgentSine(null);
                        Pnr persistedPnr = service.savePnr(pnr,air.getType());
                        service.saveRemarks(remarks,persistedPnr);
                    }
                    break;
                }
            }
        }
    }

    public void setAir(AIR air) {
        this.air = air;
    }

}
