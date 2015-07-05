package com.ets.otherservice.dao;

import com.ets.GenericDAOImpl;
import com.ets.otherservice.domain.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("categoryDAO")
@Transactional
public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements CategoryDAO{
    
}
