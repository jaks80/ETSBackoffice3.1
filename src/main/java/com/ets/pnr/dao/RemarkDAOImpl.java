package com.ets.pnr.dao;

import com.ets.GenericDAOImpl;
import com.ets.pnr.domain.Remark;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("remarkDAO")
@Transactional
public class RemarkDAOImpl extends GenericDAOImpl<Remark, Long> implements RemarkDAO{

    @Override
    @Transactional(readOnly = true)
    public List<Remark> getByPnrId(Long pnrId) {
        String hql = "SELECT DISTINCT r FROM Remark AS r "
                + "LEFT JOIN FETCH r.pnr as p "
                + "LEFT JOIN FETCH r.createdBy "
                + "WHERE p.id = :pnrId ORDER BY r.id ASC";

        Query query = getSession().createQuery(hql);
        query.setParameter("pnrId", pnrId);
        List<Remark> result = query.list();
        return result;
    }

    @Override
    public int deleteRemarks(Long pnrId) {

        String sql = "DELETE FROM remark WHERE pnrid_fk=:pnrId";
        Query query = getSession().createSQLQuery(sql);
        query.setParameter("pnrId", pnrId);
        return query.executeUpdate();
    }
}
