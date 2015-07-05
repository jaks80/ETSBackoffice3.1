package com.ets.accountingdoc_o.model;

import com.ets.accountingdoc.domain.AccountingDocumentLine;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class DocumentLineSummery {
    
    @XmlElement
    private String title;
    @XmlElement
    private String lineRemark;
    @XmlElement
    private String gross;
    @XmlElement
    private String disc;
    @XmlElement
    private String qty;
    @XmlElement
    private String net;

    public static DocumentLineSummery documentLineToModel(AccountingDocumentLine l) {
        DocumentLineSummery dl = new DocumentLineSummery();
        dl.setTitle(l.getOtherService().getTitle());
        dl.setLineRemark(l.getRemark());
        dl.setGross(l.getAmount().toString());
        dl.setDisc(l.getDiscount().toString());
        dl.setQty(String.valueOf(l.getQty()));
        dl.setNet(l.calculateOServiceLineTotal().toString());
        return dl;
    }
       
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLineRemark() {
        return lineRemark;
    }

    public void setLineRemark(String lineRemark) {
        this.lineRemark = lineRemark;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }
}
