package com.newgen.customers.service;

import java.util.List;

import com.newgen.customers.dto.CustomerDTO;
import com.newgen.customers.exception.NewGenCustomersException;

public interface CustomerService {
	List<CustomerDTO> getCustomersByAgentId(Integer agentId) throws NewGenCustomersException;
	String addCustomer(CustomerDTO custDTO) throws NewGenCustomersException;
	Integer deleteCustomer(Integer customerId) throws NewGenCustomersException;
	void editCustomer(CustomerDTO custDTO) throws NewGenCustomersException;
	CustomerDTO getCustomer(Integer customerId) throws NewGenCustomersException;
	CustomerDTO getCustomerByMobileNo(String mobileNo) throws NewGenCustomersException; 
	
}
