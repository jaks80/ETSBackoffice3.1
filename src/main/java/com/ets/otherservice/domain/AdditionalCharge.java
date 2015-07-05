package com.ets.otherservice.domain;

import com.ets.PersistentObject;
import com.ets.util.Enums.CalculationType;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "additional_charge")
public class AdditionalCharge extends PersistentObject implements Serializable{

    @XmlElement
    private String title;
    @XmlElement
    private BigDecimal charge = new BigDecimal("0.00");
    @XmlElement
    private CalculationType calculationType;  

    public AdditionalCharge(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }
}
