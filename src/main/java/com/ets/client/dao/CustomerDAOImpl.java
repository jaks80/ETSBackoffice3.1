package com.ets.client.dao;

import com.ets.GenericDAOImpl;
import com.ets.client.domain.Customer;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yusuf
 */
@Service("customerDAO")
@Transactional
public class CustomerDAOImpl extends GenericDAOImpl<Customer, Long> implements CustomerDAO {

    @Override
    public List<Customer> findByLike(String surName, String foreName, String postCode, String telNo) {
        surName = nullToEmptyValue(surName).concat("%");
        foreName = nullToEmptyValue(foreName).concat("%");
        postCode = nullToEmptyValue(postCode).concat("%");
        telNo = nullToEmptyValue(telNo).concat("%");

        String hql = "from Customer c "
                + "left join fetch c.createdBy "
                + "left join fetch c.lastModifiedBy "
                + "where "
                + "(c.surName is null or c.surName like :surName) and "
                + "(c.foreName is null or c.foreName like :foreName) and "
                + "(c.postCode is null or c.postCode like :postCode) and "
                + "(c.telNo is null or c.telNo like :telNo) ";
        Query query = getSession().createQuery(hql);
        query.setParameter("surName", surName);
        query.setParameter("foreName", foreName);
        query.setParameter("postCode", postCode);
        query.setParameter("telNo", telNo);

        return query.list();
    }

    @Override
    public List findCustomerNameList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Customer> findCustomerContainsEmail() {
        String hql = "select cust from Customer as cust "
                + "where "
                + "cust.email is not null and cust.email <>''";
        Query query = getSession().createQuery(hql);
        return query.list();
    }

    @Override
    public List<Customer> querySearch(String keyword) {
        
        keyword = nullToEmptyValue(keyword).concat("%");
        
        String hql = "from Customer c "
                + "left join fetch c.createdBy "
                + "left join fetch c.lastModifiedBy "
                + "where "
                + "c.surName like :keyword or "
                + "c.foreName like :keyword or "
                + "c.postCode like :keyword or "
                + "c.email like :keyword or "
                + "c.city like :keyword or "
                + "c.telNo like :keyword ";
        Query query = getSession().createQuery(hql);
        query.setParameter("keyword", keyword);

        return query.list();
    }
}
