package com.newgen.agent.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.newgen.agent.dto.AgentDTO;
import com.newgen.agent.dto.LoginDTO;
import com.newgen.agent.dto.MpinDTO;
import com.newgen.agent.entity.Agent;
import com.newgen.agent.exception.NewGenAgentException;
import com.newgen.agent.repository.AgentRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service(value="agentServiceImpl")
@Transactional
public class AgentServiceImpl implements AgentService{
	
	@Autowired
	AgentRepository agentRepository;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Override
	public AgentDTO getAgentByAgencyCode(String agencyCode) throws NewGenAgentException {
		Optional<Agent> optional = agentRepository.findByAgencyCode(agencyCode);
		if(!optional.isPresent()) {
			throw new NewGenAgentException("Service.AGENT_NOT_FOUND");
		}
		AgentDTO agentDTO = new AgentDTO();
		agentDTO = modelMapper.map(optional.get(), AgentDTO.class);
		return agentDTO;
		
	}

	@Override
	public String registerAgent(AgentDTO agentDTO) throws NewGenAgentException {
		if(agentDTO.getAgentId()!=null) {
			Optional<Agent> optional = agentRepository.findById(agentDTO.getAgentId());
			if(optional.isPresent()) {
				throw new NewGenAgentException("Service.AGENT_ALREADY_EXISTS");
			}
		}
		Agent agent = new Agent();
		agent.setAgencyCode(agentDTO.getAgencyCode());
		agent.setAgentName(agentDTO.getAgentName());
		agent.setMpin(agentDTO.getMpin());
		agent.setRole(agentDTO.getRole());
		agent.setPassword(passwordEncoder.encode(agentDTO.getPassword()));
		agent = agentRepository.save(agent);
		
		return agent.getAgentId().toString();
	}

	@Override
	public AgentDTO loginAgentByPassword(LoginDTO loginDTO) throws NewGenAgentException {
		Optional<Agent> ans = agentRepository.findByAgencyCode(loginDTO.getAgencyCode());
		if(!ans.isPresent()) {
			throw new NewGenAgentException("Service.INVALID_CREDENTIALS");
		}
		Agent a = ans.get();
		if(!passwordEncoder.matches(loginDTO.getPassword(), a.getPassword())) {
			throw new NewGenAgentException("Service.INVALID_CREDENTIALS");
		}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getAgencyCode(), loginDTO.getPassword()));
		AgentDTO agentDTO = modelMapper.map(a, AgentDTO.class);
		return agentDTO;
	}

	@Override
	public AgentDTO loginAgentByMpin(@Valid MpinDTO mpinDTO) throws NewGenAgentException {
		Optional<Agent> ans = agentRepository.findByAgencyCode(mpinDTO.getAgencyCode());
		if(!ans.isPresent()) {
			throw new NewGenAgentException("Service.INVALID_CREDENTIALS");
		}
		Agent a = ans.get();
		if(!mpinDTO.getMpin().equals(a.getMpin())) {
			throw new NewGenAgentException("Service.INVALID_CREDENTIALS");
		}
		AgentDTO agentDTO = modelMapper.map(a, AgentDTO.class);
		return agentDTO;
	}
	
}
