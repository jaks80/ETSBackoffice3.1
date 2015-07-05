package com.ets.accountingdoc_o.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.AccountingDocumentLine;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("accountingDocumentLineDAO")
@Transactional
public class AccountingDocumentLineDAOImpl extends GenericDAOImpl<AccountingDocumentLine, Long> implements AccountingDocumentLineDAO {

    @Override
    public List<AccountingDocumentLine> findLineItems(Date from, Date to, Long categoryId, Long itemId,Enums.ClientType clienttype, Long clientid) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch invoice.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch invoice.customer as client ";
        } else {
            concatClient = "left join fetch invoice.agent left join fetch invoice.customer ";
            clientcondition = "";
        }
        
        String hql = "select distinct l from AccountingDocumentLine as l "
                + "left join fetch l.otherService as os "
                + "left join fetch os.category as cat "
                + "inner join fetch l.otherSalesAcDoc as invoice "
                + concatClient
                + "where invoice.status <> 2 "
                + "and invoice.docIssueDate >= :from and invoice.docIssueDate <= :to "
                + clientcondition
                + "and (:categoryId is null or cat.id = :categoryId) "
                + "and (:itemId is null or os.id = :itemId) "
                + "order by l.id";
        
        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("categoryId", categoryId);
        query.setParameter("itemId", itemId);
        
        List<AccountingDocumentLine> line_items = query.list();
        return line_items;
    }

}
