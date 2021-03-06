package com.ets.accountingdoc.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.AdditionalChargeLine;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.client.domain.Agent;
import com.ets.pnr.dao.TicketDAO;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("tPurchaseAcDocDAO")
@Transactional
public class TPurchaseAcDocDAOImpl extends GenericDAOImpl<TicketingPurchaseAcDoc, Long> implements TPurchaseAcDocDAO {

    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private AdditionalChgLineDAO additionalChgLineDAO;

    @Override
    @Transactional(readOnly = true)
    public TicketingPurchaseAcDoc getWithChildrenById(Long id) {
        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.additionalChargeLines as adl "
                + "left join fetch adl.additionalCharge "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "left join fetch p.segments "
                + "left join fetch p.agent "
                + "left join fetch p.customer "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "left join fetch a.relatedDocuments as a1 "
                + "left join fetch a1.payment as payment "
                + "left join fetch a1.tickets as t1 "
                + "left join fetch a1.additionalChargeLines as adl1 "
                + "left join fetch adl1.additionalCharge "
                + "where a.id = :id";

        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);
        TicketingPurchaseAcDoc doc = (TicketingPurchaseAcDoc) query.uniqueResult();
        doc.setParent(null);
        Set<TicketingPurchaseAcDoc> related_docs = doc.getRelatedDocuments();

        for (TicketingPurchaseAcDoc rd : related_docs) {
            rd.setRelatedDocuments(null);
            rd.setParent(null);
            Set<Ticket> tickets = rd.getTickets();
            for (Ticket t : tickets) {
                t.setTicketingPurchaseAcDoc(null);
            }
        }
        return doc;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketingPurchaseAcDoc getByTicketId(Long ticketId) {
        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.additionalChargeLines as adl "
                + "left join fetch adl.additionalCharge "
                + "left join a.tickets as t "
                + "left join fetch a.relatedDocuments as a1 "
                + "left join fetch a1.payment as p "
                + "where t.id = :ticketId";

        Query query = getSession().createQuery(hql);
        query.setParameter("ticketId", ticketId);
        TicketingPurchaseAcDoc result = (TicketingPurchaseAcDoc) query.uniqueResult();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> getByPnrId(Long pnrId) {
        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.additionalChargeLines as adl "
                + "left join fetch adl.additionalCharge "
                + "left join fetch a.tickets as t "
                + "left join fetch a.relatedDocuments as a1 "
                + "left join fetch a1.payment as p "
                + "where a.pnr.id = :pnrId order by a.id asc";

        Query query = getSession().createQuery(hql);
        query.setParameter("pnrId", pnrId);
        List<TicketingPurchaseAcDoc> result = query.list();
        return result;
    }

    @Override
    public List<TicketingPurchaseAcDoc> getByGDSPnr(String GdsPnr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findOutstandingInvoiceReference(Enums.AcDocType acDocType, Long agentid, Date from, Date to) {

        String operator = ">";

        if (acDocType.equals(Enums.AcDocType.REFUND)) {
            operator = "<";//To get outstanding refund
        } else if (acDocType.equals(Enums.AcDocType.INVOICE)) {
            operator = ">";//To get outstanding invoice
        }

        String hql = "select a.reference from TicketingPurchaseAcDoc a "
                + "left join a.pnr as p  "
                + "inner join p.ticketing_agent as tktingagent "
                + "where a.status<>2 and a.type <> 1 and a.type <> 4 and "
                + "(select sum(b.documentedAmount) as total "
                + "from TicketingPurchaseAcDoc b "
                + "where a.reference=b.reference and b.status = 0 group by b.reference)" + operator + "0 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and (:agentid is null or tktingagent.id = :agentid) group by a.reference order by a.docIssueDate,a.id";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);

        List<Long> results = query.list();
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findInvoiceByRef(Long... references) {

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.additionalChargeLines as adl "
                + "left join fetch adl.additionalCharge "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "left join fetch p.segments "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "where a.type = 0 and a.reference in (:references)";

        Query query = getSession().createQuery(hql);

        query.setParameterList("references", references);
        List<TicketingPurchaseAcDoc> invoices = query.list();
        return invoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List findOutstandingInvoiceSQL(Enums.AcDocType type, Long clientid, Date from, Date to) {

        String clientcondition = "AND (:clientid IS null OR a.id = :clientid) ";
        String dateCondition = "";
        
        if (from != null && to != null) {
            dateCondition = "AND t.docIssueDate BETWEEN :from AND :to ";
        }
        char operator = type.equals(Enums.AcDocType.REFUND) ? '<' : '>';

        String sqlAgent = "SELECT t.id, t.docIssueDate, t1.reference, "
                + "pnr.gdsPnr, pnr.noOfPax, pnr.flightSummery, pnr.leadPax, pnr.airLineCode, "
                + "t.documentedAmount AS inv_amount, SUM(t1.documentedAmount) AS balance, "
                + "GROUP_CONCAT(t1.type) AS types, GROUP_CONCAT(t1.documentedAmount) AS amounts, "
                + "created_by.surName, created_by.foreName, a.name "
                + "FROM tkt_purch_acdoc t "
                + "LEFT JOIN tkt_purch_acdoc t1 ON t1.reference = t.reference AND t1.status = 0 "
                + "LEFT JOIN pnr ON t.pnr_fk = pnr.id "
                + "LEFT JOIN bo_user created_by ON t.created_by = created_by.id "
                + "INNER JOIN agent a ON pnr.tkagentid_fk = a.id "
                + "WHERE t.status=0 AND t.type=0 "
                + dateCondition + clientcondition
                + "GROUP BY t1.reference HAVING balance " + operator + "0 ORDER BY t.docIssueDate,t.id";

        Query query = null;

        query = getSession().createSQLQuery(sqlAgent);
        query.setParameter("clientid", clientid);

        if (from != null && to != null) {
            query.setParameter("from", from);
            query.setParameter("to", to);
        }

        List results = query.list();

        return results;
    }

    /**
     * acDocType Invoice return due invoices acDocType Refund return due refund
     *
     * @deprecated
     * @param acDocType
     * @param agentid
     * @param from
     * @param to
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findOutstandingInvoice(Enums.AcDocType acDocType, Long agentid, Date from, Date to) {

        String operator = ">";

        if (acDocType.equals(Enums.AcDocType.REFUND)) {
            operator = "<";//To get outstanding refund
        } else if (acDocType.equals(Enums.AcDocType.INVOICE)) {
            operator = ">";//To get outstanding invoice
        }

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "left join fetch p.segments "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "where a.status <> 2 and a.type = 0 and "
                + "(select sum(b.documentedAmount) as total "
                + "from TicketingPurchaseAcDoc b "
                + "where a.reference=b.reference and b.status <> 2 group by b.reference)" + operator + "0 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and (:agentid is null or tktingagent.id = :agentid) order by a.docIssueDate,a.id";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);

        List<TicketingPurchaseAcDoc> dueInvoices = query.list();
        return dueInvoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findOutstandingBSPInvoice(Long agentid, Date from, Date to) {

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "left join fetch p.segments "
                + "left join fetch r.tickets as rt "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "left join fetch a.parent as parent "
                + "left join fetch parent.relatedDocuments as pr "
                + "left join fetch parent.tickets as pt "
                + "left join fetch parent.pnr as pp "
                + "left join fetch pp.segments "
                + "left join fetch parent.tickets as prt "
                //+ "inner join fetch p.ticketing_agent as tktingagent "
                + "where a.status = 0 and a.type <> 1 and a.type <> 4 and "
                + "(select sum(b.documentedAmount) as total "
                + "from TicketingPurchaseAcDoc b "
                + "where a.reference=b.reference and b.status = 0 group by b.reference) <> 0 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and tktingagent.id = :agentid order by a.docIssueDate,a.id";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);

        List<TicketingPurchaseAcDoc> dueInvoices = query.list();
        return dueInvoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> find_ADM_ACM(Long agentid, Date from, Date to, Enums.AcDocType acDocType) {

        String operator = ">";

        if (acDocType.equals(Enums.AcDocType.REFUND)) {
            operator = "<";//To get outstanding refund
        } else if (acDocType.equals(Enums.AcDocType.INVOICE)) {
            operator = ">";//To get outstanding invoice
        }

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "left join fetch a.parent as parent "
                + "left join fetch parent.relatedDocuments as pr "
                + "left join fetch parent.tickets as pt "
                + "left join fetch parent.pnr as pp "
                + "left join fetch pp.segments "
                + "left join fetch parent.tickets as prt "
                + "where a.status = 0 and (a.type = 2 or a.type = 3) and"
                + "(select sum(b.documentedAmount) as total "
                + "from TicketingPurchaseAcDoc b "
                + "where a.reference=b.reference and b.status = 0 group by b.reference)" + operator + "0 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and (:agentid is null or tktingagent.id = :agentid) order by a.docIssueDate,a.id";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);

        List<TicketingPurchaseAcDoc> docs = query.list();
        return docs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findBSP_ADM_ACM(Long agentid, Date from, Date to) {

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "where a.status = 0 and (a.type = 2 or a.type = 3) and"
                + "(select sum(b.documentedAmount) as total "
                + "from TicketingPurchaseAcDoc b "
                + "where a.reference=b.reference and b.status = 0 group by b.reference) <> 0 and t is null "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and tktingagent.id = :agentid order by a.docIssueDate";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);

        List<TicketingPurchaseAcDoc> docs = query.list();
        return docs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findInvoiceHistory(Long agentid, Date from, Date to) {

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.relatedDocuments as r "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "left join fetch p.segments "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "where a.status = 0 and a.type = 0 and "
                + "a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and (:agentid is null or tktingagent.id = :agentid) order by a.docIssueDate,a.id";

        Query query = getSession().createQuery(hql);

        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("agentid", agentid);
        List<TicketingPurchaseAcDoc> dueInvoices = query.list();
        return dueInvoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketingPurchaseAcDoc> findAllDocuments(Long agentid, Date from, Date to) {

        String hql = "select distinct a from TicketingPurchaseAcDoc as a "
                + "left join fetch a.payment as payment "
                + "left join fetch a.tickets as t "
                + "left join fetch a.pnr as p "
                + "inner join fetch p.ticketing_agent as tktingagent "
                + "left join fetch p.segments "
                + "where a.status <> 2 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "and (:agentid is null or tktingagent.id = :agentid)"
                + " order by a.id asc";

        Query query = getSession().createQuery(hql);

        query.setParameter("agentid", agentid);
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<TicketingPurchaseAcDoc> history = query.list();
        return history;
    }

    @Override
    public BigDecimal getAccountBallanceToDate(Long agentid, Date to) {

        String hql = "select coalesce(sum(a.documentedAmount),0) as balance "
                + "from TicketingPurchaseAcDoc a "
                + "left join a.pnr as p "
                + "inner join p.ticketing_agent as tktingagent "
                + "where "
                + "(:agentid is null or tktingagent.id = :agentid)"
                + "and a.docIssueDate <= :to ";

        Query query = getSession().createQuery(hql);
        query.setParameter("agentid", agentid);
        query.setParameter("to", to);

        Object balance = query.uniqueResult();
        return new BigDecimal(balance.toString());
    }

    @Override
    public boolean voidDocument(TicketingPurchaseAcDoc doc) {
        Set<Ticket> tickets = doc.getTickets();

        doc.setTickets(null);

        if (tickets != null && !tickets.isEmpty()) {
            for (Ticket t : tickets) {
                t.setTicketingPurchaseAcDoc(null);
            }
            ticketDAO.saveBulk(new ArrayList(tickets));
        }

        Set<AdditionalChargeLine> additionalChargeLines = doc.getAdditionalChargeLines();

        if (!additionalChargeLines.isEmpty()) {
            additionalChgLineDAO.deleteBulk(additionalChargeLines);
            doc.setAdditionalChargeLines(null);
        }

        //Do we need to void related documents Exp: Payment for purchase invoice.
//        if (doc.getType().equals(Enums.AcDocType.INVOICE)) {
//            Set<TicketingPurchaseAcDoc> related_docs = doc.getRelatedDocuments();
//            for (TicketingPurchaseAcDoc d : related_docs) {
//                d.setParent(null);
//                d.setStatus(Enums.AcDocStatus.VOID);
//            }
//            saveBulk(new ArrayList(related_docs));
//        }
        doc.setStatus(Enums.AcDocStatus.VOID);
        save(doc);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> allAgentOutstandingReport(Date from, Date to) {
        String sql = " select agt.name as agentname, coalesce(sum(acdoc.documentedAmount), 0) as balance "
                + "from tkt_purch_acdoc invoice "
                + "left outer join tkt_purch_acdoc acdoc on invoice.reference=acdoc.reference and (acdoc.status<>2) "
                + "left outer join pnr p on invoice.pnr_fk=p.id "
                + "inner join agent agt on p.tkagentid_fk=agt.id "
                + "where invoice.status<>2 and invoice.type=0 and "
                + "(select sum(ticketings4_.documentedAmount) from tkt_purch_acdoc ticketings4_ where invoice.reference=ticketings4_.reference and ticketings4_.status<>2 group by ticketings4_.reference)>0 "
                + "and invoice.docIssueDate>=:from and invoice.docIssueDate<=:to "
                + "group by agt.id order by balance desc";

        Query query = getSession().createSQLQuery(sql);
        query.setParameter("from", from);
        query.setParameter("to", to);

        List results = query.list();
        Map<String, BigDecimal> map = new LinkedHashMap<>();

        Iterator it = results.iterator();
        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            map.put((String) objects[0], (BigDecimal) objects[1]);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agent> outstandingAgentsSQL(Enums.AcDocType acDocType) {

        char operator;

        if (acDocType.equals(Enums.AcDocType.REFUND)) {
            operator = '<';//To get outstanding refund
        } else {
            operator = '>';//To get outstanding invoice
        }

        String sql = "SELECT a.id, a.name, a.addLine1,a.addLine2, a.city, a.country, a.email, a.fax, a.mobile, a.postCode, "
                + "a.telNo, a.officeID,  SUM(t.documentedAmount) AS balance "
                + "FROM tkt_purch_acdoc t "
                + "LEFT JOIN pnr ON t.pnr_fk = pnr.id "
                + "INNER JOIN agent a ON pnr.tkagentid_fk = a.id "
                + "WHERE t.status = 0 "
                + "GROUP BY t.reference "
                + "HAVING balance " + operator + " 0 ORDER BY a.name ";

        Query query = getSession().createSQLQuery(sql);
        List results = query.list();

        Iterator it = results.iterator();
        Map<Long, Agent> agents = new LinkedHashMap<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();

            Agent a = new Agent();

            BigInteger bid = new BigInteger(objects[0].toString());
            a.setId(bid.longValue());

            a.setName((String) objects[1]);
            a.setAddLine1((String) objects[2]);
            a.setAddLine2((String) objects[3]);
            a.setCity((String) objects[4]);
            a.setCountry((String) objects[5]);
            a.setEmail((String) objects[6]);
            a.setFax((String) objects[7]);
            a.setMobile((String) objects[8]);
            a.setPostCode((String) objects[9]);
            a.setTelNo((String) objects[10]);
            a.setOfficeID((String) objects[11]);

            agents.put(a.getId(), a);
        }

        return new ArrayList<>(agents.values());
    }

    /**
     * @deprecated @param acDocType
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Agent> outstandingAgents(Enums.AcDocType acDocType) {

        char operator;

        if (acDocType.equals(Enums.AcDocType.REFUND)) {
            operator = '<';//To get outstanding refund
        } else {
            operator = '>';//To get outstanding invoice
        }

        String sql = "SELECT a.id, a.name, a.addLine1,a.addLine2, a.city, a.country, a.email, a.fax, a.mobile, a.postCode, a.telNo, a.officeID,  SUM(t.documentedAmount) AS balance "
                + "FROM tkt_purch_acdoc t "
                + "LEFT JOIN pnr ON t.pnr_fk = pnr.id "
                + "INNER JOIN agent a ON pnr.tkagentid_fk = a.id "
                + "WHERE t.status = 0 "
                + "GROUP BY t.reference "
                + "HAVING balance " + operator + " 0 ORDER BY a.name ";

        Query query = getSession().createSQLQuery(sql);
        List results = query.list();

        Iterator it = results.iterator();
        Map<Long, Agent> agents = new LinkedHashMap<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();

            Agent a = new Agent();

            BigInteger bid = new BigInteger(objects[0].toString());
            a.setId(bid.longValue());

            a.setName((String) objects[1]);
            a.setAddLine1((String) objects[2]);
            a.setAddLine2((String) objects[3]);
            a.setCity((String) objects[4]);
            a.setCountry((String) objects[5]);
            a.setEmail((String) objects[6]);
            a.setFax((String) objects[7]);
            a.setMobile((String) objects[8]);
            a.setPostCode((String) objects[9]);
            a.setTelNo((String) objects[10]);
            a.setOfficeID((String) objects[11]);

            agents.put(a.getId(), a);
        }

        return new ArrayList<>(agents.values());
    }

    @Override
    public List<Agent> findTicketingAgents() {

//        String hql = "SELECT agt FROM Agent AS agt "
//                + "INNER JOIN Pnr p ON p.ticketingAgtOid = agt.officeID "
//                + "WHERE agt.active = true GROUP BY p.ticketingAgtOid ORDER BY agt.name ";
        String sql = "SELECT a.id, a.name, a.addLine1, a.addLine2, a.city, a.country, a.email, a.fax, a.mobile, a.postCode, a.telNo, a.officeID " 
                + "FROM agent a "
                + "INNER JOIN pnr ON a.officeID LIKE CONCAT('%',pnr.ticketingAgtOid, '%') "
                + "GROUP BY pnr.ticketingAgtOid";

        Query query = getSession().createSQLQuery(sql);
        List results = query.list();

        Iterator it = results.iterator();
        Map<Long, Agent> agents = new LinkedHashMap<>();

        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();

            Agent a = new Agent();

            BigInteger bid = new BigInteger(objects[0].toString());
            a.setId(bid.longValue());

            a.setName((String) objects[1]);
            a.setAddLine1((String) objects[2]);
            a.setAddLine2((String) objects[3]);
            a.setCity((String) objects[4]);
            a.setCountry((String) objects[5]);
            a.setEmail((String) objects[6]);
            a.setFax((String) objects[7]);
            a.setMobile((String) objects[8]);
            a.setPostCode((String) objects[9]);
            a.setTelNo((String) objects[10]);
            a.setOfficeID((String) objects[11]);

            agents.put(a.getId(), a);
        }

        return new ArrayList<>(agents.values());

    }

}
