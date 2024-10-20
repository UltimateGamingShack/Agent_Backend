package com.newgen.agent.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MpinDTO {
	@NotNull(message="{invalid_agent_mpin}")
	@Pattern(regexp="[0-9]{4}", message="{incorrect_agent_mpin}")
	private String mpin;
	@NotNull(message="{invalid_agency_code}")
	@Pattern(regexp="[0-9]{10}", message="{incorrect_agency_code}")
	private String agencyCode;
}
