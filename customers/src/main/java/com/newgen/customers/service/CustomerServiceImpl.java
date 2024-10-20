package com.newgen.customers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newgen.customers.dto.CustomerDTO;
import com.newgen.customers.entity.Customer;
import com.newgen.customers.exception.NewGenCustomersException;
import com.newgen.customers.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service(value="customerServiceImpl")
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<CustomerDTO> getCustomersByAgentId(Integer agentId) throws NewGenCustomersException {
		List<Customer> customers = customerRepository.findByAgentId(agentId);
		if(customers.isEmpty() || customers==null) {
			throw new NewGenCustomersException("Service.CUSTOMERS_NOT_FOUND");
		}
		List<CustomerDTO> customersDTO =   new ArrayList<>();
		customersDTO = customers.stream().map(n -> {
			return modelMapper.map(n, CustomerDTO.class);
		}).collect(Collectors.toList());
		return customersDTO;
	}

	@Override
	public String addCustomer(CustomerDTO custDTO) throws NewGenCustomersException {
		Customer customer = new Customer();
		customer = modelMapper.map(custDTO, Customer.class);
		
		customerRepository.save(customer);
		return customer.getCustomerId().toString();
	}

	@Override
	public Integer deleteCustomer(Integer customerId) throws NewGenCustomersException {
		Optional<Customer> optional = customerRepository.findById(customerId);
		Customer customer = optional.orElseThrow(()-> new NewGenCustomersException("Service.CUSTOMER_NOT_FOUND"));
		Integer id = customer.getPolicyId();
		customerRepository.delete(customer);
		return id;
	}

	@Override
	public void editCustomer(CustomerDTO custDTO) throws NewGenCustomersException {
		Optional<Customer> optional = customerRepository.findById(custDTO.getCustomerId());
		if(!optional.isPresent()) {
			throw new NewGenCustomersException("Service.CUSTOMER_NOT_FOUND");
		}
		Customer customer = optional.get();
		
		if(custDTO.getAgentId()!=null) {
			customer.setAgentId(custDTO.getAgentId());
		}
		if(custDTO.getCustomerId()!=null) {
			customer.setCustomerId(custDTO.getCustomerId());
		}
		if(custDTO.getDob()!=null) {
			customer.setDob(custDTO.getDob());
		}
		if(custDTO.getCustomerName()!=null) {
			customer.setCustomerName(custDTO.getCustomerName());
		}
		if(custDTO.getEmail()!=null) {
			customer.setEmail(custDTO.getEmail());
		}
		if(custDTO.getMobileNo()!=null) {
			customer.setMobileNo(custDTO.getMobileNo());
		}
		if(custDTO.getPolicy()!=null && custDTO.getPolicy().getPolicyId()!=null) {
			customer.setPolicyId(custDTO.getPolicy().getPolicyId());
		}else {
			customer.setPolicyId(null);
		}
		customerRepository.save(customer);
	}

	@Override
	public CustomerDTO getCustomer(Integer customerId) throws NewGenCustomersException {
		Optional<Customer> optional = customerRepository.findById(customerId);
		Customer customer = optional.orElseThrow(()-> new NewGenCustomersException("Service.CUSTOMER_NOT_FOUND"));
		CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
		return customerDTO;
	}

	@Override
	public CustomerDTO getCustomerByMobileNo(String mobileNo) throws NewGenCustomersException {
		List<Customer> list = customerRepository.findByMobileNo(mobileNo);
		if(list.isEmpty()) {
			throw new NewGenCustomersException("Service.CUSTOMER_NOT_FOUND");
		}
		CustomerDTO customerDTO = modelMapper.map(list.getFirst(), CustomerDTO.class);
		return customerDTO;
	}

}
