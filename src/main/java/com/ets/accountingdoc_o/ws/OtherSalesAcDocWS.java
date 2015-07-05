package com.ets.accountingdoc_o.ws;

import com.ets.accountingdoc_o.model.OtherSalesAcDocs;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc_o.model.InvoiceReportOther;
import com.ets.accountingdoc_o.model.OtherInvoiceModel;
import com.ets.accountingdoc_o.model.ServicesSaleReport;
import com.ets.accountingdoc_o.service.OSalesAcDocService;
import com.ets.productivity.model.ProductivityReport;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;
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

    @POST
    @Path("/new")
    @RolesAllowed("GS")
    public OtherSalesAcDoc create(OtherSalesAcDoc doc) {
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
    @Path("/byref/{refference}")
    @RolesAllowed("GS")
    public OtherSalesAcDocs getByRefNo(@PathParam("refference") Integer refference) {
        List<OtherSalesAcDoc> list = service.getByReffference(refference);
        OtherSalesAcDocs docs = new OtherSalesAcDocs();
        docs.setList(list);
        return docs;
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

        InvoiceReportOther report = service.dueInvoiceReport(doctype,
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
        ProductivityReport report = service.agentOutstandingReport(dateFrom,dateTo);
        return report;
    }
}
