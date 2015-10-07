/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Yusuf
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.ets.accountingdoc.ws.TicketingPurchaseAcDocWS.class);
        resources.add(com.ets.accountingdoc.ws.TicketingSalesAcDocWS.class);
        resources.add(com.ets.accountingdoc_o.ws.OtherSalesAcDocWS.class);
        resources.add(com.ets.accounts.ws.AccountsWS.class);
        resources.add(com.ets.accounts.ws.PaymentWS.class);
        resources.add(com.ets.air.ws.AirWS.class);
        resources.add(com.ets.client.ws.AgentWS.class);
        resources.add(com.ets.client.ws.CustomerWS.class);
        resources.add(com.ets.otherservice.ws.AdditionalChargeWS.class);
        resources.add(com.ets.otherservice.ws.CategoryWS.class);
        resources.add(com.ets.otherservice.ws.OtherServiceWS.class);
        resources.add(com.ets.pnr.ws.AirlineWS.class);
        resources.add(com.ets.pnr.ws.ItineraryWS.class);
        resources.add(com.ets.pnr.ws.PnrWS.class);
        resources.add(com.ets.pnr.ws.RemarkWS.class);
        resources.add(com.ets.pnr.ws.TicketWS.class);
        resources.add(com.ets.security.SecurityInterceptor.class);
        resources.add(com.ets.settings.ws.AppSettingsWS.class);
        resources.add(com.ets.settings.ws.UserWS.class);
        resources.add(com.ets.usertask.ws.UserTaskWS.class);
    }
    
}
