package com.ets;

import com.ets.client.domain.Agent;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;

/**
 *
 * @author Yusuf
 * @param <T>
 * @param <Long>
 */
public interface GenericDAO<T, Long extends Serializable> {

    public void save(T entity);        

    public void saveBulk(List<T> entityList);

    public void merge(T entity);

    public void delete(T entity);

    public void deleteBulk(Set<T> entitySet);

    public List findAll(Class clazz);    

    public T findByID(Class clazz, Long id);

    public List<T> findMany(Query query);

    public List<T> findMany(String hql);
            
    public T findOne(Query query);    
}
