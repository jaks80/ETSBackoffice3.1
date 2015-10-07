package com.ets.usertask.ws;

import com.ets.usertask.service.UserTaskService;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf Akhond
 */
@Controller
@Path("/usertask-management")
@Consumes("application/xml")
@Produces("application/xml")
public class UserTaskWS {
    
    @Autowired
    UserTaskService service;
}
