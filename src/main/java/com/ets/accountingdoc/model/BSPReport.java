package com.ets.accountingdoc.model;

import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import com.ets.pnr.model.GDSSaleReport;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class BSPReport {

    @XmlElement
    private List<TicketingPurchaseAcDoc> dueInvoices = new ArrayList<>();
    @XmlElement
    private List<TicketingPurchaseAcDoc> adm_acm = new ArrayList<>();
    @XmlElement
    private GDSSaleReport sale_report;

    public List<TicketingPurchaseAcDoc> getDueInvoices() {
        return dueInvoices;
    }

    public void setDueInvoices(List<TicketingPurchaseAcDoc> dueInvoices) {
        this.dueInvoices = dueInvoices;
    }

    public List<TicketingPurchaseAcDoc> getAdm_acm() {
        return adm_acm;
    }

    public void setAdm_acm(List<TicketingPurchaseAcDoc> adm_acm) {
        this.adm_acm = adm_acm;
    }

    public GDSSaleReport getSale_report() {
        return sale_report;
    }

    public void setSale_report(GDSSaleReport sale_report) {
        this.sale_report = sale_report;
    }
}
