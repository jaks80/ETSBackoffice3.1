package com.ets.util;

import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Remark;
import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums.TicketStatus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Yusuf
 */
public class PnrUtil {

    public static void initPnrChildren(Pnr pnr) {

        PnrUtil.initPnrInRemark(pnr, pnr.getRemarks());
        PnrUtil.initPnrInTickets(pnr, pnr.getTickets());
        PnrUtil.initPnrInSegments(pnr, pnr.getSegments());

    }

    public static void undefineChildrenInPnr(Pnr pnr) {
        pnr.setTickets(null);
        pnr.setSegments(null);
        pnr.setRemarks(null);
        pnr.setCreatedBy(null);
        pnr.setLastModifiedBy(null);
    }

    public static void undefinePnrChildren(Pnr pnr) {
        if (pnr.getRemarks() != null && !pnr.getRemarks().isEmpty()) {
            PnrUtil.undefinePnrInRemark(pnr, pnr.getRemarks());
        }
        PnrUtil.undefinePnrInTickets(pnr, pnr.getTickets());
        PnrUtil.undefinePnrInSegments(pnr, pnr.getSegments());
    }

    public static Set<Remark> initPnrInRemark(Pnr pnr, Set<Remark> remarks) {

        for (Remark rm : remarks) {
            rm.setPnr(pnr);
        }
        return remarks;
    }

    public static Set<Remark> undefinePnrInRemark(Pnr pnr, Set<Remark> remarks) {
        for (Remark rm : remarks) {
            rm.setPnr(null);
        }
        return remarks;
    }

    public static Collection initPnrInTickets(Pnr pnr, Collection tickets) {
        Set<Ticket> tempTickets = new LinkedHashSet<>();
        for (Object object : tickets) {
            Ticket ticket = (Ticket) object;
            ticket.setPnr(pnr);
            tempTickets.add(ticket);
        }
        return tempTickets;
    }

    public static Set<Ticket> undefinePnrInTickets(Pnr pnr, Set<Ticket> tickets) {
        Set<Ticket> tempTickets = new LinkedHashSet<>();
        for (Ticket ticket : tickets) {
            ticket.setPnr(null);
            tempTickets.add(ticket);
        }
        return tempTickets;
    }

    public static List<Ticket> undefinePnrInTickets(Pnr pnr, List<Ticket> tickets) {
        List<Ticket> tempTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticket.setPnr(null);
            tempTickets.add(ticket);
        }
        return tempTickets;
    }

    public static Set<Itinerary> initPnrInSegments(Pnr pnr, Set<Itinerary> segments) {
        for (Itinerary segment : segments) {
            segment.setPnr(pnr);
        }
        return segments;
    }

    public static Set<Itinerary> undefinePnrInSegments(Pnr pnr, Set<Itinerary> segments) {
        for (Itinerary segment : segments) {
            segment.setPnr(null);
        }
        return segments;
    }

    public static Pnr updatePnr(Pnr oldPnr, Pnr newPnr) {
        oldPnr.setNoOfPax(newPnr.getNoOfPax());
        oldPnr.setBookingAgtOid(newPnr.getBookingAgtOid());
        oldPnr.setTicketingAgtOid(newPnr.getTicketingAgtOid());
        oldPnr.setTicketingAgentSine(newPnr.getTicketingAgentSine());
        oldPnr.setPnrCreatorAgentSine(newPnr.getPnrCreatorAgentSine());
        oldPnr.setPnrCreationDate(newPnr.getPnrCreationDate());
        oldPnr.setPnrCancellationDate(newPnr.getPnrCancellationDate());
        oldPnr.setVendorPNR(newPnr.getVendorPNR());
        oldPnr.setAirLineCode(newPnr.getAirLineCode());
        oldPnr.setRemarks(initPnrInRemark(oldPnr, newPnr.getRemarks()));
        return oldPnr;
    }

    public static Set<Ticket> updateTickets(Collection oldCollection, Collection newCollection) {

        Set<Ticket> oldTickets = new LinkedHashSet<>();
        Set<Ticket> newTickets = new LinkedHashSet<>();

        if (oldCollection instanceof ArrayList) {
            oldTickets = new LinkedHashSet<>(oldCollection);
        } else {
            oldTickets = (Set<Ticket>) oldCollection;
        }

        if (newCollection instanceof ArrayList) {
            newTickets = new LinkedHashSet<>(oldCollection);
        } else {
            newTickets = (Set<Ticket>) newCollection;
        }

        for (Ticket newTkt : newTickets) {
            boolean exist = false;

            for (Ticket t : oldTickets) {
                if ((newTkt.getTicketNo() == null ? t.getTicketNo() == null : newTkt.getTicketNo().equals(t.getTicketNo()))
                        && newTkt.getTktStatus() == t.getTktStatus()
                        && t.getSurName().equals(newTkt.getSurName())) {
                    exist = true;
                    break;
                } else if ((t.getTktStatus() == TicketStatus.BOOK || t.getTktStatus() == TicketStatus.VOID)
                        && t.getSurName().equals(newTkt.getSurName())) {

                    t.setForeName(newTkt.getForeName());
                    t.setTktStatus(newTkt.getTktStatus());
                    t.setNumericAirLineCode(newTkt.getNumericAirLineCode());
                    t.setTicketNo(newTkt.getTicketNo());
                    t.setBaseFare(newTkt.getBaseFare());
                    t.setTax(newTkt.getTax());
                    t.setDocIssuedate(newTkt.getDocIssuedate());
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                oldTickets.add(newTkt);
            }
        }
        return oldTickets;
    }

    public static Set<Itinerary> updateSegments(Set<Itinerary> oldSegs, Set<Itinerary> newSegs) {
        Set<Itinerary> finalSegments = new LinkedHashSet();
        Itinerary tempOSeg = new Itinerary();

        for (Itinerary newSeg : newSegs) {
            boolean exist = false;
            loop:
            for (Itinerary oldSeg : oldSegs) {
                tempOSeg = new Itinerary();
                tempOSeg = oldSeg;
                if (oldSeg.getSegmentNo() == newSeg.getSegmentNo()) {
                    exist = true;
                    break loop;
                }
            }
            if (exist) {
                finalSegments.add(tempOSeg);
            } else {
                finalSegments.add(newSeg);
            }
        }
        return finalSegments;
    }

    public static Set<Ticket> getUnRefundedTickets(Pnr pnr, Enums.SaleType saleType) {
        Set<Ticket> refundTickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {

            if (t.getTktStatus().equals(Enums.TicketStatus.REFUND)
                    && saleType.equals(Enums.SaleType.TKTSALES)
                    && t.getTicketingSalesAcDoc() == null
                    && t.getGrossFare().compareTo(new BigDecimal("0.00")) != 0) {
                refundTickets.add(t);
            } else if (t.getTktStatus().equals(Enums.TicketStatus.REFUND)
                    && saleType.equals(Enums.SaleType.TKTPURCHASE)
                    && t.getTicketingPurchaseAcDoc() == null) {
                refundTickets.add(t);
            }

            if (t.getTktStatus().equals(Enums.TicketStatus.REFUND) && t.getTicketingSalesAcDoc() == null && t.getGrossFare().compareTo(new BigDecimal("0.00")) != 0) {
                refundTickets.add(t);
            }
        }
        return refundTickets;
    }

    public static Set<Ticket> getUnInvoicedBookedTicket(Set<Ticket> tickets, Enums.SaleType saleType) {

        Set<Ticket> bookedTickets = new LinkedHashSet<>();

        for (Ticket t : tickets) {
            if (t.getTktStatus().equals(Enums.TicketStatus.BOOK)) {

                if (saleType.equals(Enums.SaleType.TKTSALES) && t.getTicketingSalesAcDoc() == null) {
                    bookedTickets.add(t);
                } else if (saleType.equals(Enums.SaleType.TKTPURCHASE) && t.getTicketingPurchaseAcDoc() == null) {
                    bookedTickets.add(t);
                }
            }
        }
        return bookedTickets;
    }

    public static Set<Ticket> getUnInvoicedIssuedTicket(Pnr pnr, Enums.SaleType saleType) {
        Set<Ticket> tickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if (t.getTktStatus().equals(Enums.TicketStatus.ISSUE)) {

                if (saleType.equals(Enums.SaleType.TKTSALES) && t.getTicketingSalesAcDoc() == null) {
                    tickets.add(t);
                } else if (saleType.equals(Enums.SaleType.TKTPURCHASE) && t.getTicketingPurchaseAcDoc() == null) {
                    tickets.add(t);
                }
            }
        }
        return tickets;
    }

    public static Set<Ticket> getUnInvoicedVoidTicket(Pnr pnr, Enums.SaleType saleType) {
        Set<Ticket> tickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if (t.getTktStatus().equals(Enums.TicketStatus.VOID)) {

                if (saleType.equals(Enums.SaleType.TKTSALES) && t.getTicketingSalesAcDoc() == null) {
                    tickets.add(t);
                } else if (saleType.equals(Enums.SaleType.TKTPURCHASE) && t.getTicketingPurchaseAcDoc() == null) {
                    tickets.add(t);
                }
            }
        }
        return tickets;
    }
    
    public static Set<Ticket> getUnInvoicedTicket(Pnr pnr) {
        Set<Ticket> tickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if (!t.getTktStatus().equals(Enums.TicketStatus.REFUND)
                    && !t.getTktStatus().equals(Enums.TicketStatus.VOID)
                    && t.getTicketingSalesAcDoc() == null) {
                tickets.add(t);
            }
        }
        return tickets;
    }

    public static Set<Ticket> getPUnInvoicedTicket(Pnr pnr) {
        Set<Ticket> tickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if (!t.getTktStatus().equals(Enums.TicketStatus.REFUND)
                    && !t.getTktStatus().equals(Enums.TicketStatus.BOOK)
                    && !t.getTktStatus().equals(Enums.TicketStatus.VOID)
                    && t.getTicketingPurchaseAcDoc() == null) {
                tickets.add(t);
            }
        }
        return tickets;
    }

    public static Set<Ticket> getUnInvoicedReIssuedTicket(Pnr pnr, Enums.SaleType saleType) {
        Set<Ticket> tickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if (t.getTktStatus().equals(Enums.TicketStatus.REISSUE)) {

                if (saleType.equals(Enums.SaleType.TKTSALES) && t.getTicketingSalesAcDoc() == null) {
                    tickets.add(t);
                } else if (saleType.equals(Enums.SaleType.TKTPURCHASE) && t.getTicketingPurchaseAcDoc() == null) {
                    tickets.add(t);
                }
            }
        }
        return tickets;
    }

    public static Set<Ticket> getIssuedInvoicedTickets(Pnr pnr) {
        Set<Ticket> invoicedTickets = new LinkedHashSet<>();

        for (Ticket t : pnr.getTickets()) {
            if ((t.getTktStatus().equals(Enums.TicketStatus.ISSUE)
                    || t.getTktStatus().equals(Enums.TicketStatus.REISSUE))
                    && t.getTicketingSalesAcDoc() != null) {
                invoicedTickets.add(t);
            }
        }
        return invoicedTickets;
    }

    public static String getOutBoundFlightSummery(Set<Itinerary> segments) {
        StringBuilder sb = new StringBuilder();

        Itinerary seg = segments.iterator().next();
        sb.append(DateUtil.dateTOddmm(seg.getDeptDate())).append("/");
        sb.append(seg.getDeptFrom()).append("-").append(seg.getDeptTo()).append("/");
        sb.append(seg.getAirLineCode());

        return sb.toString();
    }

    public static Itinerary getFirstSegment(Set<Itinerary> segments) {
        Itinerary firstSegment = null;
        Long index = null;

        for (Itinerary i : segments) {
            if (index == null) {
                index = i.getId();
                firstSegment = i;
            } else {
                if (i.getId() < index) {
                    index = i.getId();
                    firstSegment = i;
                }
            }

        }
        return firstSegment;
    }

    public static Ticket calculateLeadPaxTicket(Set<Ticket> ticket_list) {
        Ticket leadPax = null;
        int paxNo = 99;

        for (Ticket t : ticket_list) {
            if (t.getPassengerNo() <= paxNo && (!t.isChild() && !t.isInfant())) {
                leadPax = t;
                paxNo = t.getPassengerNo();
            }
        }
        if (leadPax != null) {
            return leadPax;
        } else {
            Iterator<Ticket> iterator = ticket_list.iterator();
            Ticket setElement = new Ticket();
            while (iterator.hasNext()) {
                setElement = iterator.next();
                break;
            }
            return setElement;
        }
    }

    public static String calculateLeadPaxName(Set<Ticket> ticket_list) {

        Ticket ticket = calculateLeadPaxTicket(ticket_list);
        return ticket.getFullPaxName();
    }

    public static String calculatePartialName(String name) {
        if (name.length() < 10) {
            return name;
        } else {
            return name.substring(0, 10);
        }
    }

    public static Date getEarliestDate(Set<Date> dates) {

        Date date = new java.util.Date();
        for (Date t : dates) {
            if (t.before(date)) {
                date = t;
            }
        }
        return date;
    }
}
