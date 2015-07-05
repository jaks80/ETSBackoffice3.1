package com.ets.otherservice.dao;

import com.ets.GenericDAOImpl;
import com.ets.otherservice.domain.OtherService;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("otherServiceDAO")
@Transactional
public class OtherServiceDAOImpl extends GenericDAOImpl<OtherService, Long> implements OtherServiceDAO {

    @Override
    @Transactional(readOnly = true)
    public List<OtherService> findItemsByCategory(Long categoryId) {
        String hql = "from OtherService os "
                + "left join fetch os.category as cat "
                + "where os.active = 0 and "
                + "(:categoryId is null or cat.id = :categoryId)";
        Query query = getSession().createQuery(hql);
        query.setParameter("categoryId", categoryId);
        return query.list();
    }

    @Override
    public List<OtherService> findItemsByKeyword(String keyword) {
        keyword = "%".concat(keyword.concat("%"));

        String hql = "from OtherService os "
                + "left join fetch os.category as cat "
                + "where "
                + "(os.title like :keyword)";
        Query query = getSession().createQuery(hql);
        query.setParameter("keyword", keyword);
        return query.list();
    }

}
