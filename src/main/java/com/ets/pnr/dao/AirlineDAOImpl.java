package com.ets.pnr.dao;

import com.ets.GenericDAOImpl;
import com.ets.pnr.domain.Airline;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("airlineDAO")
@Transactional
public class AirlineDAOImpl extends GenericDAOImpl<Airline, Long> implements AirlineDAO {

    @Override
    public Airline fineByCode(String code) {
        String hql = "from Airline as a where a.code = :code";
        Query query = getSession().createQuery(hql);
        query.setParameter("code", code);

        Airline airLine = findOne(query);
        return airLine;
    }

}
