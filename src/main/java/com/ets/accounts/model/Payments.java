package com.ets.accounts.model;

import com.ets.accounts.domain.Payment;
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
public class Payments {
    
    @XmlElement
    private List<Payment> list = new ArrayList<>();

    public List<Payment> getList() {
        return list;
    }

    public void setList(List<Payment> list) {
        this.list = list;
    }        
}
