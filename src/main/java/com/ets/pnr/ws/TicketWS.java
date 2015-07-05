package com.ets.pnr.ws;

import com.ets.pnr.domain.Ticket;
import com.ets.pnr.model.GDSSaleReport;
import com.ets.pnr.model.TicketSaleReport;
import com.ets.pnr.service.TicketService;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/ticket-management")
public class TicketWS {

    @Autowired
    TicketService service;

    @PUT
    @Path("/update")
    @RolesAllowed("GS")
    public Ticket update(Ticket ticket) {
        service.update(ticket);
        return ticket;
    }

    @POST
    @Path("/updatepurchase")
    @RolesAllowed("GS")
    public Response updatePurchase(Ticket ticket) {
        int status = service.updatePurchase(ticket);
        if (status == 1) {
            return Response.status(200).build();
        } else {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/void")
    @RolesAllowed("SM")
    public Response _void(@QueryParam("pnr") String pnr,
            @QueryParam("airlinecode") String airlineCode,
            @QueryParam("tktno") String tktno,
            @QueryParam("surname") String surname) {
        service._void(pnr, airlineCode, tktno, surname);
        return Response.status(200).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("SM")
    public Response delete(@PathParam("id") long id) {

        if (service.delete(id)) {
            return Response.status(200).build();
        } else {
            return Response.status(500).build();
        }
    }

    @GET
    @Produces("application/xml")
    @Path("/gds-salereport")
    @RolesAllowed("SM")
    public GDSSaleReport gdsSaleReport(@QueryParam("ticketingType") Enums.TicketingType ticketingType,
            @QueryParam("ticketStatus") Enums.TicketStatus ticketStatus,
            @QueryParam("airLineCode") String airLineCode,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd,
            @QueryParam("ticketingAgtOid") String ticketingAgtOid) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        GDSSaleReport report = service.saleReport(ticketingType, ticketStatus, airLineCode, dateFrom, dateTo, ticketingAgtOid);
        return report;
    }

    @GET
    @Produces("application/xml")
    @Path("/salereport")
    @RolesAllowed("SM")
    public TicketSaleReport saleReport(@QueryParam("ticketingType") Enums.TicketingType ticketingType,
            @QueryParam("ticketStatus") Enums.TicketStatus ticketStatus,
            @QueryParam("airLineCode") String airLineCode,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd,
            @QueryParam("ticketingAgtOid") String ticketingAgtOid,
            @QueryParam("userid") Long userid) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        TicketSaleReport report = service.saleRevenueReport(userid,ticketingType, ticketStatus, airLineCode, dateFrom, dateTo, ticketingAgtOid);
        return report;
    }
}
