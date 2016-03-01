package com.ets.accountingdoc_o.dao;

import com.ets.GenericDAO;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
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
public interface OtherSalesAcDocDAO extends GenericDAO<OtherSalesAcDoc, Long>{
    
    public Long getNewAcDocRef();  
    
    public OtherSalesAcDoc getWithChildrenById(Long id);
    
    public List<OtherSalesAcDoc> findAllById(Long... id);
    
    public boolean voidDocument(OtherSalesAcDoc doc);
    
    public List<OtherSalesAcDoc> findInvoiceByRef(Long... reference);
    
    public List findOutstandingDocumentsSQL(Enums.AcDocType type, Enums.ClientType clienttype,
            Long clientid, Date from, Date to);
    
    /**
     * @deprecated 
     * @param type
     * @param clienttype
     * @param clientid
     * @param dateStart
     * @param dateEnd
     * @return 
     */
    public List<OtherSalesAcDoc> findOutstandingDocuments(Enums.AcDocType type,Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);      
    
    public List<OtherSalesAcDoc> findInvoiceHistory(Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);
    
    public List<OtherSalesAcDoc> findAllDocuments(Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);
        
    public int update(int itemQuantity, String category, long id);
    
    public List<OtherSalesAcDoc> findAllDocuments();
    
    public BigDecimal getAccountBallanceToDate(Enums.ClientType clienttype,Long clientid,Date dateEnd);
    
    public Map<User,BigDecimal> userProductivityReport(Date dateStart,Date dateEnd);
    
    public Map<String,BigDecimal> allAgentOutstandingReport(Date dateStart,Date dateEnd);
    
    /**
     * @deprecated 
     * @param acDocType
     * @return 
     */
    public List<Agent> outstandingAgents(Enums.AcDocType acDocType);
    
    /**
     * @deprecated 
     * @param acDocType
     * @return 
     */
    public List<Customer> outstandingCusotmers(Enums.AcDocType acDocType);

    public List<Agent> outstandingAgentsSQL(Enums.AcDocType acDocType);
    
    public List<Customer> outstandingCustomersSQL(Enums.AcDocType acDocType);
    
    public BigDecimal accountQuickBalance(Enums.ClientType clienttype,Long clientid);
}
