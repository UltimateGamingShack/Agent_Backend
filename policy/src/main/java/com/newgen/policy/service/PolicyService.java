package com.newgen.policy.service;

import com.newgen.policy.dto.PolicyDTO;
import com.newgen.policy.dto.PolicyRenewDTO;
import com.newgen.policy.exception.NewgenPolicyException;

public interface PolicyService {
	PolicyDTO getPolicy(Integer policyId) throws NewgenPolicyException;
	Integer addPolicy(PolicyDTO policyDTO) throws NewgenPolicyException;
	Integer deletePolicy(Integer policyId) throws NewgenPolicyException;
	void renewPolicy(PolicyRenewDTO policyRenewDTO) throws NewgenPolicyException;
}
