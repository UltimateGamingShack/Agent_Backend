package com.newgen.agent.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.newgen.agent.entity.Agent;

public interface AgentRepository extends CrudRepository<Agent, Integer>{
	Optional<Agent> findByAgencyCode(String agencyCode);
}
