package com.ets.pnr.model;

import com.ets.pnr.domain.Pnr;
import com.ets.pnr.domain.Ticket;
import com.ets.util.DateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TicketSaleReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String reportTitle;
    @XmlElement
    private String dateFrom;
    @XmlElement
    private String dateTo;
    @XmlElement
    private int tktQty;

    @XmlElement
    private String totalBaseFare;
    @XmlElement
    private String totalTax;
    @XmlElement
    private String totalFee;
    @XmlElement
    private String totalCommission;
    @XmlElement
    private String totalNetPurchaseFare;
    @XmlElement
    private String totalRevenue;
    
    @XmlElement
    private String totalAtolChg;
    @XmlElement
    private String totalNetSellingFare;
    @XmlElement
    private List<SaleReportLine> saleReportLines = new ArrayList<>();

    public static TicketSaleReport serializeToSalesSummery(List<Ticket> tickets, Date issueDateFrom, Date issueDateTo) {

        BigDecimal totalBaseFare = new BigDecimal("0.00");
        BigDecimal totalTax = new BigDecimal("0.00");
        BigDecimal totalFee = new BigDecimal("0.00");
        BigDecimal totalCommission = new BigDecimal("0.00");
        BigDecimal totalNetPurchaseFare = new BigDecimal("0.00");
        BigDecimal totalAtolChg = new BigDecimal("0.00");
        BigDecimal totalNetSellingFare = new BigDecimal("0.00");
        BigDecimal totalRevenue = new BigDecimal("0.00");

        TicketSaleReport report = new TicketSaleReport();
        report.setDateFrom(DateUtil.dateToString(issueDateFrom));
        report.setDateTo(DateUtil.dateToString(issueDateTo));
        report.setTktQty(tickets.size());
        
        for (Ticket t : tickets) {
            SaleReportLine line = new SaleReportLine();

            line.setTicketNo(t.getTicketNo());
            line.setDocIssuedate(DateUtil.dateToString(t.getDocIssuedate()));
            line.setTktStatus(t.getTktStatus().toString());
            
            if(t.getTicketingSalesAcDoc()!=null){
             line.setSalesInvoiceId(t.getTicketingSalesAcDoc().getId());
             line.setSellingRefference(t.getTicketingSalesAcDoc().getReference().toString());
            }
            if(t.getTicketingPurchaseAcDoc()!=null){
             line.setPurchaseInvoiceId(t.getTicketingPurchaseAcDoc().getId());
             line.setVendorRefference(t.getTicketingPurchaseAcDoc().getVendorRef());
            }
                        
            line.setBaseFare(t.getBaseFare().toString());
            totalBaseFare = totalBaseFare.add(t.getBaseFare());

            line.setTax(t.getTax().toString());
            totalTax = totalTax.add(t.getTax());

            line.setCommission(t.getCommission().toString());
            totalCommission = totalCommission.add(t.getCommission());

            line.setFee(t.getFee().toString());
            totalFee = totalFee.add(t.getFee());

            line.setNetPurchaseFare(t.calculateNetPurchaseFare().toString());
            totalNetPurchaseFare = totalNetPurchaseFare.add(t.calculateNetPurchaseFare());

            line.setGrossFare(t.getGrossFare().toString());
            line.setDiscount(t.getDiscount().toString());

            line.setAtolChg(t.getAtolChg().toString());
            totalAtolChg = totalAtolChg.add(t.getAtolChg());

            line.setNetSellingFare(t.calculateNetSellingFare().toString());
            totalNetSellingFare = totalNetSellingFare.add(t.calculateNetSellingFare());
            line.setRevenue(t.calculateRevenue().toString());
            totalRevenue = totalRevenue.add(t.calculateRevenue());

            Pnr pnr = t.getPnr();
            line.setGdsPnr(pnr.getGdsPnr());
            line.setAirLineCode(pnr.getAirLineCode());
            if(pnr.getTicketing_agent()!=null){
             line.setTicketingAgent(pnr.getTicketing_agent().calculateFullName());
            }
            if (pnr.getAgent() != null) {
                line.setClient(pnr.getAgent().getName());
            } else if(pnr.getCustomer() !=null){
                line.setClient(pnr.getCustomer().calculateFullName());
            }
                        
            
            report.addLine(line);
        }
        
        report.setTotalBaseFare(totalBaseFare.toString());
        report.setTotalTax(totalTax.toString());
        report.setTotalCommission(totalCommission.toString());
        report.setTotalFee(totalFee.toString());
        report.setTotalNetPurchaseFare(totalNetPurchaseFare.toString());
        report.setTotalAtolChg(totalAtolChg.toString());
        report.setTotalNetSellingFare(totalNetSellingFare.toString());
        report.setTotalRevenue(totalRevenue.toString());
        
        return report;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getTktQty() {
        return tktQty;
    }

    public void setTktQty(int tktQty) {
        this.tktQty = tktQty;
    }

    public List<SaleReportLine> getSaleReportLines() {
        return saleReportLines;
    }

    public void setSaleReportLines(List<SaleReportLine> saleReportLines) {
        this.saleReportLines = saleReportLines;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement
    public static class SaleReportLine {

        @XmlElement
        private Long salesInvoiceId;
        @XmlElement
        private Long purchaseInvoiceId;
        @XmlElement
        private String airLineCode;
        @XmlElement
        private String gdsPnr;

        @XmlElement
        private String client;
        @XmlElement
        private String ticketingAgent;

        @XmlElement
        private String ticketNo;
        @XmlElement
        private String docIssuedate;
        @XmlElement
        private String sellingRefference;
        @XmlElement
        private String vendorRefference;

        @XmlElement
        private String baseFare;
        @XmlElement
        private String tax;
        @XmlElement
        private String fee;
        @XmlElement
        private String commission;
        @XmlElement
        private String netPurchaseFare;
        @XmlElement
        private String grossFare;
        @XmlElement
        private String discount;
        @XmlElement
        private String atolChg;
        @XmlElement
        private String netSellingFare;
        @XmlElement
        private String tktStatus;
         @XmlElement
        private String revenue;

        public String getTicketNo() {
            return ticketNo;
        }

        public void setTicketNo(String ticketNo) {
            this.ticketNo = ticketNo;
        }

        public String getDocIssuedate() {
            return docIssuedate;
        }

        public void setDocIssuedate(String docIssuedate) {
            this.docIssuedate = docIssuedate;
        }

        public String getAirLineCode() {
            return airLineCode;
        }

        public void setAirLineCode(String airLineCode) {
            this.airLineCode = airLineCode;
        }

        public String getGdsPnr() {
            return gdsPnr;
        }

        public void setGdsPnr(String gdsPnr) {
            this.gdsPnr = gdsPnr;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getTicketingAgent() {
            return ticketingAgent;
        }

        public void setTicketingAgent(String ticketingAgent) {
            this.ticketingAgent = ticketingAgent;
        }

        public String getSellingRefference() {
            return sellingRefference;
        }

        public void setSellingRefference(String sellingRefference) {
            this.sellingRefference = sellingRefference;
        }

        public String getVendorRefference() {
            return vendorRefference;
        }

        public void setVendorRefference(String vendorRefference) {
            this.vendorRefference = vendorRefference;
        }

        public String getBaseFare() {
            return baseFare;
        }

        public void setBaseFare(String baseFare) {
            this.baseFare = baseFare;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getNetPurchaseFare() {
            return netPurchaseFare;
        }

        public void setNetPurchaseFare(String netPurchaseFare) {
            this.netPurchaseFare = netPurchaseFare;
        }

        public String getGrossFare() {
            return grossFare;
        }

        public void setGrossFare(String grossFare) {
            this.grossFare = grossFare;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getAtolChg() {
            return atolChg;
        }

        public void setAtolChg(String atolChg) {
            this.atolChg = atolChg;
        }

        public String getNetSellingFare() {
            return netSellingFare;
        }

        public void setNetSellingFare(String netSellingFare) {
            this.netSellingFare = netSellingFare;
        }

        public String getTktStatus() {
            return tktStatus;
        }

        public void setTktStatus(String tktStatus) {
            this.tktStatus = tktStatus;
        }

        public String getRevenue() {
            return revenue;
        }

        public void setRevenue(String revenue) {
            this.revenue = revenue;
        }

        public Long getSalesInvoiceId() {
            return salesInvoiceId;
        }

        public void setSalesInvoiceId(Long salesInvoiceId) {
            this.salesInvoiceId = salesInvoiceId;
        }

        public Long getPurchaseInvoiceId() {
            return purchaseInvoiceId;
        }

        public void setPurchaseInvoiceId(Long purchaseInvoiceId) {
            this.purchaseInvoiceId = purchaseInvoiceId;
        }
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getTotalBaseFare() {
        return totalBaseFare;
    }

    public void setTotalBaseFare(String totalBaseFare) {
        this.totalBaseFare = totalBaseFare;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String getTotalNetPurchaseFare() {
        return totalNetPurchaseFare;
    }

    public void setTotalNetPurchaseFare(String totalNetPurchaseFare) {
        this.totalNetPurchaseFare = totalNetPurchaseFare;
    }

    public String getTotalAtolChg() {
        return totalAtolChg;
    }

    public void setTotalAtolChg(String totalAtolChg) {
        this.totalAtolChg = totalAtolChg;
    }

    public String getTotalNetSellingFare() {
        return totalNetSellingFare;
    }

    public void setTotalNetSellingFare(String totalNetSellingFare) {
        this.totalNetSellingFare = totalNetSellingFare;
    }
    
    public void addLine(SaleReportLine line){
     this.saleReportLines.add(line);
    }
}
