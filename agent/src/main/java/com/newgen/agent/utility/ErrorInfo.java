package com.newgen.agent.utility;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorInfo {
	private LocalDateTime timestamp;
	private int errorCode;
	private String errorMessage;
}
