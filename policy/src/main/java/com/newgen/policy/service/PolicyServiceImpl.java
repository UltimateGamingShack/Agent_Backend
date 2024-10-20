package com.newgen.policy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newgen.policy.dto.PolicyDTO;
import com.newgen.policy.dto.PolicyRenewDTO;
import com.newgen.policy.entity.Policy;
import com.newgen.policy.exception.NewgenPolicyException;
import com.newgen.policy.repository.PolicyRepository;

import jakarta.transaction.Transactional;

@Service(value="policyServiceImpl")
@Transactional
public class PolicyServiceImpl implements PolicyService{
	@Autowired
	PolicyRepository policyRepository;
	private static final String ERRORMESSAGE = "Service.POLICY_NOT_FOUND";
	@Override
	public PolicyDTO getPolicy(Integer policyId) throws NewgenPolicyException {
		Policy policy = (policyRepository.findById(policyId)).orElseThrow(()->new NewgenPolicyException(ERRORMESSAGE));
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setDuration(policy.getDuration());
		policyDTO.setEndDate(policy.getEndDate());
		policyDTO.setPolicyId(policy.getPolicyId());
		policyDTO.setPolicyType(policy.getPolicyType());
		policyDTO.setStartDate(policy.getStartDate());
		return policyDTO;
	}

	@Override
	public Integer addPolicy(PolicyDTO policyDTO) throws NewgenPolicyException {
		Policy policy = new Policy();
		policy.setDuration(policyDTO.getDuration());
		policy.setEndDate(policyDTO.getEndDate());
		policy.setPolicyId(policyDTO.getPolicyId());
		policy.setPolicyType(policyDTO.getPolicyType());
		policy.setStartDate(policyDTO.getStartDate());
		
		Policy savedpolicy = policyRepository.save(policy);
		return savedpolicy.getPolicyId();
	}

	@Override
	public Integer deletePolicy(Integer policyId) throws NewgenPolicyException {
		Policy policy = policyRepository.findById(policyId).orElseThrow(()->new NewgenPolicyException(ERRORMESSAGE));
		Integer id = policy.getPolicyId();
		policyRepository.delete(policy);
		return id;
	}

	@Override
	public void renewPolicy(PolicyRenewDTO policyRenewDTO) throws NewgenPolicyException {
		Policy policy = policyRepository.findById(policyRenewDTO.getPolicyId()).orElseThrow(()-> new NewgenPolicyException(ERRORMESSAGE));
		policy.setEndDate(policy.getEndDate().plusMonths(policyRenewDTO.getRenewDuration()));
		policy.setDuration(policy.getDuration()+policyRenewDTO.getRenewDuration());
		policyRepository.save(policy);
		policyRenewDTO.setRenewDuration(-1);
	}

}
