package com.newgen.agent.service;

import com.newgen.agent.dto.AgentDTO;
import com.newgen.agent.dto.LoginDTO;
import com.newgen.agent.dto.MpinDTO;
import com.newgen.agent.exception.NewGenAgentException;

import jakarta.validation.Valid;

public interface AgentService {
	AgentDTO getAgentByAgencyCode(String agencyCode) throws NewGenAgentException;
	
	String registerAgent(@Valid AgentDTO agentDTO) throws NewGenAgentException;
	
	AgentDTO loginAgentByPassword(@Valid LoginDTO loginDTO) throws NewGenAgentException;
	
	AgentDTO loginAgentByMpin(@Valid MpinDTO mpinDTO) throws NewGenAgentException;
}
