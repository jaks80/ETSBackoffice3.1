package com.ets.accounts.logic;

import com.ets.accountingdoc.domain.OtherSalesAcDoc;
import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.accounts.domain.Payment;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public class PaymentLogic {

    /**
     * If invoice amount is greater then 0 then It will be payment. If invoice
     * amount is less then 0 the It will be a refund. If invoice amount is 0
     * then payment will inactive
     *
     * @param amount
     * @param invoice
     * @param remark
     * @param type
     * @param user
     * @return
     */
    public synchronized static Payment processSingleTSalesPayment(BigDecimal amount, TicketingSalesAcDoc invoice,
            String remark, Enums.PaymentType type, User user) {
        if (amount.compareTo(invoice.calculateDueAmount().abs()) > 0) {
            return null;
        } else {
            Payment payment = new Payment();
            payment.setRemark(remark);
            payment.setPaymentType(type);
            payment.setCreatedOn(new java.util.Date());
            payment.setCreatedBy(user);

            TicketingSalesAcDoc doc = new TicketingSalesAcDoc();
            doc.setReference(invoice.getReference());
            doc.setStatus(Enums.AcDocStatus.ACTIVE);
            doc.setDocIssueDate(new java.util.Date());
            doc.setPnr(invoice.getPnr());
            doc.setCreatedOn(new java.util.Date());
            doc.setCreatedBy(user);
            doc.setParent(invoice);
            doc.setPayment(payment);
            if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == 1) {
                //Make payment   
                doc.setType(Enums.AcDocType.PAYMENT);
                doc.setDocumentedAmount(amount.negate());//Payment saves as negative
            } else if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == -1) {
                //Make refund
                doc.setType(Enums.AcDocType.REFUND);
                doc.setDocumentedAmount(amount);
            } else {
                //Do nothing  
                return null;
            }
            //invoice.addRelatedDocument(doc);
            payment.addTSalesDocument(doc);

            return payment;
        }
    }

    public synchronized static Payment processSingleTPurchasePayment(BigDecimal amount, TicketingPurchaseAcDoc invoice, String remark,
            Enums.PaymentType type, User user) {
        if (amount.compareTo(invoice.calculateDueAmount().abs()) > 0) {
            return null;
        } else {
            Payment payment = new Payment();
            payment.setRemark(remark);
            payment.setPaymentType(type);
            payment.setCreatedOn(new java.util.Date());
            payment.setCreatedBy(user);

            TicketingPurchaseAcDoc doc = new TicketingPurchaseAcDoc();
            doc.setReference(invoice.getReference());
            doc.setStatus(Enums.AcDocStatus.ACTIVE);
            doc.setDocIssueDate(new java.util.Date());
            doc.setPnr(invoice.getPnr());
            doc.setCreatedBy(user);
            doc.setCreatedOn(new java.util.Date());
            doc.setParent(invoice);
            doc.setPayment(payment);

            if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == 1) {
                //Make payment   
                doc.setType(Enums.AcDocType.PAYMENT);
                doc.setDocumentedAmount(amount.negate());//Payment saves as negative
            } else if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == -1) {
                //Make refund
                doc.setType(Enums.AcDocType.REFUND);
                doc.setDocumentedAmount(amount);
            } else {
                //Do nothing  
                return null;
            }

            payment.addTPurchaseDocument(doc);
            return payment;
        }
    }

    public synchronized static Payment processSingleOSalesPayment(BigDecimal amount, OtherSalesAcDoc invoice,
            String remark, Enums.PaymentType type, User user) {
        if (amount.compareTo(invoice.calculateDueAmount().abs()) > 0) {
            return null;
        } else {
            Payment payment = new Payment();
            payment.setRemark(remark);
            payment.setPaymentType(type);
            payment.setCreatedOn(new java.util.Date());
            payment.setCreatedBy(user);

            OtherSalesAcDoc doc = new OtherSalesAcDoc();
            doc.setReference(invoice.getReference());
            doc.setStatus(Enums.AcDocStatus.ACTIVE);
            doc.setDocIssueDate(new java.util.Date());
            doc.setCreatedBy(user);
            doc.setCreatedOn(new java.util.Date());
            doc.setAgent(invoice.getAgent());
            doc.setCustomer(invoice.getCustomer());
            doc.setParent(invoice);
            doc.setPayment(payment);

            if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == 1) {
                //Make payment   
                doc.setType(Enums.AcDocType.PAYMENT);
                doc.setDocumentedAmount(amount.negate());//Payment saves as negative
            } else if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == -1) {
                //Make refund
                doc.setType(Enums.AcDocType.REFUND);
                doc.setDocumentedAmount(amount);
            } else {
                //Do nothing  
                return null;
            }

            payment.addOtherDocument(doc);
            return payment;
        }
    }

    public BigDecimal calculateTotalDueAmount(List<TicketingSalesAcDoc> invoices) {

        BigDecimal total = new BigDecimal("0.00");
        for (TicketingSalesAcDoc inv : invoices) {
            total = total.add(inv.calculateDueAmount());
        }

        return total;
    }

    /**
     * BSP Payment logic: BSP payment is fixed for the period. So just pay due
     * invoice amount or pay due invoice refund. Refund may occur from last
     * months invoice payment.
     *
     * @param payment_amount
     * @param invoices
     * @param user
     * @param paymentDate
     * @return
     */
    public synchronized Payment processBSPPayment(List<TicketingPurchaseAcDoc> invoices, User user,Date paymentDate) {
      
        Payment payment = new Payment();
        payment.setRemark("BSP payment");
        payment.setPaymentType(Enums.PaymentType.BANKT_TANSFER);
        payment.setCreatedOn(paymentDate);
        payment.setCreatedBy(user);

        for (TicketingPurchaseAcDoc invoice : invoices) {
            TicketingPurchaseAcDoc doc = new TicketingPurchaseAcDoc();
            doc.setReference(invoice.getReference());
            doc.setStatus(Enums.AcDocStatus.ACTIVE);
            doc.setDocIssueDate(paymentDate);
            doc.setPnr(invoice.getPnr());
            doc.setCreatedBy(user);
            doc.setCreatedOn(paymentDate);
            doc.setParent(invoice);
            doc.setPayment(payment);

            if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == 1) {
                //Make payment   
                doc.setType(Enums.AcDocType.PAYMENT);
                doc.setDocumentedAmount(invoice.calculateDueAmount().negate());//Payment saves as negative
            } else if (invoice.calculateDueAmount().compareTo(new BigDecimal("0.00")) == -1) {
                //Make refund
                doc.setType(Enums.AcDocType.REFUND);
                doc.setDocumentedAmount(invoice.calculateDueAmount().abs());//Refund saves a +
            }
            payment.addTPurchaseDocument(doc);
        }
        return payment;
    }

    public BigDecimal calculateTotalDueAmountOther(List<OtherSalesAcDoc> invoices) {

        BigDecimal total = new BigDecimal("0.00");
        for (OtherSalesAcDoc inv : invoices) {
            total = total.add(inv.calculateDueAmount());
        }

        return total;
    }

    public BigDecimal calculateTotalPDueAmount(List<TicketingPurchaseAcDoc> invoices) {

        BigDecimal total = new BigDecimal("0.00");
        for (TicketingPurchaseAcDoc inv : invoices) {
            total = total.add(inv.calculateDueAmount());
        }

        return total;
    }
}
