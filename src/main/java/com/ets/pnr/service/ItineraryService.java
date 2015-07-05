package com.ets.pnr.service;

import com.ets.pnr.dao.ItineraryDAO;
import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Ticket;
import com.ets.pnr.model.SegmentReport;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("itineraryService")
public class ItineraryService {

    @Resource(name = "itineraryDAO")
    private ItineraryDAO dao;

    public Itinerary save(Itinerary itinerary) {
        dao.save(itinerary);
        
        return itinerary;
    }
        
    public SegmentReport segmentReport(String ticketStatus,
            String airLineCode, String from, String to, String officeId) {

        Integer status = null;
        String[] officeIds = null;
        String[] airLineCodes = null;

        if (!"null".equals(ticketStatus) && ticketStatus != null) {
            status = Enums.TicketStatus.valueOf(ticketStatus).getId();
        }

        if ("null".equals(airLineCode) || airLineCode == null) {
            airLineCode = null;
        } else {
            airLineCodes = airLineCode.split(",");
        }

        if ("null".equals(officeId) || officeId == null) {
            officeId = null;
        } else {
            officeIds = officeId.split(",");
        }

        Date dateFrom = DateUtil.stringToDate(from, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(to, "ddMMMyyyy");
        List results = dao.findSegments(status, airLineCodes, dateFrom, dateTo, officeIds);

        SegmentReport report = new SegmentReport();
        
         for (int i = 0; i < results.size(); i++) {
            Object[] object = (Object[]) results.get(i);
            
            Ticket ticket = (Ticket) object[1];
            Itinerary segment = (Itinerary) object[0];                            
            report.addSegment(segment, segment.getPnr(), ticket);
        }
        report.generateSummery();
        return report;
    }
}
