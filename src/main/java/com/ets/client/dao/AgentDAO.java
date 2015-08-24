package com.ets.client.dao;

import com.ets.GenericDAO;
import com.ets.client.domain.Agent;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface AgentDAO extends GenericDAO<Agent, Long>{
    
    public List<Agent> findByOfficeID(String officeID);
       
    public List<Agent> findByLike(String name, String pCode, String officeID);   
    
    public List<Agent> querySearch(String keyword);
    
    public List<Agent> findByKeyword(String name, String officeID);                   

    public List<Agent> findTicketingAgents();       
    
    public List<Agent> findAgentContainsEmail();       
}
