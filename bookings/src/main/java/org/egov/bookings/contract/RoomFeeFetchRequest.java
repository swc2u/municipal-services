package org.egov.bookings.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RoomFeeFetchRequest {

	private String totalNumberOfRooms;

	private String typeOfRomm;

	private String sector;
	
	private String communityCenterName;

}
