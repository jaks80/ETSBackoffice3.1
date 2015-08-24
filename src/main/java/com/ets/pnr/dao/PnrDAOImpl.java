package com.ets.pnr.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("pnrDAO")
@Transactional
public class PnrDAOImpl extends GenericDAOImpl<Pnr, Long> implements PnrDAO {

    @Override
    @Transactional(readOnly = true)
    public Pnr getByIdWithChildren(Long id) {

        String hql = "select distinct p from Pnr as p "
                + "left join fetch p.tickets as t "
                + "left join fetch p.segments "
                + "left join fetch p.remarks "
                + "left join fetch p.agent "
                + "left join fetch p.customer "
                + "left join fetch p.createdBy "
                + "left join fetch p.lastModifiedBy "
                + "left join fetch p.ticketing_agent "
                + "left join fetch t.ticketingSalesAcDoc "
                + "left join fetch t.ticketingPurchaseAcDoc "
                + "where p.id = :id";

        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);
        Pnr pnr = (Pnr) query.uniqueResult();

        if (pnr == null) {
            return null;
        }
        for (Ticket t : pnr.getTickets()) {

            TicketingSalesAcDoc sdoc = t.getTicketingSalesAcDoc();
            if (sdoc != null) {
                sdoc.setAdditionalChargeLines(null);
                sdoc.setTickets(null);
                sdoc.setPnr(null);
                sdoc.setRelatedDocuments(null);
            }
            TicketingPurchaseAcDoc pdoc = t.getTicketingPurchaseAcDoc();
            if (pdoc != null) {
                pdoc.setAdditionalChargeLines(null);
                pdoc.setTickets(null);
                pdoc.setPnr(null);
                pdoc.setRelatedDocuments(null);
            }
        }
        return pnr;
    }

    @Override
    public List<Pnr> bookedPnrs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Pnr> find(Date from, Date to, String[] ticketingAgtOid, String[] bookingAgtOid) {

        String bookingAgtOidQuery = "";
        String ticketingAgtOidQuery = "";

        if (bookingAgtOid != null) {
            bookingAgtOidQuery = "p.bookingAgtOid in (:bookingAgtOid) and ";
        }
        if (ticketingAgtOid != null) {
            ticketingAgtOidQuery = "p.ticketingAgtOid in (:ticketingAgtOid) and ";
        }

        String hql = "select distinct p from Pnr p "
                + "left join fetch p.tickets as t "
                + "left join fetch p.segments as s "
                + "left join fetch p.remarks as r "
                + "where "
                + bookingAgtOidQuery + ticketingAgtOidQuery
                + "(p.airCreationDate >= :from) and (p.airCreationDate <= :to)";

        Query query = getSession().createQuery(hql);
        query.setParameter("from", from);
        query.setParameter("to", to);

        if (bookingAgtOid != null) {
            query.setParameterList("bookingAgtOid", bookingAgtOid);
        }

        if (ticketingAgtOid != null) {
            query.setParameterList("ticketingAgtOid", ticketingAgtOid);
        }

        return query.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> searchUninvoicedPnr() {
        List<Pnr> list = new ArrayList<>();

//        String hql = "select t,p from Ticket t "
//                + "inner join t.pnr as p "
//                + "where t.ticketingSalesAcDoc is null group by t.pnr.id";
        String hql = "select distinct p from Pnr p left join fetch p.tickets as t where t.ticketingSalesAcDoc is null group by p.id";

        Query query = getSession().createQuery(hql);
        list = query.list();

//        Iterator it = results.iterator();
//
//        while (it.hasNext()) {
//            Object[] objects = (Object[]) it.next();
//            Ticket leadPaxTicket = (Ticket) objects[0];
//            Pnr pnr = (Pnr) objects[1];
//            pnr.setTickets(null);//Avoid lazy loading here. Create a new hashset and set
//            pnr.setSegments(null);
//            pnr.setRemarks(null);
//            Set<Ticket> tickets = new LinkedHashSet<>();
//            tickets.add(leadPaxTicket);
//            pnr.setTickets(tickets);
//            list.add(pnr);
//        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> searchPnrsToday(Date date) {

        List<Pnr> list = new ArrayList<>();

        String hql = "select t,p from Ticket t "
                + "inner join t.pnr as p "
                + "where t.docIssuedate = :date group by p.id";

        Query query = getSession().createQuery(hql);
        query.setDate("date", date);
        List results = query.list();

        Iterator it = results.iterator();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            Ticket leadPaxTicket = (Ticket) objects[0];
            Pnr pnr = (Pnr) objects[1];
            pnr.setTickets(null);//Avoid lazy loading here. Create a new hashset and set
            pnr.setSegments(null);
            pnr.setRemarks(null);
            Set<Ticket> tickets = new LinkedHashSet<>();
            tickets.add(leadPaxTicket);
            pnr.setTickets(tickets);
            list.add(pnr);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> getByTktNo(String ticketNo) {
        String hql = "select t,p from Ticket t "
                + "inner join t.pnr as p "
                + "where t.ticketNo = :ticketNo "
                + "group by p.id";

        Query query = getSession().createQuery(hql);
        query.setParameter("ticketNo", ticketNo);
        List results = query.list();
        Iterator it = results.iterator();
        List<Pnr> list = new ArrayList<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            Ticket leadPaxTicket = (Ticket) objects[0];
            Pnr pnr = (Pnr) objects[1];
            pnr.setSegments(null);
            pnr.setRemarks(null);
            pnr.setAgent(null);
            pnr.setCustomer(null);
            pnr.setTicketing_agent(null);
            Set<Ticket> tickets = new LinkedHashSet<>();
            tickets.add(leadPaxTicket);
            pnr.setTickets(tickets);
            list.add(pnr);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> getByGDSPnr(String gdsPnr) {
        String hql = "select t,p from Ticket t "
                + "inner join t.pnr as p "
                + "where p.gdsPnr = :gdsPnr "
                + "group by p.id";

        Query query = getSession().createQuery(hql);
        query.setParameter("gdsPnr", gdsPnr);
        List results = query.list();
        Iterator it = results.iterator();
        List<Pnr> list = new ArrayList<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            Ticket leadPaxTicket = (Ticket) objects[0];
            Pnr pnr = (Pnr) objects[1];
            pnr.setSegments(null);
            pnr.setRemarks(null);
            pnr.setAgent(null);
            pnr.setCustomer(null);
            pnr.setTicketing_agent(null);
            Set<Ticket> tickets = new LinkedHashSet<>();
            tickets.add(leadPaxTicket);
            pnr.setTickets(tickets);
            list.add(pnr);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> getByInvRef(String invref) {
        String hql = "select t,p from Ticket t "
                + "inner join t.pnr as p "                
                + "where t.ticketingSalesAcDoc.reference =:invref "
                + "group by p.id";

        Query query = getSession().createQuery(hql);
        query.setLong("invref", Long.valueOf(invref));
        List results = query.list();
        Iterator it = results.iterator();
        List<Pnr> list = new ArrayList<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            Ticket leadPaxTicket = (Ticket) objects[0];
            Pnr pnr = (Pnr) objects[1];
            pnr.setSegments(null);
            pnr.setRemarks(null);
            pnr.setAgent(null);
            pnr.setCustomer(null);
            pnr.setTicketing_agent(null);
            Set<Ticket> tickets = new LinkedHashSet<>();
            tickets.add(leadPaxTicket);
            pnr.setTickets(tickets);
            list.add(pnr);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pnr> searchByPaxName(String surName, String foreName) {
        surName = nullToEmptyValue(surName).concat("%");
        foreName = nullToEmptyValue(foreName).concat("%");

        String hql = "select t,p from Ticket t "
                + "inner join t.pnr as p "
                + "where "
                + "(t.surName is null or t.surName like :surName) and "
                + "(t.foreName is null or t.foreName like :foreName) "
                + "group by p.id ";

        Query query = getSession().createQuery(hql);
        query.setParameter("surName", surName);
        query.setParameter("foreName", foreName);

        List results = query.list();
        Iterator it = results.iterator();
        List<Pnr> list = new ArrayList<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            Ticket leadPaxTicket = (Ticket) objects[0];
            Pnr pnr = (Pnr) objects[1];
            pnr.setSegments(null);
            pnr.setRemarks(null);
            pnr.setAgent(null);
            pnr.setCustomer(null);
            pnr.setTicketing_agent(null);
            Set<Ticket> tickets = new LinkedHashSet<>();
            tickets.add(leadPaxTicket);
            pnr.setTickets(tickets);
            list.add(pnr);
        }

        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> findTicketingOIDs() {
        String hql = "select p.ticketingAgtOid from Pnr p where p.ticketingAgtOid is not null group by p.ticketingAgtOid";
        Query query = getSession().createQuery(hql);
        List results = query.list();
        return new HashSet<>(results);
    }
}
