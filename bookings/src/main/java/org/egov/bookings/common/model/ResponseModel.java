package org.egov.bookings.common.model;

import java.io.Serializable;

public class ResponseModel implements Serializable {

	private static final long serialVersionUID = 1463124437645744569L;

	private String status;
	private String message;
	private Object data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
