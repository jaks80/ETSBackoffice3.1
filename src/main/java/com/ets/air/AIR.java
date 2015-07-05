package com.ets.air;

import java.io.Serializable;
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
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class AIR implements Serializable{

    private static long serialVersionUID = 1L;
        
    @XmlElement
    private String type;
    @XmlElement
    private String creationDate;
    @XmlElement
    private String bookingAgtOid;        
    @XmlElement
    private String ticketingAgtOid;
    @XmlElement
    private Integer totalPages;
    @XmlElement
    private String version;
    @XmlElement
    private Integer currentPage; 
    @XmlElement
    private Long airSequenceNumber;
    
    @XmlElement
    private List<String> lines = new ArrayList<>();
    @XmlElement
    private List<AIR> morePages = new ArrayList<>();
    
    public AIR() {

    }
    
    public String getALine() {

        for (String s : lines) {
            if (s.startsWith("A-")) {
                return s;
            }
        }        
        return null;
    }
    
    public String getMUCLine() {

        for (String s : lines) {
            if (s.startsWith("MUC")) {
                return s;
            }
        }        
        return null;
    }
    
    public boolean hasMorePages() {
        if (this.getTotalPages() > this.getCurrentPage()) {
            return true;
        } else {
            return false;
        }
    }

    public void addPage(AIR air) {
            this.morePages.add(air);
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void addLine(String line){
     this.lines.add(line);
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public String getBookingAgtOid() {
        return bookingAgtOid;
    }

    public void setBookingAgtOid(String bookingAgtOid) {
        this.bookingAgtOid = bookingAgtOid;
    }

    public String getTicketingAgtOid() {
        return ticketingAgtOid;
    }

    public void setTicketingAgtOid(String ticketingAgtOid) {
        this.ticketingAgtOid = ticketingAgtOid;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Long getAirSequenceNumber() {
        return airSequenceNumber;
    }

    public void setAirSequenceNumber(Long airSequenceNumber) {
        this.airSequenceNumber = airSequenceNumber;
    }
    
    public List<AIR> getMorePages() {
        return morePages;
    }

    public void setMorePages(List<AIR> morePages) {
        this.morePages = morePages;
    }
}
