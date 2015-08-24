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
    
    public List<OtherSalesAcDoc> findOutstandingDocuments(Enums.AcDocType type,Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);      
    
    public List<OtherSalesAcDoc> findInvoiceHistory(Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);
    
    public List<OtherSalesAcDoc> findAllDocuments(Enums.ClientType clienttype,Long clientid,Date dateStart,Date dateEnd);
        
    public BigDecimal getAccountBallanceToDate(Enums.ClientType clienttype,Long clientid,Date dateEnd);
    
    public Map<User,BigDecimal> userProductivityReport(Date dateStart,Date dateEnd);
    
    public Map<String,BigDecimal> allAgentOutstandingReport(Date dateStart,Date dateEnd);
    
    public List<Agent> outstandingAgents(Enums.AcDocType acDocType);
    
    public List<Customer> outstandingCusotmers(Enums.AcDocType acDocType);

}
