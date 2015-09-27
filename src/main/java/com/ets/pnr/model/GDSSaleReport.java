package com.ets.pnr.model;

import com.ets.pnr.domain.Ticket;
import com.ets.util.Enums.TicketStatus;
import com.ets.pnr.logic.PnrUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class GDSSaleReport {

    @XmlElement
    private List<Ticket> list = new ArrayList<>();
    @XmlElement
    private BigDecimal totalIssuedFare;
    @XmlElement
    private BigDecimal totalRefundFare;
    @XmlElement
    private BigDecimal totalTax;
    @XmlElement
    private BigDecimal totalTaxRefund;
    @XmlElement
    private BigDecimal totalBSPCom;
    @XmlElement
    private BigDecimal totalBSPComRefund;
    @XmlElement
    private BigDecimal saleBalance;
    @XmlElement
    private BigDecimal refundBalance;
    @XmlElement
    private BigDecimal comBalance;
    @XmlElement
    private int totalBooking = 0;
    @XmlElement
    private int totalIssue = 0;
    @XmlElement
    private int totalReIssue = 0;
    @XmlElement
    private int totalVoid = 0;
    @XmlElement
    private int totalRefund = 0;

    @XmlElement
    private String summery;

    public GDSSaleReport() {

    }

    public GDSSaleReport(List<Ticket> list) {
        this.saleBalance = new BigDecimal("0.00");
        this.totalBSPComRefund = new BigDecimal("0.00");
        this.totalBSPCom = new BigDecimal("0.00");
        this.totalTaxRefund = new BigDecimal("0.00");
        this.totalRefundFare = new BigDecimal("0.00");
        this.totalTax = new BigDecimal("0.00");
        this.totalIssuedFare = new BigDecimal("0.00");
        this.refundBalance = new BigDecimal("0.00");
        this.comBalance = new BigDecimal("0.00");

        for (Ticket t : list) {
            t.setCreatedBy(null);
            t.setLastModified(null);
            PnrUtil.undefineChildrenInPnr(t.getPnr());
        }

        this.list = list;
        generate();
    }

    public void generate() {

        for (Ticket t : list) {
            if (t.getTktStatus() == TicketStatus.ISSUE || t.getTktStatus() == TicketStatus.REISSUE) {
                totalIssuedFare = totalIssuedFare.add(t.getBaseFare());
                totalTax = totalTax.add(t.getTax());
                totalBSPCom = totalBSPCom.add(t.getCommission());
                if (t.getTktStatus() == TicketStatus.ISSUE) {
                    ++totalIssue;
                } else if (t.getTktStatus() == TicketStatus.REISSUE) {
                    ++totalReIssue;
                }
            } else if (t.getTktStatus() == TicketStatus.REFUND) {
                totalRefundFare = totalRefundFare.add(t.getBaseFare()).add(t.getFee());
                totalTaxRefund = totalTaxRefund.add(t.getTax());
                totalBSPComRefund = totalBSPComRefund.add(t.getCommission());
                ++totalRefund;
            } else if (t.getTktStatus() == TicketStatus.BOOK) {
                ++totalBooking;
            } else if (t.getTktStatus() == TicketStatus.VOID) {
                ++totalVoid;
            }
        }

        saleBalance = totalIssuedFare.add(totalTax).add(totalBSPCom);
        refundBalance = totalRefundFare.add(totalTaxRefund).add(totalBSPComRefund);
        comBalance = totalBSPCom.add(totalBSPComRefund);

        Object[][] sSummery = new Object[6][4];//[row][col]
        sSummery[0][0] = "Fare";
        sSummery[1][0] = "Tax";
        sSummery[2][0] = "Doc Amount";
        sSummery[3][0] = "Com";
        //sSummery[4][0] = "Fee";
        sSummery[4][0] = "Remit";

        sSummery[0][1] = totalIssuedFare;
        sSummery[0][2] = totalRefundFare;
        sSummery[0][3] = totalIssuedFare.add(totalRefundFare);

        sSummery[1][1] = totalTax;
        sSummery[1][2] = totalTaxRefund;
        sSummery[1][3] = totalTax.add(totalTaxRefund);

        sSummery[2][1] = totalIssuedFare.add(totalTax);
        sSummery[2][2] = totalRefundFare.add(totalTaxRefund);
        sSummery[2][3] = null;

        sSummery[3][1] = totalBSPCom;
        sSummery[3][2] = totalBSPComRefund;
        sSummery[3][3] = comBalance;

        sSummery[4][1] = saleBalance;
        sSummery[4][2] = refundBalance;
        sSummery[4][3] = saleBalance.add(refundBalance);

        String html = "<html>"
                + "<table width=\"100%\" height=\"120\" border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"1\">"
                + "<tr>"
                + "<td width=\"126\" align=\"right\" valign=\"middle\">&nbsp;</td>"
                + "<td width=\"64\" align=\"right\" valign=\"middle\">Sales</span></td>"
                + "<td width=\"67\" align=\"right\" valign=\"middle\">Refund</span></td>"
                + "<td width=\"82\" align=\"right\" valign=\"middle\">Balance</span></td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"right\" valign=\"middle\">Fare</span></td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[0][1] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[0][2] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[0][3] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"right\" valign=\"middle\">Tax</span></td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[1][1] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[1][2] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[1][3] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"right\" valign=\"middle\">Doc Amount</span></td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[2][1] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[2][2] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[2][3] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"right\" valign=\"middle\">Com</span></td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[3][1] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[3][2] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[3][3] + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td align=\"right\" valign=\"middle\">Remit</span></td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[4][1] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[4][2] + "</td>"
                + "<td align=\"right\" valign=\"middle\">" + sSummery[4][3] + "</td>"
                + "</tr>"
                + "</table>"
                + "</html>";

        summery = html;
    }

    public List<Ticket> getList() {
        return list;
    }

    public void setList(List<Ticket> list) {
        this.list = list;
    }

    public BigDecimal getTotalIssuedFare() {
        return totalIssuedFare;
    }

    public BigDecimal getTotalRefundFare() {
        return totalRefundFare;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public BigDecimal getTotalTaxRefund() {
        return totalTaxRefund;
    }

    public BigDecimal getTotalBSPCom() {
        return totalBSPCom;
    }

    public BigDecimal getTotalBSPComRefund() {
        return totalBSPComRefund;
    }

    public BigDecimal getSaleBalance() {
        return saleBalance;
    }

    public BigDecimal getRefundBalance() {
        return refundBalance;
    }

    public BigDecimal getComBalance() {
        return comBalance;
    }

    public int getTotalIssue() {
        return totalIssue;
    }

    public int getTotalRefund() {
        return totalRefund;
    }

    public String getSummery() {
        return summery;
    }

    public int getTotalBooking() {
        return totalBooking;
    }

    public void setTotalBooking(int totalBooking) {
        this.totalBooking = totalBooking;
    }

    public void setTotalIssue(int totalIssue) {
        this.totalIssue = totalIssue;
    }

    public int getTotalReIssue() {
        return totalReIssue;
    }

    public void setTotalReIssue(int totalReIssue) {
        this.totalReIssue = totalReIssue;
    }

    public int getTotalVoid() {
        return totalVoid;
    }

    public void setTotalVoid(int totalVoid) {
        this.totalVoid = totalVoid;
    }

    public void setTotalRefund(int totalRefund) {
        this.totalRefund = totalRefund;
    }
}
