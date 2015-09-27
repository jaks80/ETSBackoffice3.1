package com.ets.air.service;

import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accountingdoc.service.TSalesAcDocService;
import com.ets.air.dao.AirDAO;
import com.ets.client.domain.Agent;
import com.ets.client.service.AgentService;
import com.ets.pnr.dao.*;
import com.ets.pnr.domain.*;
import com.ets.pnr.logic.PnrBusinessLogic;
import com.ets.pnr.service.AirlineService;
import com.ets.pnr.service.TicketService;
import com.ets.util.Enums;
import com.ets.pnr.logic.PnrUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("airService")
public class AirService {

    @Resource(name = "airDAO")
    private AirDAO dao;
    @Autowired
    AgentService agentService;

    @Resource(name = "itineraryDAO")
    private ItineraryDAO itineraryDAO;

    @Resource(name = "ticketDAO")
    private TicketDAO ticketDAO;
    @Autowired
    TicketService ticketService;

    @Autowired
    AirlineService airlineService;
    @Autowired
    TSalesAcDocService tSalesAcDocService;

    public AirService() {

    }

    /**
     * This method is for Booking to Issue or Direct Issue or Re Issue If Pnr
     * exist update it otherwise insert. Its children needs to be updated too.
     *
     * @param newPnr
     * @param fileType
     * @return
     */
    public Pnr savePnr(Pnr newPnr, String fileType) {

        Pnr persistedPnr = findPnr(newPnr.getGdsPnr(), newPnr.getPnrCreationDate());

        if (persistedPnr == null) {
            persistedPnr = newPnr;
        } else {
            //BT should not override issued stuff.
            if (fileType.equals("INV") || fileType.equals("TTP") || fileType.equals("TTP/BTK")) {

                if (checkVoidInvoiceNeeded(newPnr, persistedPnr)) {// Do this before updating tickets
                    persistedPnr = findPnr(newPnr.getGdsPnr(), newPnr.getPnrCreationDate());
                }

                persistedPnr = PnrUtil.updatePnr(persistedPnr, newPnr);
                Set<Ticket> dbTickets = persistedPnr.getTickets();
                persistedPnr.setTickets(PnrUtil.updateTickets(dbTickets, newPnr.getTickets()));
                itineraryDAO.deleteBulk(persistedPnr.getSegments());//Saving final segments. Issued segments are final segments.
                persistedPnr.setSegments(newPnr.getSegments());
            } else {
                return null;//Imrpove BT logic here
            }
        }

        Airline airline = airlineService.find(persistedPnr.getAirLineCode());
        if (airline != null) {
            setBspCommission(persistedPnr.getTickets(), airline);
        }

        PnrUtil.initPnrChildren(persistedPnr);

        Agent ticketing_agent = agentService.findByOfficeID(persistedPnr.getTicketingAgtOid());
        persistedPnr.setTicketing_agent(ticketing_agent);
        save(persistedPnr);

        PnrUtil.undefinePnrChildren(persistedPnr); //Undefine cyclic dependencies to avoid cyclic xml exception

        return persistedPnr;
    }

    public List<Ticket> refundTicket(List<Ticket> tickets) {

        Pnr persistedPnr = dao.findPnr(tickets.get(0).getTicketNo(), tickets.get(0).getSurName());
        List<Ticket> tobePersisted = new ArrayList<>();

        //Check if ticket already exist in db. If exist then ignore
        for (Ticket tn : tickets) {
            boolean exist = false;
            for (Ticket to : persistedPnr.getTickets()) {
                if (tn.getTicketNo().equals(to.getTicketNo())
                        && tn.getSurName().equals(tn.getSurName())
                        && tn.getTktStatusString().equals(to.getTktStatusString())) {
                    exist = true;
                }
            }

            if (!exist) {
                tobePersisted.add(tn);
            }
        }

        if (!tobePersisted.isEmpty()) {

            Airline airline = airlineService.find(persistedPnr.getAirLineCode());
            if (airline != null) {
                setBspCommission(new LinkedHashSet<>(tobePersisted), airline);
            }

            PnrUtil.initPnrInTickets(persistedPnr, tobePersisted);
            ticketDAO.saveBulk(tobePersisted);
            PnrUtil.undefinePnrInTickets(persistedPnr, tobePersisted);
        }
        return tickets;
    }

    public List<Ticket> voidTicket(List<Ticket> tickets, Pnr pnr) {
        for (Ticket t : tickets) {
            ticketService._void(pnr.getGdsPnr(), t.getNumericAirLineCode(), t.getTicketNo(), t.getSurName());
        }
        return tickets;
    }

    private void setBspCommission(Set<Ticket> tickets, Airline airline) {
        for (Ticket t : tickets) {
            t.setCommission(airline.calculateBspCom(t));
        }
    }

    public Pnr findPnr(String gdsPnr, Date pnrCreationdate) {
        return dao.findPnr(gdsPnr, pnrCreationdate);
    }

    public void save(Pnr pnr) {
        pnr.setFirstSegment(PnrBusinessLogic.getFirstSegmentSummery(pnr.getSegments()));
        
        Ticket leadPaxTicket = PnrBusinessLogic.calculateLeadPaxTicket(pnr.getTickets());
        pnr.setLeadPax(leadPaxTicket.getSurName()+"/"+leadPaxTicket.getForeName()+"/"+leadPaxTicket.getFullTicketNo());
        
        dao.save(pnr);
    }

    //VOID sales document if booking to issue
    public boolean checkVoidInvoiceNeeded(Pnr newPnr, Pnr persistedPnr) {

        for (Ticket newTicket : newPnr.getTickets()) {
            for (Ticket oldTicket : persistedPnr.getTickets()) {
                if ((newTicket.getSurName().equals(oldTicket.getSurName())
                        && newTicket.getTktStatus().equals(Enums.TicketStatus.ISSUE)
                        && oldTicket.getTktStatus().equals(Enums.TicketStatus.BOOK))) {

                    //Reloading pnr is neseccasry because its sales invoice is voided.
                    //For Booking to Issue purpose
                    voidSalesInvoice(oldTicket, persistedPnr);
                    return true;
                }
            }
        }
        return false;
    }

    public void voidSalesInvoice(Ticket ticket, Pnr persistedPnr) {
        TicketingSalesAcDoc invoice = tSalesAcDocService.findInvoiceByPaxName(ticket.getSurName(),
                ticket.getTktStatus(), persistedPnr.getId());
        if(invoice!=null){
         tSalesAcDocService.voidInvoiceByAIRReader(invoice);
        }
    }
}
