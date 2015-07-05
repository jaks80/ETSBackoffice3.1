package com.ets.air.ws;

import com.ets.air.AIR;
import com.ets.air.AIRReader;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/air")
@Consumes("application/xml")
@Produces("application/xml")
public class AirWS {

    @Resource(name = "aIRReader")
    AIRReader reader;
    
    @POST
    @Path("/new")
    @PermitAll
    public Response newAir(AIR air){
        reader.setAir(air);
        reader.startReading();
        return Response.status(200).build();
    }    
}
