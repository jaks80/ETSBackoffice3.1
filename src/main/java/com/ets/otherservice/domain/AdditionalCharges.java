package com.ets.otherservice.domain;

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
public class AdditionalCharges {
    @XmlElement
    private List<AdditionalCharge> list = new ArrayList<>();
    
    public AdditionalCharges(){
    
    }
    
    public AdditionalCharges(List<AdditionalCharge> list){
     this.list = list;               
    }

    public List<AdditionalCharge> getList() {
        return list;
    }

    public void setList(List<AdditionalCharge> list) {
        this.list = list;
    }
}
