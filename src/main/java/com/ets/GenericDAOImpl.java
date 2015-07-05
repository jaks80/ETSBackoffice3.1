package com.ets;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 * @param <T>
 */
@SuppressWarnings("unchecked")
@Service("genericDAO")
@Transactional
public abstract class GenericDAOImpl<T, Long extends Serializable> implements GenericDAO<T, Long> {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public GenericDAOImpl() {

    }

    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    public void save(T entity) {                 
       getSession().saveOrUpdate(entity);      
    }

    public void saveBulk(List<T> entityList){
    
        for(T entity: entityList){
         save(entity);
        }
    }
    
    public void merge(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.merge(entity);
    }

    public void delete(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.delete(entity);
    }

    public void deleteBulk(Set<T> entitySet) {
        Session hibernateSession = this.getSession();
        
        for(T t: entitySet){
        hibernateSession.delete(t);
        }
    }
        
    public List<T> findMany(Query query) {
        List<T> t;
        t = (List<T>) query.list();
        return t;
    }

    public List<T> findMany(String hql) {
        Query query = getSession().createQuery(hql);
        
        return findMany(query);
    }
    
    public T findOne(Query query) {
        T t;
        t = (T) query.uniqueResult();
        return t;
    }

    public T findByID(Class clazz, Long id) {
        Session hibernateSession = this.getSession();
        T t = null;
        t = (T) hibernateSession.get(clazz, id);
        return t;
    }

    public List<T> findAll(Class clazz) {
        return findByCriteria(clazz);        
    }        
       
    protected List<T> findByCriteria(Class clazz, Criterion... criterion) {
        Criteria crit = getSession().createCriteria(clazz);
        
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }        
    
         public String nullStringToNullValue(String string){
     
        if(string == null || "null".equals(string)){
         string = null;
        }        
        return string;
    }
     
     public String nullToEmptyValue(String string){
     
        if("null".equals(string) || string == null){
         string = "";
        }        
        return string;
    }
}
