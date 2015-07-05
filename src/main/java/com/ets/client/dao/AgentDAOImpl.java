package com.ets.client.dao;

import com.ets.GenericDAOImpl;
import com.ets.client.domain.Agent;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("agentDAO")
@Transactional
public class AgentDAOImpl extends GenericDAOImpl<Agent, Long> implements AgentDAO {

    @Override
    public List<Agent> findByOfficeID(String officeID) {

        String hql = "from Agent agt "
                + "where agt.officeID=:officeID and agt.active = true";

        Query query = getSession().createQuery(hql);
        query.setParameter("officeID", officeID);

        List<Agent> agents = query.list();
        return agents;
    }

    @Override
    public List<Agent> findByLike(String name, String postCode, String officeID) {
        name = nullToEmptyValue(name).concat("%");
        postCode = nullToEmptyValue(postCode).concat("%");
        officeID = nullToEmptyValue(officeID).concat("%");

        if (officeID.length() > 2) {
            officeID = "%".concat(officeID);
        }

        String hql = "from Agent a "
                + "where "
                + "a.name like :name and "
                + "a.postCode like :postCode and "
                + "(a.officeID is null or a.officeID like :officeID) ";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("postCode", postCode);
        query.setParameter("officeID", officeID);

        return query.list();
    }

    @Override
    public List<Agent> findByKeyword(String name, String officeID) {
        name = nullToEmptyValue(name).concat("%");       
        officeID = nullToEmptyValue(officeID).concat("%");

        if (officeID.length() > 2) {
            officeID = "%".concat(officeID);
        }

        String hql = "from Agent agt "
                + "where "
                + "agt.name like :name and "                
                + "(agt.officeID is null or agt.officeID like :officeID and agt.active = true) ";
        Query query = getSession().createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("officeID", officeID);

        return query.list();
    }

    @Override
    public List<Agent> findTicketingAgents() {
        String hql = "select distinct agt from Agent as agt, Pnr as p "
                + "where p.ticketingAgtOid = agt.officeID and agt.active = true order by agt.name ";

        Query query = getSession().createQuery(hql);
        return query.list();
    }
}
