package com.ets.pnr.dao;

import com.ets.GenericDAOImpl;
import com.ets.pnr.domain.Itinerary;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("itineraryDAO")
@Transactional
public class ItineraryDAOImpl extends GenericDAOImpl<Itinerary, Long> implements ItineraryDAO {

    public List findSegments(Integer tktStatus,
            String[] airLineCode, Date from, Date to, String... ticketingAgtOid) {

        String airLineCodeQuery = "";
        String ticketingAgtOidQuery = "";

        if (airLineCode != null) {
            airLineCodeQuery = "p.airLineCode in (:airLineCode) and ";
        }
        if (ticketingAgtOid != null) {
            ticketingAgtOidQuery = "p.ticketingAgtOid in (:ticketingAgtOid) and ";
        }

        String hql = "from Itinerary as i, Ticket as t "
                + "left join fetch t.pnr as p "
                + "where i.pnr.id = t.pnr.id and "
                + airLineCodeQuery + ticketingAgtOidQuery
                + "t.docIssuedate >= :from and t.docIssuedate <= :to and "
                + "(:tktStatus is null or t.tktStatus = :tktStatus)";
               

        Query query = getSession().createQuery(hql);
        query.setParameter("tktStatus", tktStatus);
        query.setParameter("from", from);
        query.setParameter("to", to);

        if (airLineCode != null) {
            query.setParameterList("airLineCode", airLineCode);
        }

        if (ticketingAgtOid != null) {
            query.setParameterList("ticketingAgtOid", ticketingAgtOid);
        }
        List results = query.list();

        return results;
    }
}
