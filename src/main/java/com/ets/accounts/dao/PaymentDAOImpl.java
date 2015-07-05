package com.ets.accounts.dao;

import com.ets.GenericDAOImpl;
import com.ets.accounts.domain.Payment;
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
@Service("paymentDAO")
@Transactional
public class PaymentDAOImpl extends GenericDAOImpl<Payment, Long> implements PaymentDAO {

    @Override
    public List<Payment> findPaymentBySalesInvoice(Long invoice_id) {

        String hql = "select distinct p from Payment as p "
                + "inner join fetch p.tSalesPayments as sp "
                + "left join fetch sp.parent as sinv "
                + "where sinv.id = :invoice_id";

        Query query = getSession().createQuery(hql);
        query.setParameter("invoice_id", invoice_id);
        List<Payment> result = query.list();
        return result;
    }

    @Override
    public Payment findById(Long id) {
        String hql = "select distinct p from Payment as p "
                + "left join fetch p.tSalesAcDocuments as sp "
                + "left join fetch sp.pnr as pnr "
                + "left join fetch pnr.agent "
                + "left join fetch pnr.customer "
                + "left join fetch p.tPurchaseAcDocuments as pp "
                + "left join fetch pp.pnr as pnr1 "
                + "left join fetch pnr1.ticketing_agent "
                + "left join fetch p.oSalesAcDocuments as op "
                + "where p.id = :id";

        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);
        Payment payment = (Payment) query.uniqueResult();
        return payment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findTicketingPaymentHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType salesType) {
        String concatClient = "";
        String concatSaleType = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch p.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch p.customer as client ";
        } else {
            concatClient = "left join fetch p.agent left join fetch p.customer ";
            clientcondition = "";
        }

        if (Enums.SaleType.TKTSALES.equals(salesType)) {
            concatSaleType = "left join fetch pay.tSalesAcDocuments as sp ";
        } else if (Enums.SaleType.TKTPURCHASE.equals(salesType)) {
            concatSaleType = "left join fetch pay.tPurchaseAcDocuments as sp ";
            concatClient = "inner join fetch p.ticketing_agent as client ";
        } else {
            concatSaleType = "left join fetch pay.oSalesAcDocuments as sp ";
        }

        String hql = "select distinct pay from Payment as pay "
                //+ "left join fetch pay.createdBy as createdby "
                //+ "left join fetch pay.lastModifiedBy as modifiedby "
                + concatSaleType
                + "left join fetch sp.pnr as p "
                + "left join fetch p.segments "
                + "left join fetch sp.parent as invoice "
                + "left join fetch invoice.tickets "
                + concatClient
                + "where "
                + "(sp.docIssueDate >= :from and sp.docIssueDate <= :to) "
                + clientcondition
                + "order by pay.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<Payment> payment_history = query.list();
        return payment_history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findOtherPaymentHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType salesType) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch sp.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch sp.customer as client ";
        } else {
            concatClient = "left join fetch sp.agent left join fetch sp.customer ";
            clientcondition = "";
        }

        String hql = "select distinct pay from Payment as pay "
                //+ "left join fetch pay.createdBy as createdby "
                //+ "left join fetch pay.lastModifiedBy as modifiedby "
                + "left join fetch pay.oSalesAcDocuments as sp "
                + "left join fetch sp.parent as invoice "
                + concatClient
                + "where "
                + "(sp.docIssueDate >= :from and sp.docIssueDate <= :to) "
                + clientcondition
                + "order by pay.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);

        List<Payment> payment_history = query.list();
        return payment_history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findTicketingSalesCashBook(Date from, Date to, Long userid,
            Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {

        String clientcondition = "";
        String concatClient = contactClient(clienttype);

        if (clienttype != null) {
            clientcondition = "and (:clientid is null or client.id = :clientid) ";
        }

        String hql = "select distinct pay from Payment as pay "
                + "left join fetch pay.createdBy as createdby "
                + "left join fetch pay.lastModifiedBy as modifiedby "
                + "inner join fetch pay.tSalesAcDocuments as ts "
                + "left join fetch ts.pnr as p "                
                + concatClient
                + "where "
                + "(pay.createdOn >= :from and pay.createdOn <= :to) "
                + "and (:paymentType is null or pay.paymentType = :paymentType) "
                + clientcondition
                + "and (:userid is null or createdby.id = :userid) "
                + "order by pay.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("userid", userid);
        if(paymentType!=null){
         query.setParameter("paymentType", paymentType.getId());
        }else{
         query.setParameter("paymentType", null);
        }

        List<Payment> payment_history = query.list();
        return payment_history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findTicketingPurchaseCashBook(Date from, Date to, Long userid,Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {
        String clientcondition = "";
        String concatClient = contactClient(clienttype);

        if (clienttype != null) {
            clientcondition = "and (:clientid is null or client.id = :clientid) ";
        }

        String hql = "select distinct pay from Payment as pay "
                + "left join fetch pay.createdBy as createdby "
                + "left join fetch pay.lastModifiedBy as modifiedby "
                + "inner join fetch pay.tPurchaseAcDocuments as tp "
                + "left join fetch tp.pnr as p "
                + "left join fetch p.ticketing_agent "
                + concatClient
                + "where "
                + "(pay.createdOn >= :from and pay.createdOn <= :to) "
                + "and (:paymentType is null or pay.paymentType = :paymentType) "
                + clientcondition
                + "and (:userid is null or createdby.id = :userid) "
                + "order by pay.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("userid", userid);
        if(paymentType!=null){
         query.setParameter("paymentType", paymentType.getId());
        }else{
         query.setParameter("paymentType", null);
        }

        List<Payment> payment_history = query.list();
        return payment_history;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findOtherSalesCashBook(Date from, Date to, Long userid, Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {
        String concatClient = "";
        String clientcondition = "and (:clientid is null or client.id = :clientid) ";

        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch os.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch os.customer as client ";
        } else {
            concatClient = "left join fetch os.agent left join fetch os.customer ";
            clientcondition = "";
        }

        String hql = "select distinct pay from Payment as pay "
                + "left join fetch pay.createdBy as createdby "
                + "left join fetch pay.lastModifiedBy as modifiedby "
                + "inner join fetch pay.oSalesAcDocuments as os "
                + concatClient
                + "where "
                + "(pay.createdOn >= :from and pay.createdOn <= :to) "
                + "and (:paymentType is null or pay.paymentType = :paymentType) "
                + clientcondition
                + "and (:userid is null or createdby.id = :userid) "
                + "order by pay.id";

        Query query = getSession().createQuery(hql);
        if (!clientcondition.isEmpty()) {
            query.setParameter("clientid", clientid);
        }
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("userid", userid);
        if(paymentType!=null){
         query.setParameter("paymentType", paymentType.getId());
        }else{
         query.setParameter("paymentType", null);
        }

        List<Payment> payment_history = query.list();
        return payment_history;
    }

    private String contactClient(Enums.ClientType clienttype) {
        String concatClient = "";
        if (clienttype != null && clienttype.equals(Enums.ClientType.AGENT)) {
            concatClient = "inner join fetch p.agent as client ";
        } else if (clienttype != null && clienttype.equals(Enums.ClientType.CUSTOMER)) {
            concatClient = "inner join fetch p.customer as client ";
        } else {
            concatClient = "left join fetch p.agent left join fetch p.customer ";
        }

        return concatClient;
    }
}
