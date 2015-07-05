package com.ets.client.collection;

import com.ets.client.domain.Customer;
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
public class Customers {
    
    @XmlElement
    private List<Customer> list = new ArrayList<>();
    
    public Customers(){
    
    }
    
    public Customers(List<Customer> list){
     this.list = list;
    }

    public List<Customer> getList() {
        return list;
    }

    public void setList(List<Customer> list) {
        this.list = list;
    }
        
}
