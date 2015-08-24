package com.ets.client.service;

import com.ets.client.dao.CustomerDAO;
import com.ets.client.domain.Customer;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yusuf
 */
@Service("customerService")
public class CustomerService {

    @Resource(name = "customerDAO")
    private CustomerDAO dao;

    public List<Customer> findAll() {
        return dao.findAll(Customer.class);
    }

    public List<Customer> findAll(String surName, String foreName, String postCode, String telNo) {

        return dao.findByLike(surName, foreName, postCode, telNo);
    }

    public List<Customer> querySearch(String keyword) {
     return dao.querySearch(keyword);
    }
    
    public Customer getCustomer(Long id) {
        return dao.findByID(Customer.class, id);
    }

    public Customer saveorUpdate(Customer customer) {
        dao.save(customer);
        return customer;
    }

    public void delete(Long id) {
        Customer customer = dao.findByID(Customer.class, id);
        dao.delete(customer);
    }

    public List<Customer> findCustomerContainsEmail() {
        return dao.findCustomerContainsEmail();
    }
}
