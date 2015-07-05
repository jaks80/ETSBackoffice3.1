package com.ets.pnr.dao;

import com.ets.GenericDAOImpl;
import com.ets.accountingdoc.domain.TicketingSalesAcDoc;
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
        String hql = "select distinct r from Remark as r "
                + "left join fetch r.pnr as p "                
                + "where p.id = :pnrId order by r.id asc";

        Query query = getSession().createQuery(hql);
        query.setParameter("pnrId", pnrId);
        List<Remark> result = query.list();
        return result;
    }
    
}
