package com.ets.client.domain;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "agent")
public class Agent extends Contactable implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private String name;
    @XmlElement
    private String web;
    @XmlElement
    private String atol;
    @XmlElement
    private String iata;
    @XmlElement
    private String abta;
    @XmlElement
    private String officeID;
    @XmlElement
    private boolean active;

    public Agent() {
        super();
    }

    @Transient
    public String[] getOfficeIDCollection() {

        if (this.officeID.contains(",")) {
            return this.officeID.split(",");
        } else {
            String[] oids = {this.officeID};
            return oids;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getAtol() {
        return atol;
    }

    public void setAtol(String atol) {
        this.atol = atol;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getAbta() {
        return abta;
    }

    public void setAbta(String abta) {
        this.abta = abta;
    }

    @Override
    public String calculateFullName() {
        return this.name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
