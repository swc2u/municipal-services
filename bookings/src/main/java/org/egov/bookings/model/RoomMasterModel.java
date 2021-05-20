package org.egov.bookings.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RoomMasterModel")
@Table(name = "BK_ROOM_MASTER")
public class RoomMasterModel {

	/** The id. */
	@Id
	@Column(name = "ID")
	private String id;

	/** The sector name. */
	@Column(name = "sector_name")
	private String sectorName;

	/** The total number of rooms. */
	@Column(name = "total_number_of_rooms")
	private String totalNumberOfRooms;

	/** The type of room. */
	@Column(name = "type_of_room")
	private String typeOfRoom;

	/** The rent for one day. */
	@Column(name = "rent_for_one_day")
	private String rentForOneDay;

	/** The rent for 3 hrs. */
	@Column(name = "rent_for_3_hrs")
	private String rentFor3Hrs;

	/** The rent for 6 hrs. */
	@Column(name = "rent_for_6_hrs")
	private String rentFor6Hrs;

	/** The rent for 9 hrs. */
	@Column(name = "rent_for_9_hrs")
	private String rentFor9Hrs;

	/** The from date. */
	@Column(name = "FROM_DATE")
	private Date fromDate;

	/** The to date. */
	@Column(name = "TO_DATE")
	private Date toDate;
	
	/** The last modified date. */
	@Column(name = "LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	
	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	/** The community center name. */
	@Column(name = "community_center_name")
	private String communityCenterName;
	
}
