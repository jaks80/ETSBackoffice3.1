package com.ets.accountingdoc.dao;

import com.ets.GenericDAO;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yusuf
 */
public interface TSalesAcDocDAO extends GenericDAO<TicketingSalesAcDoc, Long> {

    public Long getNewAcDocRef();

    public TicketingSalesAcDoc getWithChildrenById(Long id);

    public TicketingSalesAcDoc getByTicketId(Long ticketId);

    public TicketingSalesAcDoc getByTicket(Long pnrId, Long ticketNo);

    public List<TicketingSalesAcDoc> findAllById(Long... id);
    
    public List<TicketingSalesAcDoc> findInvoiceByRef(Long... references);

    public TicketingSalesAcDoc findInvoiceByPaxName(String surName, Enums.TicketStatus ticketStatus, Long pnrId);

    public List<TicketingSalesAcDoc> getByPnrId(Long pnrId);

    public List<TicketingSalesAcDoc> getByGDSPnr(String GdsPnr);

    public TicketingSalesAcDoc voidSimpleDocument(TicketingSalesAcDoc doc);

    public TicketingSalesAcDoc voidTicketedDocument(TicketingSalesAcDoc doc);

    public List<String> outstandingAgentsName();

    public List<String> outstandingCusotmersName();

    /**
     * @deprecated 
     * @param acDocType
     * @return 
     */
    public List<Agent> outstandingAgents(Enums.AcDocType acDocType);

    public List<Agent> outstandingAgentsSQL(Enums.AcDocType acDocType);
    
    /**
     * @deprecated 
     * @param acDocType
     * @return 
     */
    public List<Customer> outstandingCusotmers(Enums.AcDocType acDocType);
    
    public List<Customer> outstandingCustomersSQL(Enums.AcDocType acDocType);

    public List<TicketingSalesAcDoc> findActiveUnDueInvoices(Enums.ClientType clienttype, Long clientid, Date from, Date to);

    public List<TicketingSalesAcDoc> findArchivedInvoices(Enums.ClientType clienttype, Long clientid, Date from, Date to);

    public List<TicketingSalesAcDoc> findOutstandingDocuments(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd);

    public List<TicketingSalesAcDoc> outstandingFlightReport(Enums.ClientType clienttype, Long clientid, Date dateFrom, Date dateEnd);

    public List<TicketingSalesAcDoc> findInvoiceHistory(Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd);

    public List<TicketingSalesAcDoc> findAllDocuments(Enums.ClientType clienttype, Long clientid, Date dateStart, Date dateEnd);

    public BigDecimal getAccountBallanceToDate(Enums.ClientType clienttype, Long clientid, Date dateEnd);

    public Map<User, BigDecimal> userProductivityReport(Date dateStart, Date dateEnd);

    public Map<String, BigDecimal> allAgentOutstandingReport(Date dateStart, Date dateEnd);
}
