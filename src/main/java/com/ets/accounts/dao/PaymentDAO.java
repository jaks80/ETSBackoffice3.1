package com.ets.accounts.dao;

import com.ets.GenericDAO;
import com.ets.accounts.domain.Payment;
import com.ets.util.Enums;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface PaymentDAO extends GenericDAO<Payment, Long>{
    
    public List<Payment> findPaymentBySalesInvoice(Long invoice_id);
    
    public Payment findById(Long id);
    
    public List<Payment> findTicketingSalesCashBook(Date from, Date to,Long userId, Enums.ClientType clienttype, Long clientid,Enums.PaymentType paymentType);
    
    public List<Payment> findTicketingPurchaseCashBook(Date from, Date to,Long userId, Enums.ClientType clienttype,Long clientid,Enums.PaymentType paymentType);
        
    public List<Payment> findOtherSalesCashBook(Date from, Date to,Long userId, Enums.ClientType clienttype, Long clientid,Enums.PaymentType paymentType);
    
    public List<Payment> findTicketingPaymentHistory(Enums.ClientType clienttype,Long clientid,Date from, Date to,Enums.SaleType saleType);
    
    public List<Payment> findOtherPaymentHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType salesType);
}
