package com.ets.pnr.service;

import com.ets.pnr.model.collection.Airlines;
import com.ets.pnr.dao.AirlineDAO;
import com.ets.pnr.domain.Airline;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("airlineService")
public class AirlineService {

    @Resource(name = "airlineDAO")
    private AirlineDAO dao;

    public Airline find(String code) {
        Airline airline = dao.fineByCode(code);
        return airline;
    }

    public Airlines match(String name) {

        return new Airlines();
    }

    public void save(Airline airline) {
        dao.save(airline);
    }

    public void saveBulk(Airlines careers) {

    }
}
