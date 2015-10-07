package com.ets.otherservice.service;

import com.ets.otherservice.dao.OtherServiceDAO;
import com.ets.otherservice.domain.OtherService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("otherServiceService")
public class OtherServiceService {

    @Resource(name = "otherServiceDAO")
    private OtherServiceDAO dao;

    public List<OtherService> findAll() {

        String hql = "from OtherService os "
                + "LEFT JOIN FETCH os.category as cat "
                + "LEFT JOIN FETCH os.createdBy "
                + "LEFT JOIN FETCH os.lastModifiedBy "
                + "where os.active = 0 ";
        return dao.findMany(hql);
    }

    public List<OtherService> findItemsByCategory(Long categoryId) {

        List<OtherService> services = dao.findItemsByCategory(categoryId);
        for (OtherService os : services) {
            os.setCreatedBy(null);
            os.setLastModified(null);
        }

        return services;
    }

    public List<OtherService> findItemsByKeyword(String keyword) {

        List<OtherService> services = dao.findItemsByKeyword(keyword);
        for (OtherService os : services) {
            os.setCreatedBy(null);
            os.setLastModified(null);
        }

        return services;
    }

    public OtherService saveorUpdate(OtherService otherService) {
        dao.save(otherService);
        return otherService;
    }

    public void delete(OtherService otherService) {
        dao.delete(otherService);
    }
}
