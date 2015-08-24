package com.ets.client.ws;

import com.ets.client.collection.Agents;
import com.ets.client.domain.Agent;
import com.ets.client.service.AgentService;
import java.util.List;
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
@Path("/agent-management")
@Consumes("application/xml")
@Produces("application/xml")
public class AgentWS {

    @Autowired
    AgentService service;

    @GET
    @Path("/agents")
    @RolesAllowed("SM")
    public Agents find(@QueryParam("name") String name,
            @QueryParam("postCode") String postCode,
            @QueryParam("officeID") String officeID) {

        List<Agent> list = service.findAll(name, postCode, officeID);
        Agents agents = new Agents();
        agents.setList(list);
        return agents;
    }

    @GET
    @Path("/agents/ks")//Keword Search
    @RolesAllowed("SM")
    public Agents find(@QueryParam("keyword") String keyword) {

        List<Agent> list = service.querySearch(keyword);
        Agents agents = new Agents();
        agents.setList(list);
        return agents;
    }

    @GET
    @Path("/agentswithemail")
    @RolesAllowed("SM")
    public Agents find() {

        List<Agent> list = service.findAgentContainsEmail();
        Agents agents = new Agents();
        agents.setList(list);
        return agents;
    }

    @GET
    @Path("/ticketingagents")
    @RolesAllowed("GS")
    public Agents findTicketingAgents() {

        List<Agent> list = service.findTicketingAgents();
        Agents agents = new Agents();
        agents.setList(list);
        return agents;
    }

    @GET
    @Path("/agents/kw")
    @RolesAllowed("GS")
    public Agents findByQuery(@QueryParam("keyword") String keyword) {

        List<Agent> list = service.findByKeyword(null, keyword);
        if (list.isEmpty()) {
            list = service.findByKeyword(keyword, null);
        }

        Agents agents = new Agents();
        agents.setList(list);
        return agents;
    }

    @POST
    @Path("/new")
    @RolesAllowed("SM")
    public Agent create(Agent agent) {
        return service.saveorUpdate(agent);
    }

    @PUT
    @Path("/update")
    @RolesAllowed("SM")
    public Agent update(Agent agent) {
        return service.saveorUpdate(agent);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("AD")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.status(200).build();
    }
}
