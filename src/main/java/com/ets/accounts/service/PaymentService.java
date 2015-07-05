package com.ets.accounts.service;

import com.ets.accounts.domain.Payment;
import com.ets.accounts.model.*;
import com.ets.accounts.dao.PaymentDAO;
import com.ets.accountingdoc.domain.*;
import com.ets.accountingdoc.service.TPurchaseAcDocService;
import com.ets.accountingdoc.service.TSalesAcDocService;
import com.ets.accountingdoc_o.service.OSalesAcDocService;
import com.ets.accounts.logic.PaymentLogic;
import com.ets.security.LoginManager;
import com.ets.settings.domain.User;
import com.ets.util.Enums;
import com.ets.util.PnrUtil;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("paymentService")
public class PaymentService {

    @Resource(name = "paymentDAO")
    private PaymentDAO dao;
    @Autowired
    private TSalesAcDocService tSalesAcDocService;
    @Autowired
    private TPurchaseAcDocService tPurchaseAcDocService;
    @Autowired
    private OSalesAcDocService oSalesAcDocService;

    public Payment newBSPPayment(Long agentid, Date dateStart, Date dateEnd, Long userid, Date paymentDate) {
        List<TicketingPurchaseAcDoc> invoices = tPurchaseAcDocService.dueBSPInvoices(agentid, dateStart, dateEnd);
        User user = LoginManager.getUserById(userid);
        PaymentLogic logic = new PaymentLogic();

        Payment bspPayment = logic.processBSPPayment(invoices, user, paymentDate);

        save(bspPayment);
        return bspPayment;
    }

    public Payment save(Payment payment) {
        Set<TicketingSalesAcDoc> sdocs = payment.gettSalesAcDocuments();
        Set<TicketingPurchaseAcDoc> pdocs = payment.gettPurchaseAcDocuments();
        Set<OtherSalesAcDoc> odocs = payment.getoSalesAcDocuments();

        for (TicketingSalesAcDoc d : sdocs) {
            d.setPayment(payment);
        }

        for (TicketingPurchaseAcDoc d : pdocs) {
            d.setPayment(payment);
        }

        for (OtherSalesAcDoc d : odocs) {
            d.setPayment(payment);
        }

        dao.save(payment);

        sdocs = payment.gettSalesAcDocuments();
        pdocs = payment.gettPurchaseAcDocuments();
        odocs = payment.getoSalesAcDocuments();

        for (TicketingSalesAcDoc d : sdocs) {
            d.setPayment(null);
        }

        for (TicketingPurchaseAcDoc d : pdocs) {
            d.setPayment(null);
        }

        for (OtherSalesAcDoc d : odocs) {
            d.setPayment(null);
        }
        return payment;
    }

    public Payment findPaymentById(Long id) {
        Payment payment = dao.findById(id);
        return payment;
    }

    public String delete(Long paymentid) {        
        Payment payment = dao.findById(paymentid);
        dao.delete(payment);
        return "Deleted";
    }

    public Payment _void(Long paymentid) {
        Payment payment = dao.findById(paymentid);
        if (!payment.gettSalesAcDocuments().isEmpty()) {
            for (TicketingSalesAcDoc d : payment.gettSalesAcDocuments()) {
                d.setParent(null);
                d.setStatus(Enums.AcDocStatus.VOID);
            }
            dao.save(payment);
            return findTSalesPaymentById(paymentid);
        } else if (!payment.gettPurchaseAcDocuments().isEmpty()) {

            for (TicketingPurchaseAcDoc d : payment.gettPurchaseAcDocuments()) {
                d.setParent(null);
                d.setStatus(Enums.AcDocStatus.VOID);
            }
            dao.save(payment);
            return findTPurchasePaymentById(paymentid);
        } else if (!payment.getoSalesAcDocuments().isEmpty()) {
            for (OtherSalesAcDoc d : payment.getoSalesAcDocuments()) {
                d.setParent(null);
                d.setStatus(Enums.AcDocStatus.VOID);
            }
            dao.save(payment);
            return findOSalesPaymentById(paymentid);
        }

        return payment;
    }

    public Payment findTSalesPaymentById(Long id) {
        Payment payment = dao.findById(id);

        Set<TicketingSalesAcDoc> sdocs = payment.gettSalesAcDocuments();

        if (sdocs != null && !sdocs.isEmpty()) {
            for (TicketingSalesAcDoc d : sdocs) {
                d.setTickets(null);
                d.setRelatedDocuments(null);
                d.setAdditionalChargeLines(null);
                d.setParent(null);
                d.setPayment(null);
                d.setCreatedBy(null);
                d.setLastModifiedBy(null);
                PnrUtil.undefineChildrenInPnr(d.getPnr());
            }
            payment.settPurchaseAcDocuments(null);
            payment.setoSalesAcDocuments(null);
        }
        return payment;
    }

    public Payment findTPurchasePaymentById(Long id) {
        Payment payment = dao.findById(id);

        Set<TicketingPurchaseAcDoc> pdocs = payment.gettPurchaseAcDocuments();

        if (pdocs != null && !pdocs.isEmpty()) {
            for (TicketingPurchaseAcDoc d : pdocs) {
                d.setTickets(null);
                d.setRelatedDocuments(null);
                d.setAdditionalChargeLines(null);
                d.setParent(null);
                d.setPayment(null);
                d.setCreatedBy(null);
                d.setLastModifiedBy(null);
                PnrUtil.undefineChildrenInPnr(d.getPnr());
            }
            payment.settSalesAcDocuments(null);
            payment.setoSalesAcDocuments(null);
        }
        return payment;
    }

    public Payment findOSalesPaymentById(Long id) {
        Payment payment = dao.findById(id);

        Set<OtherSalesAcDoc> odocs = payment.getoSalesAcDocuments();

        if (odocs != null && !odocs.isEmpty()) {
            for (OtherSalesAcDoc d : odocs) {
                d.setRelatedDocuments(null);
                d.setAdditionalChargeLines(null);
                d.setAccountingDocumentLines(null);
                d.setParent(null);
                d.setPayment(null);
                d.setCreatedBy(null);
                d.setLastModifiedBy(null);
            }
            payment.settPurchaseAcDocuments(null);
            payment.settSalesAcDocuments(null);
        }

        return payment;
    }

    /**
     * Make a individual refund from invoices to refund. And then pay same
     * amount to the invoice to pay. This is credit transfer. Refund map
     * contains "invoice id to refund" and amount to refund.
     *
     * @param creditTransfer
     */
    public void createCreditTransfer(CreditTransfer creditTransfer) {

        LinkedHashMap<Long, BigDecimal> refundMap = creditTransfer.getRefundMap();
        Long[] ids = refundMap.keySet().toArray(new Long[refundMap.size()]);
        List<Payment> newTransactions = new ArrayList<>();

        if (creditTransfer.getSaleType().equals(Enums.SaleType.TKTSALES)) {
            List<TicketingSalesAcDoc> invoiceToRefund = tSalesAcDocService.findAllById(ids);
            TicketingSalesAcDoc invoiceToPay = tSalesAcDocService.getWithChildrenById(creditTransfer.getInvoiceId());

            for (TicketingSalesAcDoc rfd_doc : invoiceToRefund) {
                BigDecimal amount = refundMap.get(rfd_doc.getId());
                String remark = "Credit Transfer from: " + rfd_doc.getReference() + " to " + invoiceToPay.getReference();

                Payment refund = PaymentLogic.processSingleTSalesPayment(amount, rfd_doc, remark,
                        Enums.PaymentType.CREDIT_TRANSFER, creditTransfer.getUser());

                newTransactions.add(refund);
                Payment payment = PaymentLogic.processSingleTSalesPayment(amount, invoiceToPay, remark,
                        Enums.PaymentType.CREDIT_TRANSFER, creditTransfer.getUser());
                newTransactions.add(payment);
            }
        } else if (creditTransfer.getSaleType().equals(Enums.SaleType.OTHERSALES)) {
            List<OtherSalesAcDoc> invoiceToRefund = oSalesAcDocService.findAllById(ids);
            OtherSalesAcDoc invoiceToPay = oSalesAcDocService.getWithChildrenById(creditTransfer.getInvoiceId());

            for (OtherSalesAcDoc rfd_doc : invoiceToRefund) {
                BigDecimal amount = refundMap.get(rfd_doc.getId());
                String remark = "Credit Transfer from: " + rfd_doc.getReference() + " to " + invoiceToPay.getReference();

                Payment refund = PaymentLogic.processSingleOSalesPayment(amount, rfd_doc, remark,
                        Enums.PaymentType.CREDIT_TRANSFER, creditTransfer.getUser());

                newTransactions.add(refund);
                Payment payment = PaymentLogic.processSingleOSalesPayment(amount, invoiceToPay, remark,
                        Enums.PaymentType.CREDIT_TRANSFER, creditTransfer.getUser());
                newTransactions.add(payment);
            }
        }
        if (newTransactions.size() > 0) {
            dao.saveBulk(newTransactions);
        }
    }

    public Payments findPaymentBySalesInvoice(Long invoice_id) {
        List<Payment> list = dao.findPaymentBySalesInvoice(invoice_id);
        for (Payment p : list) {
            Set<TicketingSalesAcDoc> paydocs = p.gettSalesAcDocuments();
            for (TicketingSalesAcDoc d : paydocs) {
                d.setTickets(null);
                d.setRelatedDocuments(null);
                d.setAdditionalChargeLines(null);
                d.getParent().setTickets(null);
                d.getParent().setRelatedDocuments(null);
                d.getParent().setAdditionalChargeLines(null);
            }
            p.settPurchaseAcDocuments(null);
            p.setoSalesAcDocuments(null);
        }
        Payments payments = new Payments();
        payments.setList(list);
        return payments;
    }

    public synchronized List<Payment> findTicketingPaymentHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType saleType) {
        List<Payment> payment_list = dao.findTicketingPaymentHistory(clienttype, clientid, from, to, saleType);

        for (Payment pay : payment_list) {

            if (Enums.SaleType.TKTSALES.equals(saleType)) {
                pay.setoSalesAcDocuments(null);
                pay.settPurchaseAcDocuments(null);
                for (TicketingSalesAcDoc doc : pay.gettSalesAcDocuments()) {
                    doc.setAdditionalChargeLines(null);
                    doc.setPayment(null);
                    doc.setTickets(null);
                    doc.setRelatedDocuments(null);
                    doc.getPnr().setTickets(null);
                    doc.getPnr().setRemarks(null);

                    if (doc.getParent() != null) {
                        doc.getParent().setAdditionalChargeLines(null);
                        doc.getParent().setPnr(null);
                        doc.getParent().setRelatedDocuments(null);
                        doc.getParent().setPayment(null);
                        doc.getParent().setParent(null);
                    }
                }
            } else if (Enums.SaleType.TKTPURCHASE.equals(saleType)) {
                pay.setoSalesAcDocuments(null);
                pay.settSalesAcDocuments(null);
                for (TicketingPurchaseAcDoc doc : pay.gettPurchaseAcDocuments()) {
                    doc.setAdditionalChargeLines(null);
                    doc.setPayment(null);
                    doc.setTickets(null);
                    doc.setRelatedDocuments(null);
                    doc.getPnr().setTickets(null);
                    doc.getPnr().setRemarks(null);

                    if (doc.getParent() != null) {
                        doc.getParent().setAdditionalChargeLines(null);
                        doc.getParent().setPnr(null);
                        doc.getParent().setRelatedDocuments(null);
                        doc.getParent().setPayment(null);
                        doc.getParent().setParent(null);
                    }
                }
            }
        }
        return payment_list;
    }

    public synchronized List<TransactionReceipt> findTicketingPaymentReceipts(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType saleType) {
        List<Payment> payment_list = findTicketingPaymentHistory(clienttype, clientid, from, to, saleType);
        List<TransactionReceipt> receipt_list = new ArrayList<>();

        for (Payment payment : payment_list) {
            TransactionReceipt receipt = new TransactionReceipt(payment);
            receipt_list.add(receipt);
        }

        return receipt_list;
    }

    public synchronized List<Payment> findOtherPaymentHistory(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType saleType) {
        List<Payment> payment_list = dao.findOtherPaymentHistory(clienttype, clientid, from, to, saleType);

        for (Payment pay : payment_list) {
            pay.settSalesAcDocuments(null);
            pay.settPurchaseAcDocuments(null);
            for (OtherSalesAcDoc doc : pay.getoSalesAcDocuments()) {
                doc.setAdditionalChargeLines(null);
                doc.setAccountingDocumentLines(null);
                doc.setPayment(null);
                doc.setRelatedDocuments(null);

                doc.getParent().setAccountingDocumentLines(null);
                doc.getParent().setAdditionalChargeLines(null);
                doc.getParent().setRelatedDocuments(null);
                doc.getParent().setPayment(null);
                doc.getParent().setParent(null);
            }
        }
        return payment_list;
    }

    public synchronized List<TransactionReceipt> findOtherPaymentReceipts(Enums.ClientType clienttype, Long clientid, Date from, Date to, Enums.SaleType saleType) {
        List<Payment> payment_list = findOtherPaymentHistory(clienttype, clientid, from, to, saleType);
        List<TransactionReceipt> receipt_list = new ArrayList<>();

        for (Payment payment : payment_list) {
            TransactionReceipt receipt = new TransactionReceipt(payment);
            receipt_list.add(receipt);
        }

        return receipt_list;
    }

    public List<Payment> findTicketingSalesCashBook(Date from, Date to, Long userId,
            Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {
        List<Payment> payment_list = dao.findTicketingSalesCashBook(from, to, userId, clienttype, clientid, paymentType);

        for (Payment pay : payment_list) {
            pay.settPurchaseAcDocuments(null);
            pay.setoSalesAcDocuments(null);
        }

        return payment_list;
    }

    public List<Payment> findTicketingPurchaseCashBook(Date from, Date to, Long userId,
            Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {
        List<Payment> payment_list = dao.findTicketingPurchaseCashBook(from, to, userId, clienttype, clientid, paymentType);

        for (Payment pay : payment_list) {
            pay.settSalesAcDocuments(null);
            pay.setoSalesAcDocuments(null);
        }

        return payment_list;
    }

    public List<Payment> findOtherSalesCashBook(Date from, Date to, Long userId,
            Enums.ClientType clienttype, Long clientid, Enums.PaymentType paymentType) {
        List<Payment> payment_list = dao.findOtherSalesCashBook(from, to, userId, clienttype, clientid, paymentType);

        for (Payment pay : payment_list) {
            pay.settPurchaseAcDocuments(null);
            pay.settSalesAcDocuments(null);
        }

        return payment_list;
    }

    public CashBookReport findCashBookReport(Date from, Date to, Long userid, Enums.ClientType clienttype,
            Long clientid, Enums.SaleType saleType, Enums.PaymentType paymentType) {
        List<Payment> payment_list = new ArrayList<>();

        if (saleType.equals(Enums.SaleType.TKTSALES)) {
            payment_list = findTicketingSalesCashBook(from, to, userid, clienttype, clientid, paymentType);
        } else if (saleType.equals(Enums.SaleType.OTHERSALES)) {
            payment_list = findOtherSalesCashBook(from, to, userid, clienttype, clientid, paymentType);
        } else if (saleType.equals(Enums.SaleType.TKTPURCHASE)) {
            payment_list = findTicketingPurchaseCashBook(from, to, userid, clienttype, clientid, paymentType);
        }

        CashBookReport report = CashBookReport.serializeToReport(payment_list, from, to);
        report.setTitle("Cash Book");
        return report;
    }
}
