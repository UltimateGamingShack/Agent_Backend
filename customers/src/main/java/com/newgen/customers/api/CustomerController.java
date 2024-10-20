package com.newgen.customers.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.newgen.customers.dto.CustomerDTO;
import com.newgen.customers.dto.PolicyDTO;
import com.newgen.customers.exception.NewGenCustomersException;
import com.newgen.customers.service.CustomerServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:9100", "http://localhost:9101", "http://localhost:3000"})
@RequestMapping(value="/newgenagent")
public class CustomerController {
	@Autowired
	CustomerServiceImpl custService;
	@Autowired
	Environment environment;
	@Autowired
	WebClient.Builder webClientBuilder;
	
	//******************************* CUSTOMER FALLBACK METHODS ********************************
	public ResponseEntity<String> addCustomerFallBack(CustomerDTO custDTO, Throwable throwable) throws NewGenCustomersException{
		return new ResponseEntity<>(environment.getProperty("General.FALLBACK") + " - (Add Customer Failure)" , HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<String> editCustomerFallBack(Integer customerId, CustomerDTO custDTO, Throwable throwable) throws NewGenCustomersException{
		return new ResponseEntity<>(environment.getProperty("General.FALLBACK") + " - (Edit Customer Failure)" , HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<String> deleteCustomerFallBack(Integer customerId, Throwable throwable) throws NewGenCustomersException{
		customerId=null;
		return new ResponseEntity<>(environment.getProperty("General.FALLBACK") + " - (Delete Customer Failure)" , HttpStatus.BAD_REQUEST);
	}
	//******************************* CUSTOMER CONTROLLERS *************************************
	
	@GetMapping("/customers/{agentId")
	public ResponseEntity<List<CustomerDTO>> getCustomersByAgentId(@PathVariable Integer agentId) throws NewGenCustomersException{
		List<CustomerDTO> customers = custService.getCustomersByAgentId(agentId);
		customers = customers.stream().map(customer -> {
			if(customer.getPolicy()!=null && customer.getPolicy().getPolicyId()!=null) {
				PolicyDTO policy = webClientBuilder.build().get().uri("http://localhost:9500/newgenagent/policy/"+ customer.getPolicy().getPolicyId()).retrieve().bodyToMono(PolicyDTO.class).block();
				customer.setPolicy(policy);
			}else {
				customer.setPolicy(null);
			}
			return customer;
		}).collect(Collectors.toList());
		return new ResponseEntity<List<CustomerDTO>>(customers, HttpStatus.OK);
	}
	@GetMapping("/customers/customer/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerId) throws NewGenCustomersException{
		CustomerDTO customer = custService.getCustomer(customerId);
		PolicyDTO policyDTO = webClientBuilder.build().get().uri("http://localhost:950/newgenagent/policy/"+customer.getPolicy().getPolicyId()).retrieve().bodyToMono(PolicyDTO.class).block();
		customer.setPolicy(policyDTO);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	@PostMapping("/customers")
	@CircuitBreaker(name="customerService", fallbackMethod="addCustomerFallBack")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO custDTO) throws NewGenCustomersException{
		String id = custService.addCustomer(custDTO);
		String message = environment.getProperty("API.CUSTOMER_ADDED_SUCCESSFULLY") + " : " + id;
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	
	@PutMapping("/customers/{customerId}")
	@CircuitBreaker(name="customerService", fallbackMethod="editCustomerFallBack")
	public ResponseEntity<String> editCustomer(@PathVariable Integer customerId, @Valid @RequestBody CustomerDTO custDTO) throws NewGenCustomersException{
		custDTO.setCustomerId(customerId);
		custService.editCustomer(custDTO);
		return new ResponseEntity<>(environment.getProperty("API.CUSTOMER_EDIT_SUCCESS"), HttpStatus.OK);
	}
	
	@DeleteMapping("/customers/{customerId}")
	@CircuitBreaker(name="customerService", fallbackMethod="deleteCustomerFallBack")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId) throws NewGenCustomersException{
		Integer policyId = custService.deleteCustomer(customerId);
		if(policyId!=null) {
			webClientBuilder.build().delete().uri("http://localhost:9500/newgenagent/policy/"+policyId).retrieve().bodyToMono(String.class).block();
			
		}
		return new ResponseEntity<>(environment.getProperty("API.CUSTOMER_DELETED_SUCCESSFULLY"), HttpStatus.OK);
	}
	
	@PostMapping("/customers/{customerId}/policy")
	public ResponseEntity<Integer> addPolicyFromCustomer(@PathVariable Integer customerId, @Valid @RequestBody PolicyDTO policyDTO) throws NewGenCustomersException{
		CustomerDTO custDTO = custService.getCustomer(customerId);
		Integer id = webClientBuilder.build().post().uri("http://localhost:9500/newgenagent/policy").bodyValue(policyDTO).retrieve().bodyToMono(Integer.class).block();
		policyDTO.setPolicyId(id);
		custDTO.setPolicy(policyDTO);
		custService.editCustomer(custDTO);
		
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/customers/{customerId}/{policyId}")
	public ResponseEntity<String> deletePolicyFromCustomer(@PathVariable Integer customerId, @PathVariable Integer policyId) throws NewGenCustomersException{
		CustomerDTO custDTO = custService.getCustomer(customerId);
		Integer id = webClientBuilder.build().delete().uri("http://localhost:9500/newgenagent/policy/"+policyId).retrieve().bodyToMono(Integer.class).block();
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setPolicyId(null);
		custDTO.setPolicy(policyDTO);
		custService.editCustomer(custDTO);
		String message = environment.getProperty("API.POLICY_DELETED_SUCCESSFULLY");
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
}
