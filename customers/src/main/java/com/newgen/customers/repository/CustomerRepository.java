package com.newgen.customers.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.newgen.customers.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	List<Customer> findByAgentId(Integer agentId);
	List<Customer> findByMobileNo(String mobileNo);
}
