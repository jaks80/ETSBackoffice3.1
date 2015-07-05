package com.ets.pnr.dao;

import com.ets.GenericDAO;
import com.ets.pnr.domain.Airline;

/**
 *
 * @author Yusuf
 */
public interface AirlineDAO extends GenericDAO<Airline, Long>{
    
    public Airline fineByCode(String code);
}
