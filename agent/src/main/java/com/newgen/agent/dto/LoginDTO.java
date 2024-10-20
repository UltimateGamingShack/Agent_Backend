package com.newgen.agent.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDTO {
	@NotNull(message="{invalid_agency_code}")
	@Pattern(regexp = "[0-9]{10}", message = "{incorrect_agency_code}")
	private String agencyCode;
	@NotNull(message="{invalid_agent_password}")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message="{incorrect_agent_password}")
	private String password;
}
