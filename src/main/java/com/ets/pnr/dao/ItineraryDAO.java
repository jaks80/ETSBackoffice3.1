package com.ets.pnr.dao;

import com.ets.GenericDAO;
import com.ets.pnr.domain.Itinerary;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface ItineraryDAO  extends GenericDAO<Itinerary, Long>{
    
    public List findSegments(Integer tktStatus, String[] airLineID, Date from, Date to,String... officeIds);
}
