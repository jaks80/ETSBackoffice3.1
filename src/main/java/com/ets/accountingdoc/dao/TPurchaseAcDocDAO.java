package com.ets.accountingdoc.dao;

import com.ets.GenericDAO;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.client.domain.Agent;
import com.ets.client.domain.Customer;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yusuf
 */
public interface TPurchaseAcDocDAO extends GenericDAO<TicketingPurchaseAcDoc, Long> {

    public TicketingPurchaseAcDoc getWithChildrenById(Long id);

    public List<TicketingPurchaseAcDoc> getByPnrId(Long pnrId);

    public boolean voidDocument(TicketingPurchaseAcDoc doc);

    public TicketingPurchaseAcDoc getByTicketId(Long ticketId);

    public List<TicketingPurchaseAcDoc> getByGDSPnr(String GdsPnr);

    public List<Long> findOutstandingInvoiceReference(Enums.AcDocType acDocType, Long agentid, Date from, Date to);

    public TicketingPurchaseAcDoc findInvoiceByRef(Long... reference);

    public List<TicketingPurchaseAcDoc> findOutstandingInvoice(Enums.AcDocType type, Long agentid, Date dateStart, Date dateEnd);

    public List<TicketingPurchaseAcDoc> findOutstandingBSPInvoice(Long agentid, Date dateStart, Date dateEnd);

    public List<TicketingPurchaseAcDoc> find_ADM_ACM(Long agentid, Date from, Date to, Enums.AcDocType acDocType);

    public List<TicketingPurchaseAcDoc> findBSP_ADM_ACM(Long agentid, Date from, Date to);

    public List<TicketingPurchaseAcDoc> findInvoiceHistory(Long agentid, Date dateStart, Date dateEnd);

    public List<TicketingPurchaseAcDoc> findAllDocuments(Long agentid, Date dateStart, Date dateEnd);

    public BigDecimal getAccountBallanceToDate(Long agentid, Date dateEnd);

    public Map<String, BigDecimal> allAgentOutstandingReport(Date dateStart, Date dateEnd);
                   
    public List<Agent> outstandingAgents(Enums.AcDocType acDocType);    
}
