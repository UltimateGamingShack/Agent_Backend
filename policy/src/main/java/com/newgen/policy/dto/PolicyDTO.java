package com.newgen.policy.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class PolicyDTO {
	private Integer policyId;
	@NotNull(message="{invalid_policy_type}")
	private PolicyType policyType;
	@NotNull(message="{invalid_policy_start_date}")
	@PastOrPresent(message="{incorrect_policy_start_date}")
	private LocalDate startDate;
	@NotNull(message="{invalid_policy_end_date}")
	@PastOrPresent(message="{incorrect_policy_end_date}")
	private LocalDate endDate;
	@NotNull(message="{invalid_policy_duration}")
	private Integer duration;
}
