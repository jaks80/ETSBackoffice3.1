package com.ets.client.service;

import com.ets.client.dao.AgentDAO;
import com.ets.client.dao.MainAgentDAO;
import com.ets.client.domain.Agent;
import com.ets.client.domain.MainAgent;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("agentService")
public class AgentService {

    @Resource(name = "agentDAO")
    private AgentDAO dao;
    @Resource(name = "mainAgentDAO")
    private MainAgentDAO mainAgentDAO;

    public MainAgent getMainAgent() {
        return mainAgentDAO.findByID(MainAgent.class, Long.parseLong("1"));
    }

    public Agent getAgent(Long id) {
        return dao.findByID(Agent.class, id);
    }

    public List<Agent> querySearch(String keyword) {
     return dao.querySearch(keyword);
    }
    
    public Agent findByOfficeID(String officeID) {
        List<Agent> agents = dao.findByOfficeID(officeID);
        if (!agents.isEmpty()) {
            return agents.get(0);
        } else {
            return null;
        }
    }

    public List<Agent> findListByOfficeID(String officeID) {
        List<Agent> agents = dao.findByOfficeID(officeID);
        return agents;
    }

    public Agent findTktingAgentByOfficeID(String officeID) {

        List<Agent> tkting_agents = dao.findTicketingAgents();
        Agent agent = null;
        for (Agent ta : tkting_agents) {
            if (ta.getOfficeID().equals(officeID)) {
                agent = ta;
                return agent;
            }
        }
        return agent;
    }

    public List<Agent> findAll() {
        return dao.findAll(Agent.class);
    }

    public List<Agent> findTicketingAgents() {
        return dao.findTicketingAgents();
    }

    public List<Agent> findAll(String name, String pCode, String officeID) {
        return dao.findByLike(name, pCode, officeID);
    }

    public List<Agent> findByKeyword(String name, String officeID) {
        return dao.findByKeyword(name, officeID);
    }

    
    public MainAgent saveorUpdate(MainAgent agent) {
        MainAgent in_db = getMainAgent();
        agent.setCreatedBy(in_db.getCreatedBy());
        //agent.setId(Long.parseLong("1"));
        mainAgentDAO.save(agent);
        return agent;
    }

    public Agent saveorUpdate(Agent agent) {
        dao.save(agent);
        return agent;
    }

    public void delete(Long id) {
        Agent agent = dao.findByID(Agent.class, id);
        dao.delete(agent);
    }
    
    public List<Agent> findAgentContainsEmail() {
     return dao.findAgentContainsEmail();
    }
}
