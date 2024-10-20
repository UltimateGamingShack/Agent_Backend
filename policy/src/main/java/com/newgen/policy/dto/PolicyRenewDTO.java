package com.newgen.policy.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PolicyRenewDTO {
	@NotNull(message="{invalid_renew_policy_id}")
	private Integer policyId;
	@NotNull(message="{invalid_renew_duration")
	private Integer renewDuration;
}
