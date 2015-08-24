package com.ets.accountingdoc_o.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.AccountingDocumentLine;
import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
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

    @Resource(name = "otherSalesAcDocDAO")
    private OtherSalesAcDocDAO otherSalesAcDocDAO;

    @Override
    public List<AccountingDocumentLine> findLineItems(Date from, Date to, Long categoryId, Long itemId, Enums.ClientType clienttype, Long clientid) {
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

    @Override
    public AccountingDocumentLine findByIdWithParent(Long id) {
        String hql = "select distinct l from AccountingDocumentLine as l "
                + "left join fetch l.otherSalesAcDoc "
                + "where l.id = :id";

        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);

        AccountingDocumentLine line = (AccountingDocumentLine) query.uniqueResult();
        return line;
    }

    @Override
    public void deleteLine(Long id,Long userid) {
        AccountingDocumentLine line = findByIdWithParent(id);
        Long invoiceId = line.getOtherSalesAcDoc().getId();

        delete(line);
        
        OtherSalesAcDoc invoice = otherSalesAcDocDAO.getWithChildrenById(invoiceId);
        User deletedBy = new User();
        deletedBy.setId(userid);
        invoice.setLastModifiedBy(deletedBy);        
        invoice.setDocumentedAmount(invoice.calculateDocumentedAmount());        
    }
}
