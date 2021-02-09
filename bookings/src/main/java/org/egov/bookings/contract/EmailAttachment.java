package org.egov.bookings.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
public class EmailAttachment {
	private String name;
	private String url;
	private String mimeType;
}
