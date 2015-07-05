package com.ets.accountingdoc.service;

import com.ets.accountingdoc.domain.*;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Yusuf
 */
public class AcDocUtil {

    public static List<TicketingSalesAcDoc> getVoidSalesInvoices(List<TicketingSalesAcDoc> invoices) {
        List<TicketingSalesAcDoc> filteredDocs = new ArrayList<>();

        for (TicketingSalesAcDoc d : invoices) {
            if (d.getStatus().equals(Enums.AcDocStatus.VOID)) {
                filteredDocs.add(d);
            }
        }
        return filteredDocs;
    }
        
    public static Set<TicketingSalesAcDoc> filterVoidRelatedDocuments(Set<TicketingSalesAcDoc> relatedDocuments) {
        Set<TicketingSalesAcDoc> filteredDocs = new LinkedHashSet<>();

        for (TicketingSalesAcDoc d : relatedDocuments) {
            if (!d.getStatus().equals(Enums.AcDocStatus.VOID)) {
                filteredDocs.add(d);
            }
        }
        return filteredDocs;
    }

    public static Set<OtherSalesAcDoc> filterVoidRelatedDocumentsOther(Set<OtherSalesAcDoc> relatedDocuments) {
        Set<OtherSalesAcDoc> filteredDocs = new LinkedHashSet<>();

        for (OtherSalesAcDoc d : relatedDocuments) {
            if (!d.getStatus().equals(Enums.AcDocStatus.VOID)) {
                filteredDocs.add(d);
            }
        }
        return filteredDocs;
    }
        
    public static Set<TicketingPurchaseAcDoc> filterPVoidRelatedDocuments(Set<TicketingPurchaseAcDoc> relatedDocuments) {
        Set<TicketingPurchaseAcDoc> filteredDocs = new LinkedHashSet<>();

        for (TicketingPurchaseAcDoc d : relatedDocuments) {
            if (!d.getStatus().equals(Enums.AcDocStatus.VOID)) {
                filteredDocs.add(d);
            }
        }
        return filteredDocs;
    }

    public static void undefineTSAcDocumentInPayment(TicketingSalesAcDoc a) {
        if (a.getPayment() != null) {
            a.getPayment().settSalesAcDocuments(null);
            a.getPayment().settPurchaseAcDocuments(null);
            a.getPayment().setoSalesAcDocuments(null);
        }
    }

    public static void undefineTPAcDocumentInPayment(TicketingPurchaseAcDoc a) {
        if (a.getPayment() != null) {
            a.getPayment().settSalesAcDocuments(null);
            a.getPayment().settPurchaseAcDocuments(null);
            a.getPayment().setoSalesAcDocuments(null);
        }
    }

     public static void undefineOAcDocumentInPayment(OtherSalesAcDoc a) {
        if (a.getPayment() != null) {
            a.getPayment().settSalesAcDocuments(null);
            a.getPayment().settPurchaseAcDocuments(null);
            a.getPayment().setoSalesAcDocuments(null);
        }
    }
        
    public static Long generateAcDocRef(Long lastInvRef) {
        if (lastInvRef != null) {
            return ++lastInvRef;
        } else {
            return Long.valueOf("1001");
        }
    }

    public static void initTSAcDocInTickets(TicketingSalesAcDoc doc, Set<Ticket> tickets) {        
        for (Ticket t : tickets) {
            t.setTicketingSalesAcDoc(doc);
        }       
    }

    public static void initTPAcDocInTickets(TicketingPurchaseAcDoc doc, Set<Ticket> tickets) {       
        for (Ticket t : tickets) {
            t.setTicketingPurchaseAcDoc(doc);
        }
    }

    public static void initAcDocInLine(OtherSalesAcDoc doc, Set<AccountingDocumentLine> lines) {        
        for (AccountingDocumentLine line : lines) {
            line.setOtherSalesAcDoc((OtherSalesAcDoc) doc);
        }
    }

    public static void UndefineAcDocInLine(OtherSalesAcDoc doc, Set<AccountingDocumentLine> lines) {        
        for (AccountingDocumentLine line : lines) {
            line.setOtherSalesAcDoc(null);
        }        
    }

    public static void initAddChgLine(AccountingDocument doc, Set<AdditionalChargeLine> lines) {        
        for (AdditionalChargeLine line : lines) {
            if (doc instanceof TicketingSalesAcDoc) {
                line.setTicketingSalesAcDoc((TicketingSalesAcDoc) doc);
            } else if (doc instanceof TicketingPurchaseAcDoc) {
                line.setTicketingPurchaseAcDoc((TicketingPurchaseAcDoc) doc);
            } else if (doc instanceof OtherSalesAcDoc) {
                line.setOtherSalesAcDoc((OtherSalesAcDoc) doc);
            }
        }       
    }

    public static void initAcDocInLine(AccountingDocument doc, Set<AccountingDocumentLine> lines) {              
        for (AccountingDocumentLine line : lines) {
            if (doc instanceof TicketingSalesAcDoc) {
                //line.setTicketingSalesAcDoc((TicketingSalesAcDoc) doc);
            } else if (doc instanceof OtherSalesAcDoc) {
                line.setOtherSalesAcDoc((OtherSalesAcDoc) doc);
            }
        }
    }
        
    public static void undefineAcDocInLine(AccountingDocument doc, Set<AccountingDocumentLine> lines) {        
        for (AccountingDocumentLine line : lines) {
            if (doc instanceof TicketingSalesAcDoc) {
                //line.setTicketingSalesAcDoc(null);
            } else if (doc instanceof OtherSalesAcDoc) {
                line.setOtherSalesAcDoc(null);
            }
        }
    }
        
    public static Set<AdditionalChargeLine> undefineAddChgLine(AccountingDocument doc, Set<AdditionalChargeLine> lines) {
        Set<AdditionalChargeLine> tempLines = new LinkedHashSet<>();
        for (AdditionalChargeLine line : lines) {
            if (doc instanceof TicketingSalesAcDoc) {
                line.setTicketingSalesAcDoc(null);
            } else if (doc instanceof TicketingPurchaseAcDoc) {
                line.setTicketingPurchaseAcDoc(null);
            } else if (doc instanceof OtherSalesAcDoc) {
                line.setOtherSalesAcDoc(null);
            }
        }
        return tempLines;
    }

    public static Set<Ticket> undefineTSAcDoc(TicketingSalesAcDoc doc, Set<Ticket> tickets) {
        Set<Ticket> tempTickets = new LinkedHashSet<>();
        for (Ticket ticket : tickets) {
            ticket.setTicketingSalesAcDoc(null);
            tempTickets.add(ticket);
        }
        return tempTickets;
    }

    public static Set<Ticket> undefineTPAcDoc(TicketingPurchaseAcDoc doc, Set<Ticket> tickets) {
        Set<Ticket> tempTickets = new LinkedHashSet<>();
        for (Ticket ticket : tickets) {
            ticket.setTicketingPurchaseAcDoc(null);
            tempTickets.add(ticket);
        }
        return tempTickets;
    }
}
