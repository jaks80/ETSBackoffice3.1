package com.ets.settings.ws;

import com.ets.client.domain.MainAgent;
import com.ets.settings.domain.AppSettings;
import com.ets.settings.service.AppSettingsService;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/appsettings-management")
@Consumes("application/xml")
@Produces("application/xml")
public class AppSettingsWS {

    @Autowired
    AppSettingsService service;

    @GET
    @GZIP
    @Path("/mainagent")        
    @RolesAllowed("GS")
    public MainAgent getMainAgent(){        
     return service.getMainAgent();
    }
    
    @PUT
    @GZIP
    @Path("/updatemainagent")
    @RolesAllowed("AD")
    public MainAgent update(MainAgent agent) {
        return service.saveorUpdateMainAgent(agent);
    }
    
    
    @GET
    @GZIP
    @Path("/appsettings")    
    @RolesAllowed("GS")
    @RequestMapping(value = "/appsettings", method=RequestMethod.GET)
    public AppSettings find() {

       AppSettings appSettings = service.getSettings();
        return appSettings;
    }

    
    @PUT
    @GZIP
    @Path("/update")
    @RolesAllowed("AD")
    public AppSettings update(AppSettings appSettings) {
        return service.saveorUpdate(appSettings);
    }
}
