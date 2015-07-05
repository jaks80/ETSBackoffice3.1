package com.ets.accountingdoc.logic;

import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Ticketing accounting document business logic. Invoice date should be
 * ticketing date. For multiple Ticket Issue date there should be multiple
 * Invoice. Ticket batch is sorted according to date.
 *
 * @author Yusuf
 */
public class TicketingAcDocBL {

    private Pnr pnr;

    public TicketingAcDocBL(Pnr pnr) {
        this.pnr = pnr;
    }

    public TicketingSalesAcDoc newTicketingDraftInvoice(TicketingSalesAcDoc invoice, Set<Ticket> unDocumentedTickets) {

        invoice.setType(Enums.AcDocType.INVOICE);
        invoice.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        Set<Ticket> ticketsToInv = new LinkedHashSet<>();
        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingSalesAcDoc() == null && invoice.getDocIssueDate().equals(t.getDocIssuedate())) {
                ticketsToInv.add(t);
            }
        }

        if (ticketsToInv.isEmpty()) {
            return null;
        }
        unDocumentedTickets.removeAll(ticketsToInv);
        invoice.setTickets(ticketsToInv);
        invoice.setPnr(pnr);
        return invoice;
    }

    public TicketingSalesAcDoc newTicketingDraftDMemo(TicketingSalesAcDoc invoice, Set<Ticket> unDocumentedTickets) {

        TicketingSalesAcDoc debitMemo = new TicketingSalesAcDoc();
        debitMemo.setType(Enums.AcDocType.DEBITMEMO);
        debitMemo.setPnr(pnr);
        debitMemo.setReference(invoice.getReference());
        debitMemo.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        debitMemo.setParent(invoice);
        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingSalesAcDoc() == null && debitMemo.getDocIssueDate().equals(t.getDocIssuedate())) {
                debitMemo.addTicket(t);
            }
        }

        return debitMemo;
    }

    public TicketingSalesAcDoc newTicketingDraftCMemo(TicketingSalesAcDoc invoice, Set<Ticket> unDocumentedTickets) {

        TicketingSalesAcDoc creditMemo = new TicketingSalesAcDoc();
        creditMemo.setType(Enums.AcDocType.CREDITMEMO);
        creditMemo.setPnr(pnr);
        creditMemo.setReference(invoice.getReference());
        creditMemo.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        creditMemo.setParent(invoice);

        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingSalesAcDoc() == null && creditMemo.getDocIssueDate().equals(t.getDocIssuedate())) {
                creditMemo.addTicket(t);
            }
        }

        return creditMemo;
    }

    public TicketingPurchaseAcDoc newTicketingPurchaseInvoice(TicketingSalesAcDoc salesInvoice,
            TicketingPurchaseAcDoc pinvoice) {
        
        pinvoice.setType(salesInvoice.getType());
        pinvoice.setDocIssueDate(salesInvoice.getDocIssueDate());
        pinvoice.setTickets(salesInvoice.getTickets());
        pinvoice.setReference(salesInvoice.getReference());
        pinvoice.setPnr(pnr);
        return pinvoice;
    }

    public TicketingPurchaseAcDoc newTicketingDraftPurchaseInvoice(TicketingPurchaseAcDoc draftInvoice, Set<Ticket> unDocumentedTickets) {

        draftInvoice.setType(Enums.AcDocType.INVOICE);
        draftInvoice.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        Set<Ticket> ticketsToInv = new LinkedHashSet<>();
        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingPurchaseAcDoc() == null && draftInvoice.getDocIssueDate().equals(t.getDocIssuedate())) {
                ticketsToInv.add(t);
            }
        }

        if (ticketsToInv.isEmpty()) {
            return null;
        }
        unDocumentedTickets.removeAll(ticketsToInv);
        draftInvoice.setTickets(ticketsToInv);
        draftInvoice.setPnr(pnr);
        return draftInvoice;
    }

    public TicketingPurchaseAcDoc newTicketingDraftPurchaseDMemo(TicketingPurchaseAcDoc invoice, Set<Ticket> unDocumentedTickets) {

        TicketingPurchaseAcDoc debitMemo = new TicketingPurchaseAcDoc();
        debitMemo.setType(Enums.AcDocType.DEBITMEMO);
        debitMemo.setPnr(pnr);
        debitMemo.setReference(invoice.getReference());
        debitMemo.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        debitMemo.setParent(invoice);
        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingPurchaseAcDoc()== null && debitMemo.getDocIssueDate().equals(t.getDocIssuedate())) {
                debitMemo.addTicket(t);
            }
        }

        return debitMemo;
    }

    public TicketingPurchaseAcDoc newTicketingDraftPurchaseCMemo(TicketingPurchaseAcDoc invoice, Set<Ticket> unDocumentedTickets) {

        TicketingPurchaseAcDoc creditMemo = new TicketingPurchaseAcDoc();
        creditMemo.setType(Enums.AcDocType.CREDITMEMO);
        creditMemo.setPnr(pnr);
        creditMemo.setReference(invoice.getReference());
        creditMemo.setDocIssueDate(getEarliestDate(unDocumentedTickets));
        creditMemo.setParent(invoice);

        for (Ticket t : unDocumentedTickets) {
            if (t.getTicketingPurchaseAcDoc() == null && creditMemo.getDocIssueDate().equals(t.getDocIssuedate())) {
                creditMemo.addTicket(t);
            }
        }

        return creditMemo;
    }

    public TicketingPurchaseAcDoc newTicketingPurchaseDMemo(TicketingSalesAcDoc salesDebitMemo, TicketingPurchaseAcDoc invoice) {

        TicketingPurchaseAcDoc debitMemo = new TicketingPurchaseAcDoc();
        debitMemo.setType(Enums.AcDocType.DEBITMEMO);
        debitMemo.setPnr(pnr);
        debitMemo.setReference(invoice.getReference());
        debitMemo.setDocIssueDate(salesDebitMemo.getDocIssueDate());
        debitMemo.setParent(invoice);
        debitMemo.setTickets(salesDebitMemo.getTickets());
        return debitMemo;
    }

    public TicketingPurchaseAcDoc newTicketingPurchaseCMemo(TicketingSalesAcDoc salesCreditMemo, TicketingPurchaseAcDoc invoice) {

        TicketingPurchaseAcDoc creditMemo = new TicketingPurchaseAcDoc();
        creditMemo.setType(Enums.AcDocType.CREDITMEMO);
        creditMemo.setPnr(pnr);
        creditMemo.setReference(invoice.getReference());
        creditMemo.setDocIssueDate(salesCreditMemo.getDocIssueDate());
        creditMemo.setParent(invoice);
        creditMemo.setTickets(salesCreditMemo.getTickets());
        return creditMemo;
    }

    public Date getEarliestDate(Set<Ticket> tickets) {

        Date date = new java.util.Date();
        for (Ticket t : tickets) {
            if (t.getDocIssuedate().before(date)) {
                date = t.getDocIssuedate();
            }
        }
        return date;
    }
}
