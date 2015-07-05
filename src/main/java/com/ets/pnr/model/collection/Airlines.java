package com.ets.pnr.model.collection;

import com.ets.pnr.domain.Airline;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Airlines {
    
    private List<Airline> list = new ArrayList<>();

    public List<Airline> getList() {
        return list;
    }

    public void setList(List<Airline> list) {
        this.list = list;
    }        
}
