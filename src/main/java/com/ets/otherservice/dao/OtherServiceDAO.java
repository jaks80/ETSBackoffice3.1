package com.ets.otherservice.dao;

import com.ets.GenericDAO;
import com.ets.otherservice.domain.OtherService;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface OtherServiceDAO extends GenericDAO<OtherService, Long>{   
    
    public List<OtherService> findItemsByCategory(Long categoryId);
    
    public List<OtherService> findItemsByKeyword(String keyword);
}
