package com.ets.pnr.model;

import com.ets.client.domain.MainAgent;
import com.ets.pnr.domain.Itinerary;
import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ATOLCertificate {

    @XmlElement
    private String atolHolderName;
    @XmlElement
    private String atolNo;
    @XmlElement
    private String paxNames;
    @XmlElement
    private String segmentSummery;
    @XmlElement
    private String segments = "";
    @XmlElement
    private String atolHolderDetails;
    @XmlElement
    private int paxQty;
    @XmlElement
    private String pnr;
    @XmlElement
    private String totalCost;
    @XmlElement
    private String issuedate;

    public static ATOLCertificate serializeToCertificate(String issuedate, MainAgent mainAgent, Pnr pnr) {
        Set<Ticket> tickets = pnr.getTickets();
        Set<String> paxNames = new LinkedHashSet<>();
        StringBuilder sb = new StringBuilder();
        BigDecimal totalAtol = new BigDecimal("0.00");

        int tktNum = 0;
        for (Ticket t : tickets) {
            if (t.getAtolChg() != null && t.getAtolChg().compareTo(new BigDecimal("0.00")) > 0) {
                tktNum++;
                paxNames.add(t.getFullPaxName());
                totalAtol = totalAtol.add(t.getAtolChg());
                sb.append(t.getFullPaxName());

                if (tktNum < tickets.size()) {
                    sb.append(", ");
                }
            }
        }
        if (paxNames.isEmpty()) {
            return null;
        }

        ATOLCertificate cert = new ATOLCertificate();

        Set<Itinerary> segmentlist = pnr.getSegments();
        StringBuilder ib = new StringBuilder();
        for (Itinerary i : segmentlist) {
            ib.append(i.getDeptFrom()).append(" ")
                    .append(i.getDeptTo()).append(" ")
                    .append(DateUtil.dateTOddmm(i.getDeptDate())).append(" ")
                    .append(i.getAirLineCode());

            ib.append("\n");
        }
        cert.setSegmentSummery(ib.toString());
        cert.setAtolHolderName(mainAgent.getName());
        cert.setAtolNo(mainAgent.getAtol());
        cert.setAtolHolderDetails(mainAgent.getName() + " " + mainAgent.getAtol() + " " + pnr.getGdsPnr());
        cert.setPnr(pnr.getGdsPnr());
        cert.setPaxNames(sb.toString());

        Set<Itinerary> segmentList = pnr.getSegments();
        sb = new StringBuilder();
        int segNum = 0;
        for (Itinerary i : segmentList) {
            segNum++;
            sb.append(i.getDeptFrom()).append(" ")
                    .append(i.getDeptTo()).append(" ")
                    .append(DateUtil.dateToString(i.getDeptDate())).append(" ")
                    .append(i.getAirLineCode());

            if (segNum < segmentList.size()) {
                sb.append(", ");
            }
        }

        cert.setTotalCost(totalAtol.toString());
        cert.setSegments(sb.toString());
        cert.setPaxQty(paxNames.size());
        cert.setIssuedate(issuedate);
        return cert;
    }

    public String getAtolHolderName() {
        return atolHolderName;
    }

    public void setAtolHolderName(String atolHolderName) {
        this.atolHolderName = atolHolderName;
    }

    public String getAtolNo() {
        return atolNo;
    }

    public void setAtolNo(String atolNo) {
        this.atolNo = atolNo;
    }

    public String getAtolHolderDetails() {
        return atolHolderDetails;
    }

    public void setAtolHolderDetails(String atolHolderDetails) {
        this.atolHolderDetails = atolHolderDetails;
    }

    public int getPaxQty() {
        return paxQty;
    }

    public void setPaxQty(int paxQty) {
        this.paxQty = paxQty;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getPaxNames() {
        return paxNames;
    }

    public void setPaxNames(String paxNames) {
        this.paxNames = paxNames;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getSegmentSummery() {
        return segmentSummery;
    }

    public void setSegmentSummery(String segmentSummery) {
        this.segmentSummery = segmentSummery;
    }

    public String getSegments() {
        return segments;
    }

    public void setSegments(String segments) {
        this.segments = segments;
    }

}
