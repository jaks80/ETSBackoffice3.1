package com.ets.air.dao;

import com.ets.GenericDAO;
import com.ets.pnr.domain.Pnr;
import java.util.Date;

/**
 *
 * @author Yusuf
 */
public interface AirDAO extends GenericDAO<Pnr, Long>{
    
    public Pnr findPnr(String gdsPnr, Date pnrCreationDate);
    
    public Pnr findPnr(String ticketNo, String surName);
}
