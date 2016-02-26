package com.ets.pnr.logic;

import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accountingdoc.service.AcDocUtil;
import com.ets.accountingdoc.service.TSalesAcDocService;
import com.ets.exception.PastSegmentException;
import com.ets.exception.ValidAccountingDocumentExistException;
import com.ets.exception.ValidTicketExist;
import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf Akhond
 */
@Service("pnrDeleteLogic")
public class PnrDeleteLogic {

    @Resource(name = "tSalesAcDocService")
    private TSalesAcDocService tSalesAcDocService;

    //private String message;
    /**
     * Pnr can be deleted if there are: 1. No valid accounting documents 2. No
     * valid ticket 3. Segment is not past
     *
     * @param completePnr
     * @param todayFromUsersMachine
     * @return
     * @throws com.ets.exception.ValidAccountingDocumentExistException
     * @throws com.ets.exception.PastSegmentException
     * @throws com.ets.exception.ValidTicketExist
     */
    public boolean deletable(final Pnr completePnr, final Date todayFromUsersMachine) 
            throws ValidAccountingDocumentExistException, PastSegmentException, ValidTicketExist {

        // boolean deletable = false;
        //message = "";
        List<TicketingSalesAcDoc> accountingDocs = tSalesAcDocService.getByPnrId(completePnr.getId());
        Set<TicketingSalesAcDoc> valid_accountingDocs = AcDocUtil.filterVoidDocuments(new HashSet(accountingDocs));

        Itinerary flightSummery = PnrBusinessLogic.getFirstSegment(completePnr.getSegments());

        boolean pastSegment = false;
        if (flightSummery != null) {
            pastSegment = flightSummery.getDeptDate().after(todayFromUsersMachine);
        }

        Set<Ticket> tickets = completePnr.getTickets();
        Set<Ticket> validTickets = new HashSet();

        if (!tickets.isEmpty()) {
            validTickets = PnrUtil.filterValidTicket(tickets);
        }

        if (!validTickets.isEmpty()) {
            throw new ValidTicketExist("Valid ticket exist");
        }

        if (!valid_accountingDocs.isEmpty()) {
            throw new ValidAccountingDocumentExistException("Valid sales/purchase invoice exist");
        }

        if (pastSegment) {
            if(!validTickets.isEmpty()){
             throw new PastSegmentException("Past segment PNR can not be deleted");
            }
        }

        return true;
    }
}
