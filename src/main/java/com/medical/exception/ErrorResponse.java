package com.medical.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
	private String errorMsg;
	private int statusCode;
	private LocalDateTime timeStamp;

	public ErrorResponse(LocalDateTime timeStamp, int statusCode, String errorMsg) {
		super();
		this.timeStamp = timeStamp;
		this.statusCode = statusCode;
		this.errorMsg = errorMsg;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
