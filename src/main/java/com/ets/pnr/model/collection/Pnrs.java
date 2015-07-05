package com.ets.pnr.model.collection;

import com.ets.pnr.domain.Pnr;
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
public class Pnrs {

    @XmlElement
    private List<Pnr> list = new ArrayList<>();

    public List<Pnr> getList() {
        return list;
    }

    public void setList(List<Pnr> list) {
        this.list = list;
    }
}
