package com.ets.pnr.ws;

import com.ets.pnr.domain.Remark;
import com.ets.pnr.model.collection.Remarks;
import com.ets.pnr.service.RemarkService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/remark-management")
@Consumes("application/xml")
@Produces("application/xml")
public class RemarkWS {

    @Autowired
    private RemarkService service;

    @POST
    @Path("/new")
    @RolesAllowed("GS")
    public Remarks create(Remarks remarks) {

        if (remarks.getList().size() == 1) {
            Remark remark = remarks.getList().get(0);
            service.save(remark);
        } else if (remarks.getList().size() > 1) {
            List<Remark> list = remarks.getList();
            service.saveBulk(list);
            remarks.setList(list);
        }

        return remarks;
    }

    @GET
    @Path("/bypnrid")
    @Consumes("application/xml")
    @Produces("application/xml")
    @RolesAllowed("GS")
    public Remarks getByPnrId(@QueryParam("pnrId") Long pnrId) {
        List<Remark> list = service.getByPnrId(pnrId);
        Remarks remarks = new Remarks();
        remarks.setList(list);
        return remarks;
    }
}
