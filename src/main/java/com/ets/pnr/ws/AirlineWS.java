package com.ets.pnr.ws;

import com.ets.pnr.model.collection.Airlines;
import com.ets.pnr.domain.Airline;
import com.ets.pnr.service.AirlineService;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/airline-management")
@Consumes("application/xml")
@Produces("application/xml")
public class AirlineWS {

    @Autowired
    AirlineService service;

    @GET
    @Path("/airlines/{code}")    
    @RolesAllowed("AD")
    public Airline find(@PathParam("code") String code) {
        return service.find(code);
    }
    
    @GET
    @Path("/airlines/match/{name}")    
    @RolesAllowed("AD")
    public Airlines match(@QueryParam("name") String name) {
        return service.match(name);
    }
    
    @POST
    @Path("/airlines/save")    
    @RolesAllowed("AD")
    public void save(Airline airline) {        
        service.save(airline);
    }

    @POST
    @Path("/airlines/save-all")
    @RolesAllowed("AD")
    public void saveBulk(Airlines airlines) {        
        service.saveBulk(airlines);
    }
}
