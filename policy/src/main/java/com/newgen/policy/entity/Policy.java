package com.newgen.policy.entity;

import java.time.LocalDate;

import com.newgen.policy.dto.PolicyType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Policy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer policyId;
	@Enumerated(EnumType.STRING)
	private PolicyType policyType;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer duration;
}
