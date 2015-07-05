package com.ets.productivity.model;

import com.ets.report.model.Letterhead;
import com.ets.settings.service.AppSettingsService;
import com.ets.util.Enums;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ProductivityReport {
   
    @XmlElement
    private Letterhead letterhead = AppSettingsService.getLetterhead();
    @XmlElement
    private String reportDate;
    @XmlElement
    private String title;
    @XmlElement
    private Enums.SaleType saleType;
    @XmlElement
    private String dateFrom;
    @XmlElement
    private String dateTo;
    @XmlElement
    private List<ProductivityLine> productivityLine = new ArrayList<>();

    public void addLine(ProductivityLine line){
     this.productivityLine.add(line);
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Enums.SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(Enums.SaleType saleType) {
        this.saleType = saleType;
    }

    public Letterhead getLetterhead() {
        return letterhead;
    }

    public void setLetterhead(Letterhead letterhead) {
        this.letterhead = letterhead;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public List<ProductivityLine> getProductivityLine() {
        return productivityLine;
    }

    public void setProductivityLine(List<ProductivityLine> productivityLine) {
        this.productivityLine = productivityLine;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement
    public static class ProductivityLine {
    
        @XmlElement
        private String key;
        @XmlElement
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
