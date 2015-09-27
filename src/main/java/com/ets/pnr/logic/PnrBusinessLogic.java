package com.ets.pnr.logic;

import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author User
 */
public class PnrBusinessLogic {

    public static Ticket calculateLeadPaxTicket(Set<Ticket> ticket_list) {
        Ticket leadPax = null;
        int paxNo = 99;

        for (Ticket t : ticket_list) {
            if (t.getPassengerNo() <= paxNo && (!t.isChild() && !t.isInfant())) {
                leadPax = t;
                paxNo = t.getPassengerNo();
            }
        }
        if (leadPax != null) {
            return leadPax;
        } else {
            Iterator<Ticket> iterator = ticket_list.iterator();
            Ticket setElement = new Ticket();
            while (iterator.hasNext()) {
                setElement = iterator.next();
                break;
            }
            return setElement;
        }
    }

    public static String calculateLeadPaxName(Set<Ticket> ticket_list) {

        Ticket ticket = calculateLeadPaxTicket(ticket_list);
        return ticket.getFullPaxName();
    }
    
    public static String getFirstSegmentSummery(Set<Itinerary> segments) {
        StringBuilder sb = new StringBuilder();

        Itinerary seg = segments.iterator().next();
        sb.append(DateUtil.dateTOddmm(seg.getDeptDate())).append("/");
        sb.append(seg.getDeptFrom()).append("-").append(seg.getDeptTo()).append("/");
        sb.append(seg.getAirLineCode());

        return sb.toString();
    }
    
    /**
     * Calculating first segment based on Id. It should be based on segment no.
     * @param segments
     * @return 
     */
    public static Itinerary getFirstSegment(Set<Itinerary> segments) {
        Itinerary firstSegment = null;
        Long index = null;

        for (Itinerary i : segments) {
            if (index == null) {
                index = i.getId();
                firstSegment = i;
            } else {
                if (i.getId() < index) {
                    index = i.getId();
                    firstSegment = i;
                }
            }
        }
        return firstSegment;
    }    

}
