package com.ets.air;

import com.ets.pnr.domain.Airline;
import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Remark;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import com.ets.util.Enums.TicketStatus;
import com.ets.pnr.logic.PnrUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Yusuf
 */
public class AIRToPNRConverter {

    private final AIR air;

    public AIRToPNRConverter(AIR air) {
        this.air = air;
    }

    public Pnr airToPNR() {
        Pnr pnr = new Pnr();

        for (String s : air.getLines()) {
            if (s.startsWith("MUC")) {
                String[] vals = AIRLineParser.parseMUCLine(s);
                pnr.setGdsPnr(vals[0].substring(0, 6));
                pnr.setNoOfPax(Integer.valueOf(vals[1].substring(0, 2)));
                pnr.setBookingAgtOid(vals[2]);
                pnr.setTicketingAgtOid(vals[8]);
                pnr.setVendorPNR(vals[vals.length - 1]);
                break;
            }
        }

        for (String s : air.getLines()) {
            if (s.startsWith("D-")) {
                String[] vals = AIRLineParser.parseDLine(s);
                pnr.setPnrCreationDate(DateUtil.yyMMddToDate(vals[0]));
                pnr.setAirCreationDate(DateUtil.yyMMddToDate(vals[2]));
                break;
            }
        }

        for (String s : air.getLines()) {
            if (s.startsWith("C-")) {
                String[] vals = AIRLineParser.parseCLine(s);
                pnr.setPnrCreatorAgentSine(vals[0]);
                pnr.setTicketingAgentSine(vals[1]);
                break;
            }
        }

        //This block is to get pnr cancellation date frm TKXL
        Set<Date> dates = new HashSet<>();
        for (String s : air.getLines()) {
            if (s.startsWith("TKXL")) {
                String[] data = AIRLineParser.parseTKLine(s);
                String date = data[0].substring(2);
                dates.add(DateUtil.ddmmToDate(date));
            }
        }
        if (dates.size() > 1) {
            Date date = PnrUtil.getEarliestDate(dates);
            pnr.setPnrCancellationDate(date);
        } else if (dates.size() == 1) {
            Date date = dates.iterator().next();
            pnr.setPnrCancellationDate(date);
        }

        if ("INV".equals(air.getType()) || "TTP/BTK".equals(air.getType())) {
            for (String s : air.getLines()) {
                if (s.startsWith("TK")) {
                    String[] data = AIRLineParser.parseTKLine(s);
                    String ticketingAgt = data[1];
                    pnr.setTicketingAgtOid(ticketingAgt);
                    break;
                }
            }
        }

        Airline airLine = airToCareer();
        if (airLine != null) {
            pnr.setAirLineCode(airLine.getCode());
        }

        return pnr;
    }

    public List<Remark> airToPNRRemarks() {

        List<Remark> remarks = new ArrayList<>();

        for (String s : air.getLines()) {
            if (s.startsWith("RM") && !s.contains("ROBOT")) {
                String[] vals = AIRLineParser.parseRMLine(s);
                Remark rm = new Remark();
                rm.setText(vals[0]);
                remarks.add(rm);
            }
        }
        return remarks;
    }

    public Airline airToCareer() {

        Airline career = null;

        String aLine = air.getALine();
        if (aLine != null && aLine.length() > 4) {
            career = new Airline();
            String[] vals = AIRLineParser.parseALine(aLine);
            career.setName(vals[0]);
            career.setCode(vals[1].substring(0, 3).trim());
        } else {
            String mucLine = air.getMUCLine();
            String[] vals = AIRLineParser.parseMUCLine(mucLine);
            String vendorPnr = vals[vals.length - 1];
            career = new Airline();
            career.setCode(vendorPnr.substring(0, 2));
        }

        return career;
    }

    public List<Itinerary> airToItinerary() {

        List<Itinerary> segments = new ArrayList<>();
        List<String[]> _Hs = new ArrayList<>();

        for (String s : air.getLines()) {
            if (s.startsWith("H-") || s.startsWith("U-")) {
                _Hs.add(AIRLineParser.parseHLine(s));
            } else if (s.startsWith("K-")) {
                break;//Finished H line reading, so break loop
            }
        }

        for (String[] _H : _Hs) {
            Itinerary segment = lineToIninerary(_H);
            if (segment.getDeptDate() != null) {//Invalid dept date means no confirmed segment
                segments.add(segment);
            }
        }

        return segments;
    }

    public List<Ticket> airToTicket() {
        List<Ticket> tickets = new ArrayList<>();

        String localCurrencyCode = null;
        String bfCurrencyCode = null;
        BigDecimal baseFare = new BigDecimal("0.00");
        BigDecimal totalFare = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
        Ticket ticket = null;

        for (String s : air.getLines()) {

            if (s.startsWith("K-") && s.length() > 4) {
                String[] data = AIRLineParser.parseKLine(s);

                localCurrencyCode = data[12].replaceAll("[^A-Z]", "");
                bfCurrencyCode = data[0].replaceAll("[^A-Z]", "").substring(1);

                totalFare = new BigDecimal(data[12].replaceAll("[a-zA-Z]", "").trim());

                if (totalFare.compareTo(BigDecimal.ONE) > 0) {
                    if (bfCurrencyCode.equals(localCurrencyCode)) {
                        baseFare = new BigDecimal((data[0].replaceAll("[a-zA-Z]", "").trim()));
                    } else {
                        if (!data[1].isEmpty()) {
                            String bf = data[1].replaceAll("[a-zA-Z]", "");
                            if (bf.isEmpty()) {
                                bf = "0.00";
                            }
                            baseFare = new BigDecimal(bf);
                        }
                    }
                }

                tax = totalFare.subtract(baseFare);

                if (tax.compareTo(BigDecimal.ONE) < 0) {
                    tax = new BigDecimal("0.00");
                }
            } else if (s.startsWith("KN-") && s.length() > 4) {

                if (totalFare.compareTo(new BigDecimal("0.00")) == 1) {
                    continue;
                }
                String[] data = AIRLineParser.parseKNLine(s);
                localCurrencyCode = data[12].replaceAll("[^A-Z]", "");
                bfCurrencyCode = data[0].replaceAll("[^A-Z]", "").substring(1);

                totalFare = new BigDecimal(data[12].replaceAll("[a-zA-Z]", "").trim());
                if (totalFare.compareTo(BigDecimal.ONE) > 0) {
                    if (bfCurrencyCode.equals(localCurrencyCode)) {
                        baseFare = new BigDecimal((data[0].replaceAll("[a-zA-Z]", "").trim()));
                    } else {
                        if (!data[1].isEmpty()) {
                            String bf = data[1].replaceAll("[a-zA-Z]", "");
                            if (bf.isEmpty()) {
                                bf = "0.00";
                            }
                            baseFare = new BigDecimal(bf);
                        }
                    }
                }

                tax = totalFare.subtract(baseFare);

                if (tax.compareTo(BigDecimal.ONE) < 0) {
                    tax = new BigDecimal("0.00");
                }
            } else if (s.startsWith("KS-") && s.length() > 4 && "INV".equals(air.getType())) {
                if (totalFare.compareTo(new BigDecimal("0.00")) == 1) {
                    continue;
                }
                //This block is only for thirdparty ticketing
                String[] data = AIRLineParser.parseKSLine(s);

                localCurrencyCode = data[12].replaceAll("[^A-Z]", "");
                bfCurrencyCode = data[0].replaceAll("[^A-Z]", "").substring(1);

                totalFare = new BigDecimal(data[12].replaceAll("[a-zA-Z]", "").trim());
                if (totalFare.compareTo(BigDecimal.ONE) > 0) {
                    if (bfCurrencyCode.equals(localCurrencyCode)) {
                        baseFare = new BigDecimal((data[0].replaceAll("[a-zA-Z]", "").trim()));
                    } else {
                        if (!data[1].isEmpty()) {
                            String bf = data[1].replaceAll("[a-zA-Z]", "");
                            if (bf.isEmpty()) {
                                bf = "0.00";
                            }
                            baseFare = new BigDecimal(bf);
                        }
                    }
                }

                tax = totalFare.subtract(baseFare);

                if (tax.compareTo(BigDecimal.ONE) < 0) {
                    tax = new BigDecimal("0.00");
                }
            } else if (s.startsWith("I-")) {
                String[] data = AIRLineParser.parseILine(s);
                String[] name = data[1].substring(2).trim().split("/");

                ticket = new Ticket();
                ticket.setTktStatus(TicketStatus.BOOK);//Setting default status; This will be overwridden after
                ticket.setDocIssuedate(DateUtil.yyMMddToDate(air.getCreationDate()));
                ticket = getNameFormStringArray(data, ticket);
                ticket.setBaseFare(baseFare);
                ticket.setTax(tax);
                ticket.setNetPurchaseFare(totalFare);
                ticket.setCurrencyCode(bfCurrencyCode);
                tickets.add(ticket);
            } else if (s.startsWith("T-") && s.length() > 4) {
                String[] data = AIRLineParser.parseTLine(s);
                if (data[0] != null && data[0].length() > 3) {
                    ticket.setNumericAirLineCode(data[0].substring(1, 4).trim());
                }

                if (data.length == 3) {
                    ticket.setTicketNo(data[1] + "-" + data[2]);
                } else {
                    ticket.setTicketNo(data[1]);
                }

                ticket.setTktStatus(TicketStatus.ISSUE);
            } else if (s.startsWith("FE")) {
                String restrictions = s.substring(2);
                ticket.setRestrictions(restrictions);
            } else if (s.startsWith("FO") && s.length() > 4) {
                String[] data = AIRLineParser.parseFOLine(s);

                if (s.charAt(5) == '-') {
                    ticket.setOrginalTicketNo(s.substring(6, 16));
                } else {
                    ticket.setOrginalTicketNo(s.substring(5, 15));
                }
                if (air.getType().equals("TTP")) {
                    ticket.setTktStatus(TicketStatus.REISSUE);
                }

            }
        }

        return tickets;
    }

    public List<Ticket> airToRefundedTicket() {
        List<Ticket> tickets = new ArrayList<>();

        BigDecimal baseFare = new BigDecimal("0.00");
        BigDecimal totalFare = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
        BigDecimal fee = new BigDecimal("0.00");
        Ticket ticket = null;

        for (String s : air.getLines()) {
            if (s.startsWith("RFD")) {
                String[] data = AIRLineParser.parseRFDLine(s);
                if (data[5] == null ? "" != null : !data[5].equals("")) {
                    baseFare = new BigDecimal(data[5]).negate();
                }

                if (data[8] == null ? "" != null : !data[8].equals("")) {
                    fee = new BigDecimal(data[8]);
                }

                if (data[11] == null ? "" != null : !data[11].equals("")) {
                    tax = new BigDecimal(data[11].substring(2)).negate();
                }

                if (data[12] == null ? "" != null : !data[12].equals("")) {
                    totalFare = new BigDecimal(data[12]).negate();
                }
            } else if (s.startsWith("I-")) {
                String[] data = AIRLineParser.parseILine(s);
                String[] name = data[1].substring(2).trim().split("/");

                ticket = new Ticket();
                ticket = getNameFormStringArray(data, ticket);
                ticket.setBaseFare(baseFare);
                ticket.setTax(tax);
                ticket.setFee(fee);
                ticket.setNetPurchaseFare(totalFare);
                tickets.add(ticket);
            } else if (s.startsWith("T-") && s.length() > 4) {
                String[] data = AIRLineParser.parseTLine(s);
                if (data[0] != null && data[0].length() > 3) {
                    ticket.setNumericAirLineCode(data[0].substring(1, 4).trim());
                }

                if (data.length == 3) {
                    ticket.setTicketNo(data[1] + "-" + data[2]);
                } else {
                    ticket.setTicketNo(data[1]);
                }

                ticket.setTktStatus(TicketStatus.REFUND);
            } else if (s.startsWith("R-")) {
                String[] data = AIRLineParser.parseRLine(s);
                ticket.setDocIssuedate(DateUtil.refundDate(data[1]));
            } else if (s.startsWith("FO") && s.length() > 4) {
                String[] data = AIRLineParser.parseFOLine(s);

                if (s.charAt(5) == '-') {
                    ticket.setOrginalTicketNo(s.substring(6, 16));
                } else {
                    ticket.setOrginalTicketNo(s.substring(5, 15));
                }
            }
        }

        return tickets;
    }

    private Ticket getNameFormStringArray(String[] data, Ticket ticket) {
        String[] name = data[1].substring(2).trim().split("/");
        String paxNumber = data[1].substring(0, 2);
        if (paxNumber != null) {
            ticket.setPassengerNo(Integer.valueOf(paxNumber));
        }
        ticket.setSurName(name[0].trim());
        ticket.setForeName(name[1].trim());
        return ticket;
    }

    public List<Ticket> airToVoidTicket() {
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = null;

        for (String s : air.getLines()) {
            if (s.startsWith("I-")) {
                String[] data = AIRLineParser.parseILine(s);
                ticket = new Ticket();
                ticket = getNameFormStringArray(data, ticket);
                tickets.add(ticket);
            } else if (s.startsWith("T-") && s.length() > 4) {
                String[] data = AIRLineParser.parseTLine(s);
                if (data[0] != null && data[0].length() > 3) {
                    ticket.setNumericAirLineCode(data[0].substring(1, 4).trim());
                }

                if (data.length == 3) {
                    ticket.setTicketNo(data[1] + "-" + data[2]);
                } else {
                    ticket.setTicketNo(data[1]);
                }

                ticket.setTktStatus(TicketStatus.VOID);
            }
        }
        return tickets;
    }

    private Itinerary lineToIninerary(String[] _H) {

        Itinerary segment = new Itinerary();
        String segmentNo = _H[1].substring(0, 3);
        if (segmentNo != null && !segmentNo.isEmpty()) {
            segment.setSegmentNo(Integer.valueOf(segmentNo));
        } else {
            segment.setSegmentNo(0);
        }

        segment.setStopOver(_H[1].substring(3, 4));
        segment.setDeptFrom(_H[1].substring(4).trim());
        segment.setDeptTo(_H[3].trim());
        String[] temp = _H[5].split(" ");
        segment.setAirLineCode(temp[0]);
        if (temp.length > 4) {
            segment.setFlightNo(temp[4]);
        }
        if (temp.length > 5) {
            segment.setTicketClass(temp[5]);
        }
        if (temp.length > 7) {
            segment.setDeptDate(DateUtil.ddmmToDate(temp[7].substring(0, 5)));
        }
        if (temp.length > 7) {
            segment.setDeptTime(temp[7].substring(5, 9));
        }
        if (temp.length > 9) {
            segment.setArvDate(DateUtil.ddmmToDate(temp[9]));
        }
        if (temp.length > 8) {
            segment.setArvTime(temp[8]);
        }
        if (_H.length > 6 && _H[6].length() > 2) {
            segment.setTktStatus(_H[6].substring(0, 2));
        }
        if (_H.length > 8) {
            segment.setMealCode(_H[8].trim());
        }
        if (_H.length > 13) {
            segment.setBaggage(_H[13]);
        }
        if (_H.length > 14) {
            segment.setCheckInTerminal(_H[14]);
        }
        if (_H.length > 15) {
            segment.setCheckInTime(_H[15]);
        }
        if (_H.length > 17) {
            segment.setFlightDuration(_H[17]);
        }
        if (_H.length > 19) {
            segment.setMileage(_H[19]);
        }
        return segment;
    }
}
