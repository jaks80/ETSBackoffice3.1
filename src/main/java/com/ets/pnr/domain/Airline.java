package com.ets.pnr.domain;

import com.ets.util.Enums;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yusuf
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Access(AccessType.PROPERTY)
@Table(name = "airline")
public class Airline implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String code;
    @XmlElement
    private String numaricCode;
    @XmlElement
    private String name;
    @XmlElement
    private Enums.CalculationType calculationType;
    @XmlElement
    private BigDecimal bspCom;

    public Airline() {

    }

    @Id
    @Column(length = 2)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 3)
    public String getNumaricCode() {
        return numaricCode;
    }

    public void setNumaricCode(String numaricCode) {
        this.numaricCode = numaricCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Enums.CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Enums.CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public BigDecimal getBspCom() {
        return bspCom;
    }

    public void setBspCom(BigDecimal bspCom) {
        this.bspCom = bspCom;
    }

    public BigDecimal calculateBspCom(Ticket ticket) {

        BigDecimal bspCom = new BigDecimal("0.00");

        if (ticket.getTktStatus() != Enums.TicketStatus.BOOK && ticket.getTktStatus() != Enums.TicketStatus.VOID) {
            if (getCalculationType().equals(Enums.CalculationType.FIXED)) {
                bspCom = this.getBspCom().negate();
            } else {
                bspCom = this.getBspCom().multiply(ticket.getBaseFare()).divide(new BigDecimal("100.00")).negate();
            }
        }
        if (ticket.getTktStatus() == Enums.TicketStatus.REFUND) {
            bspCom = bspCom.abs();
        }
        return bspCom;
    }
}
