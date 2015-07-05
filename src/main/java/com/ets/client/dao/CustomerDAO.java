package com.ets.client.dao;

import com.ets.GenericDAO;
import com.ets.client.domain.Customer;
import java.util.List;

/**
 *
 * @author Yusuf
 */
public interface CustomerDAO extends GenericDAO<Customer, Long>{
    
    public List<Customer> findByLike(String surName, String foreName, String postCode,String telNo);
    
    public List findCustomerNameList();
}
