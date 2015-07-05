package com.ets.accountingdoc.collection;

import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
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
public class TicketingSalesAcDocs {

    @XmlElement
    private List<TicketingSalesAcDoc> list = new ArrayList<>();

    public TicketingSalesAcDocs() {

    }

    public TicketingSalesAcDocs(List<TicketingSalesAcDoc> list) {
        this.list = list;
    }

    public List<TicketingSalesAcDoc> getList() {
        return list;
    }

    public void setList(List<TicketingSalesAcDoc> list) {
        this.list = list;
    }
}
