package org.egov.rti.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class RtiResponse {
	
	private Response cpio;
	private Response nodal;
	private Response appellate ;


}
