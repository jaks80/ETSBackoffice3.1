package com.ets.pnr.ws;

import com.ets.pnr.model.collection.Pnrs;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.model.ATOLCertificate;
import com.ets.pnr.service.PnrService;
import com.ets.util.DateUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
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
@Path("/pnr-management")
@Consumes("application/xml")
@Produces("application/xml")
public class PnrWS {

    @Autowired
    private PnrService service;

    @POST
    @Path("/new")
    @RolesAllowed("GS")
    public Pnr create(Pnr pnr) {
        service.save(pnr);
        return pnr;
    }

    @PUT
    @Path("/update")
    @RolesAllowed("GS")
    public Pnr update(Pnr pnr) {
        service.save(pnr);
        return pnr;
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("SM")
    public Response delete(@PathParam("id") long id, @QueryParam("date") String date) {

        Date _date = DateUtil.stringToDate(date, "ddMMMyyyy");
        String message = service.delete(id, _date);
        if ("Deleted".equals(message)) {
            return Response.status(200).entity(message).build();
        } else {
            return Response.status(500).entity(message).build();
        }
    }

    @GET
    @Path("/history")
    @RolesAllowed("SM")
    public Pnrs getHistory(@QueryParam("bookingAgtOid") String bookingAgtOid,
            @QueryParam("ticketingAgtOid") String ticketingAgtOid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {
        Pnrs pnrs = service.pnrHistory(dateStart, dateEnd, ticketingAgtOid, bookingAgtOid);
        return pnrs;
    }

    @GET
    @Path("/byid/{id}")
    @RolesAllowed("GS")
    public Pnr getById(@PathParam("id") long id) {
        return service.getByIdWithChildren(id);
    }

    @GET
    @Path("/atolcertbyid")
    @RolesAllowed("GS")
    public ATOLCertificate getAtolCertificate(@QueryParam("pnrid") long pnrid, @QueryParam("certissuedate") String certissuedate) {
        Date date = DateUtil.stringToDate(certissuedate, "ddMMMyyyy");
        return service.getAtolCertificate(pnrid, date);
    }

    @GET
    @Path("/withchildren/{id}")
    @RolesAllowed("GS")
    public Pnr getByIdWithChildren(@PathParam("id") long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("/bytkt/{tktNo}/{surName}")
    @RolesAllowed("GS")
    public Pnrs getPnrByTktNo(@QueryParam("tktNo") String tktNo, @QueryParam("surName") String surName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("/bypaxname")
    @RolesAllowed("GS")
    public Pnrs getPnrByName(@QueryParam("surName") String surName, @QueryParam("foreName") String foreName) {
        List<Pnr> list = service.getPnrByName(surName, foreName);
        Pnrs pnrs = new Pnrs();
        pnrs.setList(list);
        return pnrs;
    }

    @GET
    @Path("/bygdsPnr")
    @RolesAllowed("GS")
    public Pnrs getPnrBygdsPnr(@QueryParam("gdsPnr") String gdsPnr) {
        List<Pnr> list = service.getByGDSPnr(gdsPnr);
        Pnrs pnrs = new Pnrs();
        pnrs.setList(list);
        return pnrs;
    }

    @GET
    @Path("/byginvref")
    @RolesAllowed("GS")
    public Pnrs getPnrByInvRef(@QueryParam("invref") String invref) {
        List<Pnr> list = service.getByInvRef(invref);
        Pnrs pnrs = new Pnrs();
        pnrs.setList(list);
        return pnrs;
    }

    @GET
    @Path("/uninvoicedpnr")
    @RolesAllowed("GS")
    public Pnrs getUninvoicedPnr() {

        List<Pnr> list = service.searchUninvoicedPnr();
        Pnrs pnrs = new Pnrs();
        pnrs.setList(list);

        return pnrs;
    }

    @GET
    @Path("/pnrtoday")
    @RolesAllowed("GS")
    public Pnrs getPnrsToday(@QueryParam("date") String date) {

        List<Pnr> list = service.searchPnrsToday(date);
        Pnrs pnrs = new Pnrs();
        pnrs.setList(list);

        return pnrs;
    }
}
