package com.ets.accounts.ws;

import com.ets.accounts.model.AccountsReport;
import com.ets.accounts.service.AccountsService;
import com.ets.util.DateUtil;
import com.ets.util.Enums;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("/accounts-management")
@Consumes("application/xml")
@Produces("application/xml")
public class AccountsWS {
 
    @Autowired
    AccountsService service;
    
    @GET
    @Path("/saccounts_history")
    @RolesAllowed("SM")
    public AccountsReport salesAccountsHistory(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        
        AccountsReport report = service.generateClientStatement(clienttype, clientid, dateFrom, dateTo);

        return report;
    }
    
    @GET
    @Path("/paccounts_history")
    @RolesAllowed("SM")
    public AccountsReport purchaseAccountsHistory(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        
        AccountsReport report = service.generateVendorStatement(clientid, dateFrom, dateTo);

        return report;
    }
    
    @GET
    @Path("/oaccounts_history")
    @RolesAllowed("SM")
    public AccountsReport otherAccountsHistory(
            @QueryParam("clienttype") Enums.ClientType clienttype,
            @QueryParam("clientid") Long clientid,
            @QueryParam("dateStart") String dateStart,
            @QueryParam("dateEnd") String dateEnd) {

        Date dateFrom = DateUtil.stringToDate(dateStart, "ddMMMyyyy");
        Date dateTo = DateUtil.stringToDate(dateEnd, "ddMMMyyyy");
        
        AccountsReport report = service.generateClientStatementOther(clienttype, clientid, dateFrom, dateTo);

        return report;
    }
}
