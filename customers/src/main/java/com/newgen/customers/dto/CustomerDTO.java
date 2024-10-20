package com.newgen.customers.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDTO {
	private Integer customerId;
	@NotNull(message="{invalid_customer_name}")
	private String customerName;
	@NotNull(message="{invalid_customer_email}")
	@Email(message="{incorrect_customer_email}")
	private String email;
	@NotNull(message="{invalid_customer_dob}")
	@Past(message="{incorrect_customer_dob}")
	private LocalDate dob;
	@NotNull(message="{invalid_customer_mobile}")
	@Pattern(regexp="[0-9]{10}", message="{incorrect_customer_mobile}")
	private String mobileNo;
	@NotNull(message="{invalid_agent_id}")
	private Integer agentId;
	
	private PolicyDTO policy;
	
}
