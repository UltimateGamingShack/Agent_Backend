package com.newgen.agent.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.newgen.agent.dto.AgentDTO;
import com.newgen.agent.dto.LoginDTO;
import com.newgen.agent.dto.LoginResponse;
import com.newgen.agent.dto.MpinDTO;
import com.newgen.agent.entity.Agent;
import com.newgen.agent.exception.NewGenAgentException;
import com.newgen.agent.service.AgentServiceImpl;
import com.newgen.agent.service.JwtService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/newgenagent")
@CrossOrigin(origins= {"http://localhost:9200", "http://localhost:3000"})
public class AgentController {
	@Autowired
	AgentServiceImpl agentService;
	@Autowired
	WebClient.Builder webClientBuilder;
	@Autowired
	Environment environment;
	@Autowired
	JwtService jwtService;
	@Autowired
	ModelMapper modelMapper;

	private String jwtToken;
	
	//############################## FALLBACK METHODS #############################
	public ResponseEntity<String> registerAgentFallBack(AgentDTO agentDTO, Throwable throwable) throws NewGenAgentException{
		return new ResponseEntity<String>(environment.getProperty("General.FALLBACK") + " - (Register Agent Failure)", HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<LoginResponse> loginAgentByPasswordFallBack(LoginDTO loginDTO, Throwable throwable) throws NewGenAgentException{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(null);
		loginResponse.setExpiresIn(0);
		
		return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
	}
	public ResponseEntity<LoginResponse> loginAgentByMpinFallBack(LoginDTO loginDTO, Throwable throwable) throws NewGenAgentException{
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(null);
		loginResponse.setExpiresIn(0);
		
		return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
	}
	//############################## CONTROLLER METHODS ###########################
	@GetMapping(value="/agent/auth/{agencyCode}")
	public ResponseEntity<AgentDTO> getAgentByAgencyCode(@PathVariable String agencyCode) throws NewGenAgentException{
		AgentDTO agentDTO = agentService.getAgentByAgencyCode(agencyCode);
		return new ResponseEntity<>(agentDTO, HttpStatus.OK);
	}
	
	@CircuitBreaker(name="agentService", fallbackMethod="registerAgentFallBack")
	@PostMapping(value="/agent/auth/register")
	public ResponseEntity<String> registerAgent(@Valid @RequestBody AgentDTO agentDTO) throws NewGenAgentException{
		String id = agentService.registerAgent(agentDTO);
		return new ResponseEntity<String>(environment.getProperty("API.AGENT_REGISTER_SUCCESS") + ":" + id, HttpStatus.CREATED);
	}
	@PostMapping(value="/agent/auth/login")
	@CircuitBreaker(name="agentService", fallbackMethod="loginAgentByPasswordFallBack")
	public ResponseEntity<LoginResponse> loginAgentByPassword(@Valid @RequestBody LoginDTO loginDTO) throws NewGenAgentException{
		AgentDTO agentDTO = agentService.loginAgentByPassword(loginDTO);
		Agent agent = modelMapper.map(agentDTO,Agent.class);
		jwtToken = jwtService.generateToken(agent);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
	}
	
	@PostMapping(value="/agent/auth/loginMpin")
	@CircuitBreaker(name="agentService", fallbackMethod="loginAgentByMpinFallBack")
	public ResponseEntity<LoginResponse> loginAgentByMpin(@Valid @RequestBody MpinDTO mpinDTO) throws NewGenAgentException{
		AgentDTO agentDTO = agentService.loginAgentByMpin(mpinDTO);
		Agent agent = modelMapper.map(agentDTO,Agent.class);
		jwtToken = jwtService.generateToken(agent);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
	}
	
	
}
