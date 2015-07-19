package com.ets.pnr.service;

import com.ets.accountingdoc.service.TPurchaseAcDocService;
import com.ets.pnr.dao.TicketDAO;
import com.ets.pnr.domain.Ticket;
import com.ets.pnr.model.GDSSaleReport;
import com.ets.pnr.model.TicketSaleReport;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.Enums;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("ticketService")
public class TicketService {

    @Resource(name = "ticketDAO")
    private TicketDAO dao;
    @Autowired
    TPurchaseAcDocService tPurchaseAcDocService;
    @Autowired
    private PnrService pnrService;

    public Ticket update(Ticket ticket) {
        dao.save(ticket);
        return ticket;
    }

    public int updatePurchase(Ticket ticket) {
        int status = dao.updatePurchase(ticket);
        return status;
    }

    public int _void(String pnr, String airlineCode, String tktno, String surname) {
        int status = dao.voidTicket(pnr, airlineCode, tktno, surname);
        return status;
    }

    public boolean delete(long id) {
        Ticket t = dao.findByID(Ticket.class, id);
        dao.delete(t);
        return true;
    }

    public void saveBulk(List<Ticket> tickets) {
        dao.saveBulk(tickets);
    }

    public GDSSaleReport saleReport(Enums.TicketingType ticketingType, Enums.TicketStatus ticketStatus, String airLineCode,
            Date issueDateFrom, Date issueDateTo, String ticketingAgtOid) {

        String[] tktedOIDs = null;
        String[] airLineCodes = null;

        if (airLineCode != null) {
            airLineCodes = airLineCode.split(",");
        }

        if (ticketingAgtOid != null) {
            tktedOIDs = ticketingAgtOid.split(",");
        } else {
            if (ticketingType.equals(Enums.TicketingType.IATA)) {
                tktedOIDs = AppSettingsService.mainAgent.getOfficeIDCollection();
            } else {
                String mainagentoid = AppSettingsService.mainAgent.getOfficeID();
                Set<String> oidset = pnrService.findTicketingOIDs();//All tkting oid
                //Remove Main agent office ids
                Iterator<String> sit = oidset.iterator();
                while (sit.hasNext()) {
                    if (mainagentoid.matches(sit.next())) {
                        sit.remove();
                    }
                }

                tktedOIDs = oidset.toArray(new String[oidset.size()]);
            }
        }

        List<Ticket> tickets = dao.saleReport(ticketStatus, airLineCodes, issueDateFrom, issueDateTo, tktedOIDs);
        GDSSaleReport report = new GDSSaleReport(tickets);

        return report;
    }

    public TicketSaleReport saleRevenueReport(Long userid, Enums.TicketingType ticketingType,
            Enums.TicketStatus ticketStatus, String airLineCode,
            Date issueDateFrom, Date issueDateTo, Enums.ClientType clienttype,
            Long clientid,String ticketingAgtOid) {

        String[] tktedOIDs = null;
        String[] airLineCodes = null;

        if (airLineCode != null) {
            airLineCodes = airLineCode.split(",");
        }

        if (ticketingAgtOid != null) {
            tktedOIDs = ticketingAgtOid.split(",");
        }

        List<Ticket> tickets = dao.saleRevenueReport(userid, ticketStatus, airLineCodes, issueDateFrom,
                issueDateTo,clienttype,clientid, tktedOIDs);
        TicketSaleReport report = TicketSaleReport.serializeToSalesSummery(tickets, issueDateFrom, issueDateTo);
        report.setReportTitle("Sale Report: AIR Ticket");
        return report;
    }
}
