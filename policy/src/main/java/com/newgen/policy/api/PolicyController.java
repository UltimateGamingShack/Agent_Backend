package com.newgen.policy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.newgen.policy.dto.PolicyDTO;
import com.newgen.policy.dto.PolicyRenewDTO;
import com.newgen.policy.exception.NewgenPolicyException;
import com.newgen.policy.service.PolicyServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = {"http://localhost:9300", "http://localhost:3000"})
@Validated
@RequestMapping(value="/newgenagent")
@Log4j2
public class PolicyController {

	@Autowired
	PolicyServiceImpl policyService;
	
	@Autowired
	Environment environment;
	
	@Autowired
	WebClient.Builder webClientBuilder;
	
	private static final String GENERAL_MESSAGE = "General.FALLBACK";

	// ################################### FALLBACK METHODS #######################################
	
	public ResponseEntity<PolicyDTO> getPolicyFallBack(Integer policyId, Throwable throwable) throws NewgenPolicyException{
		PolicyDTO policyDTO = new PolicyDTO();
		
		log.error(throwable.getMessage());
		
		return new ResponseEntity<>(policyDTO, HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<Integer> addPolicyFallBack(PolicyDTO policyDTO, Throwable throwable) throws NewgenPolicyException{
		Integer id = null;
		log.error(throwable.getMessage());
		return new ResponseEntity<>(id, HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<String> renewPolicyFallBack(Integer policyId, PolicyRenewDTO policyRenewDTO, Throwable throwable) throws NewgenPolicyException{
		policyRenewDTO.setPolicyId(null);
		policyService.renewPolicy(null);
		log.error(throwable.getMessage());
		String message = environment.getProperty(GENERAL_MESSAGE);
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<Integer> deletePolicyFallBack(Integer policyId, Throwable throwable) throws NewgenPolicyException{
		Integer id = null;
		log.error(throwable.getMessage());
		return new ResponseEntity<>(id, HttpStatus.BAD_REQUEST);
	}
	
	// ################################### CONTROLLER METHODS ######################################
	
	@GetMapping(value="/policy/{policyId}")
	@CircuitBreaker(name="policyService", fallbackMethod="getPolicyFallBack")
	public ResponseEntity<PolicyDTO> getPolicy(@PathVariable Integer policyId) throws NewgenPolicyException{
		PolicyDTO policy = policyService.getPolicy(policyId);
		return new ResponseEntity<PolicyDTO>(policy, HttpStatus.OK);
	}
	
	@PostMapping(value="/policy")
	@CircuitBreaker(name="policyService", fallbackMethod = "addPolicyFallBack")
	public ResponseEntity<Integer> addPolicy(@Valid @RequestBody PolicyDTO policyDTO) throws NewgenPolicyException{
		Integer id = policyService.addPolicy(policyDTO);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}
	@PostMapping(value="/policy/renew/{policyId}")
	@CircuitBreaker(name="policyService", fallbackMethod = "renewPolicyFallBack")
	public ResponseEntity<String> renewPolicy(@PathVariable Integer policyId, @Valid @RequestBody PolicyRenewDTO policyRenewDTO) throws NewgenPolicyException{
		policyRenewDTO.setPolicyId(policyId);
		policyService.renewPolicy(policyRenewDTO);
		String message = environment.getProperty("API.POLICY_RENEWED_SUCCESSFULLY");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/policy/{poicyId}")
	@CircuitBreaker(name="policyService", fallbackMethod = "deletePolicyFallBack")
	public ResponseEntity<Integer> deletePolicy(@PathVariable Integer policyId) throws NewgenPolicyException{
		Integer id = policyService.deletePolicy(policyId);
		
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
}
