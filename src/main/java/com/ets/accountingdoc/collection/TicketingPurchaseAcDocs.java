package com.ets.accountingdoc.collection;

import com.ets.accountingdoc.domain.TicketingPurchaseAcDoc;
import java.util.ArrayList;
import java.util.List;
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
public class TicketingPurchaseAcDocs {

    @XmlElement
    private List<TicketingPurchaseAcDoc> list = new ArrayList<>();

    public TicketingPurchaseAcDocs() {

    }

    public TicketingPurchaseAcDocs(List<TicketingPurchaseAcDoc> list) {
        this.list = list;
    }

    public List<TicketingPurchaseAcDoc> getList() {
        return list;
    }

    public void setList(List<TicketingPurchaseAcDoc> list) {
        this.list = list;
    }
}
