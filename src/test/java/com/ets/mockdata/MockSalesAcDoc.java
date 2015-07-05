package com.ets.mockdata;

import com.ets.accounts.domain.Payment;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.util.Enums;
import java.math.BigDecimal;

/**
 *
 * @author Yusuf
 */
public class MockSalesAcDoc {

    private TicketingSalesAcDoc invoice ;
    private Payment payment;
    private TicketingSalesAcDoc creditNote;
    private TicketingSalesAcDoc debitNote;
    private TicketingSalesAcDoc refund;

    public MockSalesAcDoc(){
     this.invoice = createMockInvoice();
     this.payment = createMockPayment();
     this.creditNote = createMockCreditNote();
    }

    private TicketingSalesAcDoc createMockInvoice() {

        TicketingSalesAcDoc invoice = new TicketingSalesAcDoc();
        invoice.setType(Enums.AcDocType.INVOICE);
        invoice.setStatus(Enums.AcDocStatus.ACTIVE);
        invoice.setTerms("Net Monthly");                   

        return invoice;
    }
    
    private TicketingSalesAcDoc createMockCreditNote() {

        TicketingSalesAcDoc cnote = new TicketingSalesAcDoc();
        cnote.setType(Enums.AcDocType.CREDITMEMO);
        invoice.setStatus(Enums.AcDocStatus.ACTIVE);   

        return cnote;
    }
        
    private Payment createMockPayment() {

        Payment payment = new Payment();
        payment.setRemark("Cash Payment by Abdur Rahman");
        payment.setPaymentType(Enums.PaymentType.CASH);

        TicketingSalesAcDoc paymentDoc = new TicketingSalesAcDoc();
        paymentDoc.setType(Enums.AcDocType.PAYMENT);
        invoice.setStatus(Enums.AcDocStatus.ACTIVE);
        paymentDoc.setDocumentedAmount(new BigDecimal("200.00"));       

        return payment;
    }

    public TicketingSalesAcDoc getInvoice() {
        return invoice;
    }

    public Payment getPayment() {
        return payment;
    }

    public TicketingSalesAcDoc getCreditNote() {
        return creditNote;
    }

    public TicketingSalesAcDoc getDebitNote() {
        return debitNote;
    }

    public TicketingSalesAcDoc getRefund() {
        return refund;
    }

}
