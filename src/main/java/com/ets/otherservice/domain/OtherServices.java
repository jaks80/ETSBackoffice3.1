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
public class OtherServices {
    
    @XmlElement
    private List<OtherService> list = new ArrayList<>();
    
    public OtherServices(){
    
    }
    
    public OtherServices(List<OtherService> list){
     this.list = list;               
    }

    public List<OtherService> getList() {
        return list;
    }

    public void setList(List<OtherService> list) {
        this.list = list;
    }
}
