package com.ets.client.collection;

import com.ets.client.domain.Agent;
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
public class Agents {
    
    @XmlElement
    private List<Agent> list = new ArrayList<>();
    
    public Agents(){
    
    }
    
    public Agents(List<Agent> list){
     this.list = list;               
    }

    public List<Agent> getList() {
        return list;
    }

    public void setList(List<Agent> list) {
        this.list = list;
    }
}
