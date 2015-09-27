package com.ets.accountingdoc.ws;

import com.ets.accountingdoc.collection.TicketingSalesAcDocs;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accountingdoc.model.InvoiceModel;
import com.ets.accountingdoc.model.InvoiceReport;
import com.ets.accountingdoc.service.TPurchaseAcDocService;
import com.ets.accountingdoc.service.TSalesAcDocService;
import com.ets.client.collection.Agents;
import com.ets.client.collection.Customers;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.productivity.model.ProductivityReport;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/tsacdoc-management")
@Consumes("application/xml")
@Produces("application/xml")
public class TicketingSalesAcDocWS {

    @Autowired
    TSalesAcDocService service;
    @Autowired
    TPurchaseAcDocService purchase_service;

    @GET
    @Path("/byid/{id}")
    @RolesAllowed("GS")
    public TicketingSalesAcDoc getbyId(@PathParam("id") long id) {
        TicketingSalesAcDoc doc = service.getWithChildrenById(id);
        return doc;
    }

    @GET
    @Path("/byref/{refference}")
    @RolesAllowed("GS")
    public TicketingSalesAcDocs getByRefNo(@PathParam("refference") Integer refference) {
        List<TicketingSalesAcDoc> list = service.getByReffference(refference);
        TicketingSalesAcDocs docs = new TicketingSalesAcDocs();
        docs.setList(list);
        return docs;
    }

    @GET
    @Path("/bypnr/{pnr}")
    @RolesAllowed("GS")
    public TicketingSalesAcDocs getByGDSPnr(@PathParam("pnr") String pnr) {
        List<TicketingSalesAcDoc> list = service.getByGDSPnr(pnr);
        TicketingSalesAcDocs docs = new TicketingSalesAcDocs();
        docs.setList(list);
        return docs;
    }

    @GET
    @Path("/bypnrid")
    @RolesAllowed("GS")
    public TicketingSalesAcDocs getByPnrId(@QueryParam("pnrId") Long pnrId) {
        List<TicketingSalesAcDoc> list = service.getByPnrId(pnrId);
        TicketingSalesAcDocs docs = new TicketingSalesAcDocs();
        docs.setList(list);
        return docs;
    }

    @GET
    @Path("/draftdoc")
    @RolesAllowed("GS")
    public TicketingSalesAcDoc newDraftDocument(@QueryParam("pnrid") Long pnrid) {
        //TicketingSalesAcDoc doc = service.newDraftDocument(pnrid);
        TicketingSalesAcDoc doc = service.newDraftDocumentUpdate(pnrid);
        return doc;
    }

    @POST
    @Path("/newdoc")
    @RolesAllowed("GS")
    public TicketingSalesAcDoc createNewDocument(TicketingSalesAcDoc ticketingSalesAcDoc) {
        service.newDocument(ticketingSalesAcDoc);
        TicketingSalesAcDoc doc = service.getWithChildrenById(ticketingSalesAcDoc.getId());

        return doc;
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("SM")
    public Response delete(@PathParam("id") long id) {

        int status = service.delete(id);
        if (status == 1) {
            return Response.status(200).build();
        } else {
            return Response.status(500).build();
        }
    }

    @PUT
    @Path("/void")
    @RolesAllowed("SM")
    public TicketingSalesAcDoc _void(TicketingSalesAcDoc ticketingSalesAcDoc) {
        ticketingSalesAcDoc = service._void(ticketingSalesAcDoc);
        return ticketingSalesAcDoc;
    }

    @GET
    @Path("/due_invoices")
    @RolesAllowed("SM")
    public TicketingSalesAcDocs outstandingInvoices(
            @QueryParam("doctype") Enums.AcDocType doctype,
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        List<TicketingSalesAcDoc> list = service.dueInvoices(doctype,
                clienttype, clientid, dateFrom, dateTo);

        TicketingSalesAcDocs docs = new TicketingSalesAcDocs();
        docs.setList(list);
        return docs;
    }

    //*****************Reporting Methods are Bellow******************//
    /**
     * This web service is to print invoice.
     *
     * @param id
     * @return
     */
    @GET
    @Path("/model/byid/{id}")
    @RolesAllowed("GS")
    public InvoiceModel getModelbyId(@PathParam("id") long id) {
        InvoiceModel model = service.getModelbyId(id);
        return model;
    }

    @GET
    @Path("/byref/{refferences}")
    @RolesAllowed("GS")   
    public InvoiceReport getReportByReferenceNumber(@PathParam("refferences") String refferences) {
        String[] ids_str = refferences.split(",");
        Long[] ids_long = new Long[ids_str.length];
        for (int i = 0; i < ids_str.length; i++) {
            ids_long[i] = Long.valueOf(ids_str[i]);
        }

        InvoiceReport report = service.findInvoiceSummeryByReference(ids_long);
        return report;
    }

    @GET
    @Path("/acdoc_report")
    @RolesAllowed("SM")   
    public InvoiceReport outstandingDocumentReport(
            @QueryParam("doctype") Enums.AcDocType doctype,
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = null;
        Date dateTo = null;

        if (dateStart != null && dateEnd != null) {
            dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
            dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        }

        InvoiceReport report = service.dueInvoiceReportSQL(doctype, clienttype, clientid, dateFrom, dateTo);
        return report;
    }
    
    @GET
    @Path("/paymentdue_flight")
    @RolesAllowed("GS")
    public InvoiceReport outstandingFlightReport(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = null;
        Date dateTo = null;

        if (dateStart != null && dateEnd != null) {
            dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
            dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        }

        InvoiceReport report = service.outstandingFlightReport(clienttype, clientid, dateFrom, dateTo);
        return report;
    }

    @GET
    @Path("/acdoc_history")
    @RolesAllowed("GS")
    public InvoiceReport documentHistoryReport(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        InvoiceReport report = service.invoiceHistoryReport(clienttype,
                clientid, dateFrom, dateTo);

        return report;
    }

    @GET
    @Path("/user_productivity")
    @RolesAllowed("AD")
    public ProductivityReport userProducivityReport(
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        ProductivityReport report = service.userProductivityReport(dateFrom, dateTo);

        return report;
    }

    @GET
    @Path("/allagentduereport")
    @RolesAllowed("AD")
    public ProductivityReport allAgentDueReport(
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        ProductivityReport report = service.allAgentDueReport(dateFrom, dateTo);
        return report;
    }

    @GET
    @Path("/dueagents")
    @RolesAllowed("SM")
    @PermitAll
    public Agents outstandingAgents(@QueryParam("doctype") Enums.AcDocType doctype) {

        List<Agent> agent_list = service.outstandingAgents(doctype);
        Agents agents = new Agents();
        agents.setList(agent_list);
        return agents;
    }

    @GET
    @Path("/duecustomers")
    @RolesAllowed("SM")
    @PermitAll
    public Customers outstandingCusotmers(@QueryParam("doctype") Enums.AcDocType doctype) {

        List<Customer> customer_list = service.outstandingCusotmers(doctype);
        Customers customers = new Customers();
        customers.setList(customer_list);
        return customers;
    }
}
