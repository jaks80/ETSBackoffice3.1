package com.ets.otherservice.ws;

import com.ets.otherservice.domain.OtherService;
import com.ets.otherservice.domain.OtherServices;
import com.ets.otherservice.service.OtherServiceService;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/otherservice-management")
@Consumes("application/xml")
@Produces("application/xml")
public class OtherServiceWS {

    @Autowired
    OtherServiceService service;

    @GET
    @Path("/otherservices")
    @RolesAllowed("GS")    
    public OtherServices find() {

        List<OtherService> list = service.findAll();
        OtherServices otherService = new OtherServices();
        otherService.setList(list);
        return otherService;
    }

    @GET
    @Path("/otherservices/bycategory")
    @RolesAllowed("GS")
    public OtherServices findByCategory(@QueryParam("categoryid") Long categoryid) {

        List<OtherService> list = service.findItemsByCategory(categoryid);
        OtherServices otherService = new OtherServices();
        otherService.setList(list);
        return otherService;
    }
    
    @GET
    @Path("/otherservices/bykeyword")
    @RolesAllowed("GS")
    public OtherServices findByKeyword(@QueryParam("keyword") String keyword) {

        List<OtherService> list = service.findItemsByKeyword(keyword);
        OtherServices otherService = new OtherServices();
        otherService.setList(list);
        return otherService;
    }
    
    @POST
    @Path("/new")
    @RolesAllowed("SM")
    public OtherService create(OtherService otherService) {
        return service.saveorUpdate(otherService);
    }

    @PUT
    @Path("/update")
    @RolesAllowed("SM")
    public OtherService update(OtherService otherService) {
        return service.saveorUpdate(otherService);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("AD")
    public Response delete(@PathParam("id") long id) {
        return Response.status(200).build();
    }
}
