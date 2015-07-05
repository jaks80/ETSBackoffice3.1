package com.ets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class CollectionAbstract {
    
    @XmlElement
    private Long resultQty;
    @XmlElement
    private Long totalPages;
    @XmlElement
    private final int itemPerPage = 50;
    
    public CollectionAbstract(){
    
    }

    public Long getResultQty() {
        return resultQty;
    }

    public void setResultQty(Long resultQty) {
        this.resultQty = resultQty;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }
    
    
}
