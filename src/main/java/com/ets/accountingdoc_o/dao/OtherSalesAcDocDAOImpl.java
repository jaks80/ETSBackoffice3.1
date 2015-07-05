package com.ets.accountingdoc_o.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.dao.AdditionalChgLineDAO;
import com.ets.accountingdoc.domain.*;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.util.*;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("otherSalesAcDocDAO")
@Transactional
public class OtherSalesAcDocDAOImpl extends GenericDAOImpl<OtherSalesAcDoc, Long> implements OtherSalesAcDocDAO {

    @Autowired
    private AdditionalChgLineDAO additionalChgLineDAO;

    @Autowired
    private AccountingDocumentLineDAO accountingDocumentLineDAO;

    @Override
    public Long getNewAcDocRef() {
        String hql = "select max(acDoc.reference) from OtherSalesAcDoc acDoc";
        Query query = getSession().createQuery(hql);

        Object result = DataAccessUtils.uniqueResult(query.list());
        return (result != null) ? Long.valueOf(result.toString()) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public OtherSalesAcDoc getWithChildrenById(Long id) {

        String hql = "select distinct a from OtherSalesAcDoc as a "
                + "left join fetch a.accountingDocumentLines as adl "
                + "left join fetch adl.otherService as os "
                + "left join fetch os.category "
                + "left join fetch a.additionalChargeLines as acl "
                + "left join fetch acl.additionalCharge "
                + "left join fetch a.agent "
                + "left join fetch a.customer "
                + "left join fetch a.relatedDocuments as a1 "
                + "left join fetch a1.payment as payment "
                + "left join fetch a1.accountingDocumentLines as adl1 "
                + "left join fetch adl1.otherService os1 "
                + "left join fetch os1.category "
                + "left join fetch a1.additionalChargeLines as acl1 "
                + "left join fetch acl1.additionalCharge "
                + "where a.id = :id";

        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);
        OtherSalesAcDoc doc = (OtherSalesAcDoc) query.uniqueResult();

        Set<OtherSalesAcDoc> related_docs = doc.getRelatedDocuments();

        for (OtherSalesAcDoc rd : related_docs) {
            rd.setRelatedDocuments(null);
            rd.setParent(null);
            Set<AccountingDocumentLine> lines = rd.getAccountingDocumentLines();
            for (AccountingDocumentLine l : lines) {
                l.setOtherSalesAcDoc(null);
                //l.setTicketingSalesAcDoc(null);
            }
        }
        return doc;
    }

    @Override
    public List<OtherSalesAcDoc> findAllById(Long... id) {
        String hql = "select distinct a from OtherSalesAcDoc as a "
                + "left join fetch a.accountingDocumentLines as adl "
                + "left join fetch a.additionalChargeLines as acl "
                + "left join fetch a.relatedDocuments as a1 "
                + "left join fetch a1.accountingDocumentLines as adl1 "
                + "left join fetch a1.additionalChargeLines as acl1 "
                + "where a.type = 0 and a.id in (:id)";

        Query query = getSession().createQuery(hql);
        query.setParameterList("id", id);
        List<OtherSalesAcDoc> result = query.list();
        return result;
    }

    @Override
    public boolean voidDocument(OtherSalesAcDoc doc) {

        Set<AccountingDocumentLine> lines = doc.getAccountingDocumentLines();
        accountingDocumentLineDAO.deleteBulk(lines);
        doc.setAccountingDocumentLines(null);
        Set<AdditionalChargeLine> additionalChargeLines = doc.getAdditionalChargeLines();

        if (!additionalChargeLines.isEmpty()) {
            additionalChgLineDAO.deleteBulk(additionalChargeLines);
            doc.setAdditionalChargeLines(null);
        }
        doc.setStatus(Enums.AcDocStatus.VOID);
        save(doc);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherSalesAcDoc> findOutstandingDocuments(Enums.AcDocType type, Enums.ClientType clienttype, Long clientid, Date from, Date to) {
        String concatClient = "";
        String dateCondition = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";
        char operator = '>';

        if (from != null && to != null) {
            dateCondition = "and a.docIssueDate >= :from and a.docIssueDate <= :to ";
        }

        if (type.equals(Enums.AcDocType.REFUND)) {
            operator = '<';//To get outstanding refund
        } else {
            operator = '>';//To get outstanding invoice
        }

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch a.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch a.customer as client ";
        } else {
            concatClient = "left join fetch a.agent left join fetch a.customer ";
            clientcondition = "";
        }

        String hql = "select distinct a from OtherSalesAcDoc as a "
                + "left join fetch a.accountingDocumentLines as adl "
                + "left join a.createdBy as user "
                + "left join fetch adl.otherService as os "
                + "left join fetch os.category "
                + "left join fetch a.relatedDocuments as r "
                + concatClient
                + "where a.status = 0 and a.type = 0 and "
                + "(select sum(b.documentedAmount) as total "
                + "from OtherSalesAcDoc b "
                + "where a.reference=b.reference and b.status = 0 group by b.reference)" + operator + "0 "
                + dateCondition
                + clientcondition
                + " order by a.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }

        if (from != null && to != null) {
            query.setParameter("from", from);
            query.setParameter("to", to);
        }

        List<OtherSalesAcDoc> dueInvoices = query.list();
        return dueInvoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherSalesAcDoc> findInvoiceHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch a.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch a.customer as client ";
        } else {
            concatClient = "left join fetch a.agent left join fetch a.customer ";
            clientcondition = "";
        }

        String hql = "select distinct a from OtherSalesAcDoc as a "
                + "left join fetch a.accountingDocumentLines as adl "
                + "left join a.createdBy as user "
                + "left join fetch adl.otherService as os "
                + "left join fetch os.category "
                + "left join fetch a.relatedDocuments as r "
                + concatClient
                + "where a.type = 0 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + clientcondition
                + " order by a.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<OtherSalesAcDoc> invoice_history = query.list();
        return invoice_history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherSalesAcDoc> findAllDocuments(Enums.ClientType clienttype, Long clientid, Date from, Date to) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch a.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch a.customer as client ";
        } else {
            concatClient = "left join fetch a.agent left join fetch a.customer ";
            clientcondition = "";
        }

        String hql = "select distinct a from OtherSalesAcDoc as a "
                + "left join fetch a.payment as payment "
                + "left join fetch a.accountingDocumentLines as adl "
                + "left join fetch adl.otherService as os "
                + "left join fetch os.category "
                + "left join fetch a.relatedDocuments as r "
                + concatClient
                + "where a.status <> 2 "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + clientcondition
                + " order by a.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<OtherSalesAcDoc> invoice_history = query.list();
        return invoice_history;
    }

    @Override
    public BigDecimal getAccountBallanceToDate(Enums.ClientType clienttype, Long clientid, Date to) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join a.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join a.customer as client ";
        } else {
            concatClient = "left join a.agent left join a.customer ";
            clientcondition = "";
        }

        String hql = "select coalesce(sum(a.documentedAmount),0) as balance "
                + "from OtherSalesAcDoc as a "
                + concatClient
                + "where a.status <> 2 and "
                + "(:clientid is null or client.id = :clientid) "
                + "and a.docIssueDate <= :to "
                + clientcondition;

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }

        query.setParameter("to", to);

        Object balance = query.uniqueResult();
        return new BigDecimal(balance.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<User, BigDecimal> userProductivityReport(Date from, Date to) {
        String hql = "select user.surName, user.foreName, user.loginID, "
                + "coalesce(sum(a.documentedAmount),0) as balance "
                + "from OtherSalesAcDoc a "
                + "left join a.createdBy as user "
                + "where a.status <> 2 and a.type <> 1 and a.type <> 4 and user.active = true "
                + "and a.docIssueDate >= :from and a.docIssueDate <= :to "
                + "group by user.id order by balance desc ";

        Query query = getSession().createQuery(hql);
        query.setParameter("from", from);
        query.setParameter("to", to);

        List results = query.list();
        Map<User, BigDecimal> map = new LinkedHashMap<>();

        Iterator it = results.iterator();
        while (it.hasNext()) {
            Object[] objects = (Object[]) it.next();
            User user = new User();
            user.setSurName((String) objects[0]);
            user.setForeName((String) objects[1]);
            user.setLoginID((String) objects[2]);
            map.put(user, (BigDecimal) objects[3]);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> allAgentOutstandingReport(Date from,Date to) {
        
        String sql = " select agt.name as agentname, coalesce(sum(acdoc.documentedAmount), 0) as balance "
                + "from other_sales_acdoc invoice "
                + "left outer join other_sales_acdoc acdoc on invoice.reference=acdoc.reference and (acdoc.status<>2) "
                + "inner join agent agt on invoice.agentid_fk=agt.id "
                + "where invoice.status<>2 and invoice.type=0 and "
                + "(select sum(other4_.documentedAmount) from other_sales_acdoc other4_ where invoice.reference=other4_.reference and other4_.status<>2 group by other4_.reference)>0 "
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
}
