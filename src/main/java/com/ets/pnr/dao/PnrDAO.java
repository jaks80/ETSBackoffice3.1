package com.ets.pnr.dao;

import com.ets.GenericDAO;
import com.ets.pnr.domain.Pnr;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Yusuf
 */
public interface PnrDAO extends GenericDAO<Pnr, Long> {    
    
    public Pnr getByIdWithChildren(Long id);

    public Set<String> findTicketingOIDs();
    
    public List<Pnr> searchByTktNo(String tktNo);

    public List<Pnr> getByGDSPnr(String gdsPnr);
    
    public List<Pnr> getByInvRef(String invref);
    
    public List<Pnr> searchByPaxName(String surName, String foreName);

    public List<Pnr> bookedPnrs();

    public List<Pnr> find(Date issueDateFrom, Date issueDateTo, String[] tktingAgtOID, String[] bookingAgtOID);

    public List<Pnr> searchUninvoicedPnr();

    public List<Pnr> searchPnrsToday(Date date);
}
