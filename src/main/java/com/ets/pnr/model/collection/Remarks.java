package com.ets.pnr.model.collection;

import com.ets.pnr.domain.Remark;
import com.ets.pnr.model.RemarkSummary;
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
public class Remarks {

    @XmlElement
    private List<RemarkSummary> list = new ArrayList<>();

    public List<RemarkSummary> getList() {
        return list;
    }

    public void setList(List<RemarkSummary> list) {
        this.list = list;
    }
}
