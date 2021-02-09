package org.egov.bookings.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.egov.bookings.model.MessageIdentity;
import org.egov.bookings.model.Tenant;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	@NotEmpty
	private String code;
	@NotEmpty
	private String message;
	@NotEmpty
	private String module;
	@NotEmpty
	private String locale;

	public Message(org.egov.bookings.model.Message domainMessage) {
		this.code = domainMessage.getCode();
		this.message = domainMessage.getMessage();
		this.module = domainMessage.getModule();
		this.locale = domainMessage.getLocale();
	}

	@JsonIgnore
	public MessageIdentity getMessageIdentity(Tenant tenant) {
		return MessageIdentity.builder().code(code).module(module).locale(locale).tenant(tenant).build();
	}
}
