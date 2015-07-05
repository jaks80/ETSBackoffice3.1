package com.ets.air.dao;

import com.ets.GenericDAOImpl;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("airDAO")
@Transactional
public class AirDAOImpl extends GenericDAOImpl<Pnr, Long> implements AirDAO {

    public AirDAOImpl() {

    }

    public Pnr findPnr(String gdsPnr, Date pnrCreationDate) {

        String hql = "select distinct p from Pnr p "
                + "left join fetch p.tickets as t "
                + "left join fetch p.segments "                
                + "where p.gdsPnr = :gdsPnr and p.pnrCreationDate = :pnrCreationDate";

        Query query = getSession().createQuery(hql);
        query.setParameter("gdsPnr", gdsPnr);
        query.setParameter("pnrCreationDate", pnrCreationDate);

        Pnr pnr = null;
        List l = query.list();
        if(l.size() > 0){
         pnr = (Pnr) l.get(0);
        }

        return pnr;
    }

    @Override
    public Pnr findPnr(String ticketNo, String surName) {

        String hql = "from Ticket as t "
                + "left join fetch t.pnr as p "
                + "left join fetch p.tickets as t "
                + "where t.ticketNo = :ticketNo and t.surName = :surName ";

        Query query = getSession().createQuery(hql);
        query.setParameter("ticketNo", ticketNo);
        query.setParameter("surName", surName);

        Ticket ticket = null;
        List l = query.list();
        if (l.size() > 0) {
            ticket = (Ticket) l.get(0);
        }
        if (ticket != null) {
            return ticket.getPnr();
        } else {
            return null;
        }
    }
}
