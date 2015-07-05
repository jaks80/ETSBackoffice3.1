package com.ets.otherservice.domain;

import com.ets.PersistentObject;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yus nm,; uf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "other_service")
public class OtherService extends PersistentObject implements Serializable {

    @XmlElement
    private String title;
    @XmlElement
    private String description;
    @XmlElement
    private BigDecimal purchaseCost = new BigDecimal("0.00");
    @XmlElement
    private BigDecimal sellingPrice = new BigDecimal("0.00");
    @XmlElement
    private int vatable;//0-no, 1-yes
    @XmlElement
    private int active;//0-yes, 1-no
    @XmlElement
    private Category category;

    public OtherService() {

    }

    @Column(nullable = false, length = 60)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    @Column(nullable = false)
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int isVatable() {
        return getVatable();
    }

    public void setVatable(int vatable) {
        this.vatable = vatable;
    }

    @Column(nullable = false)
    public int getVatable() {
        return vatable;
    }    

    @ManyToOne(fetch = FetchType.LAZY)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(nullable = false)
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
