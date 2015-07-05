package com.ets.otherservice.service;

import com.ets.otherservice.dao.CategoryDAO;
import com.ets.otherservice.domain.Category;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("categoryService")
public class CategoryService {
    
    @Resource(name = "categoryDAO")
    private CategoryDAO dao;

    public List<Category> findAll() {
        return dao.findAll(Category.class);
    }

    public Category saveorUpdate(Category category) {
        dao.save(category);
        return category;
    }

    public void delete(Category category) {
        dao.delete(category);
    }
}
