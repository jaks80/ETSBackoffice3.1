package com.ets.pnr.model.collection;

import com.ets.pnr.domain.Remark;
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
    private List<Remark> list = new ArrayList<>();

    public List<Remark> getList() {
        return list;
    }

    public void setList(List<Remark> list) {
        this.list = list;
    }
}
