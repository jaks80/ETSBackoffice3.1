package com.ets.accountingdoc_o.ws;

import com.ets.accountingdoc_o.model.*;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc_o.service.OSalesAcDocService;
import com.ets.client.collection.Agents;
import com.ets.client.collection.Customers;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.exception.ClientNotFoundException;
import com.ets.productivity.model.ProductivityReport;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.math.BigDecimal;
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
@Path("/osacdoc-management")
@Consumes("application/xml")
@Produces("application/xml")
public class OtherSalesAcDocWS {

    @Autowired
    OSalesAcDocService service;

    @GET
    @Path("/fixdb")
    @PermitAll
    public void updatePnrSegmentAndLeadPax(@QueryParam("dateStart") String dateStart, @QueryParam("dateEnd") String dateEnd) {
        service.fixDB();
    }

    @POST
    @Path("/new")
    @RolesAllowed("GS")
    public OtherSalesAcDoc create(OtherSalesAcDoc doc) throws ClientNotFoundException {
        return service.newDocument(doc);
    }

    @GET
    @Path("/byid/{id}")
    @RolesAllowed("GS")
    public OtherSalesAcDoc getbyId(@PathParam("id") long id) {
        OtherSalesAcDoc doc = service.getWithChildrenById(id);
        return doc;
    }

    @GET
    @Path("/byref/{refferences}")
    @RolesAllowed("GS")

    public InvoiceReportOther getReportByReferenceNumber(@PathParam("refferences") String refferences) {
        String[] ids_str = refferences.split(",");
        Long[] ids_long = new Long[ids_str.length];
        for (int i = 0; i < ids_str.length; i++) {
            ids_long[i] = Long.valueOf(ids_str[i]);
        }

        InvoiceReportOther report = service.findInvoiceSummeryByReference(ids_long);
        return report;
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("SM")
    public Response delete(@PathParam("id") long id) {
        boolean success = service.delete(id);
        if (success) {
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/deleteline/{id}")
    @RolesAllowed("SM")
    public Response deleteLine(@PathParam("id") long id, @QueryParam("userid") Long userid) {

        boolean success = service.deleteLine(id, userid);
        if (success) {
            return Response.status(200).build();
        } else {
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/void")
    @RolesAllowed("SM")
    public OtherSalesAcDoc _void(OtherSalesAcDoc doc) {
        doc = service._void(doc);
        return doc;
    }

    @GET
    @Path("/due_invoices")
    @RolesAllowed("SM")
    public OtherSalesAcDocs outstandingInvoices(
            @QueryParam("doctype") Enums.AcDocType doctype,
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        List<OtherSalesAcDoc> list = service.dueInvoices(doctype,
                clienttype, clientid, dateFrom, dateTo);

        OtherSalesAcDocs docs = new OtherSalesAcDocs();
        docs.setList(list);
        return docs;
    }

    //*****************Reporting Methods are Bellow******************//
    @GET
    @Path("/model/byid/{id}")
    @RolesAllowed("GS")
    public OtherInvoiceModel getModelbyId(@PathParam("id") long id) {
        OtherInvoiceModel model = service.getModelbyId(id);
        return model;
    }

    @GET
    @Path("/acdoc_report")
    @RolesAllowed("SM")
    public InvoiceReportOther outstandingDocumentReport(
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

        InvoiceReportOther report = service.dueInvoiceReportSQL(doctype,
                clienttype, clientid, dateFrom, dateTo);

        return report;
    }

    @GET
    @Path("/acdoc_history")
    @RolesAllowed("SM")
    public InvoiceReportOther documentHistoryReport(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        InvoiceReportOther report = service.invoiceHistoryReport(clienttype,
                clientid, dateFrom, dateTo);

        return report;
    }

    @GET
    @Path("/services_salereport")
    @RolesAllowed("SM")
    public ServicesSaleReport servicesSaleReport(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd,
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("itemId") Long itemId) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");

        ServicesSaleReport report = service.servicesSaleReport(dateFrom, dateTo, categoryId, itemId, clienttype, clientid);

        return report;
    }

    @GET
    @Path("/user_productivity")
    @RolesAllowed("SM")
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
    public ProductivityReport agentDueReport(
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {
        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        ProductivityReport report = service.agentOutstandingReport(dateFrom, dateTo);
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

    @GET
    @Path("/quickbalance")
    @RolesAllowed("GS")
    public String accountQuickBalance(@QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid) {

        BigDecimal balance = service.accountQuickBalance(clienttype, clientid);
        return balance.toString();
    }

}
