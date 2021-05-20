package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;

import org.egov.bookings.model.RoomsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The Interface RoomsRepository.
 */
public interface RoomsRepository extends JpaRepository<RoomsModel, String>{

	/**
	 * Find by room application number.
	 *
	 * @param roomApplicationNumber the room application number
	 * @return the rooms model
	 */
	List<RoomsModel> findByRoomApplicationNumber(String roomApplicationNumber);
	
	/**
	 * Find by community application number.
	 *
	 * @param applicationNumber the application number
	 * @return the list
	 */
	public List<RoomsModel> findByCommunityApplicationNumber(String applicationNumber);
	
	/**
	 * Gets the citizen community center room booking.
	 *
	 * @param uuid the uuid
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param applicationNumber the application number
	 * @return the citizen community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM"
					+ " INNER JOIN BK_BOOKINGS AS BB ON BB.BK_APPLICATION_NUMBER = BRM.COMMUNITY_APPLICATION_NUMBER"
					+ " WHERE BB.UUID = (?1) AND BRM.ROOM_APPLICATION_STATUS LIKE (%?2%) AND BRM.TYPE_OF_ROOM LIKE (%?3%)"
					+ " AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?4%) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getCitizenCommunityCenterRoomBooking( String uuid, String applicationStatus, String typeOfRoom, String applicationNumber );
	
	/**
	 * Gets the citizen community center room booking.
	 *
	 * @param uuid the uuid
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param roomApplicationNumber the room application number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the citizen community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM"
					+ " INNER JOIN BK_BOOKINGS AS BB ON BB.BK_APPLICATION_NUMBER = BRM.COMMUNITY_APPLICATION_NUMBER"
					+ " WHERE BB.UUID = (?1) AND BRM.ROOM_APPLICATION_STATUS LIKE (%?2%) AND BRM.TYPE_OF_ROOM LIKE (%?3%)"
					+ " AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?4%) AND BRM.CREATED_DATE BETWEEN (?5) AND (?6) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getCitizenCommunityCenterRoomBooking( String uuid, String applicationStatus, String typeOfRoom, String roomApplicationNumber, Date fromDate, Date toDate );
	
	/**
	 * Gets the employee community center room booking.
	 *
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param roomApplicationNumber the room application number
	 * @return the employee community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM WHERE BRM.ROOM_APPLICATION_STATUS != 'INITIATED' AND BRM.ROOM_APPLICATION_STATUS LIKE (%?1%)"
					+ " AND BRM.TYPE_OF_ROOM LIKE (%?2%) AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?3%) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getEmployeeCommunityCenterRoomBooking( String applicationStatus, String typeOfRoom, String roomApplicationNumber );
	
	/**
	 * Gets the employee community center room booking.
	 *
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param roomApplicationNumber the room application number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM WHERE BRM.ROOM_APPLICATION_STATUS != 'INITIATED' AND BRM.ROOM_APPLICATION_STATUS LIKE (%?1%)"
					+ " AND BRM.TYPE_OF_ROOM LIKE (%?2%) AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?3%) AND BRM.CREATED_DATE BETWEEN (?4) AND (?5) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getEmployeeCommunityCenterRoomBooking( String applicationStatus, String typeOfRoom, String roomApplicationNumber, Date fromDate, Date toDate );
	
	/**
	 * Gets the employee community center room booking.
	 *
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param roomApplicationNumber the room application number
	 * @param uuid the uuid
	 * @return the employee community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM"
					+ " INNER JOIN BK_BOOKINGS AS BB ON BB.BK_APPLICATION_NUMBER = BRM.COMMUNITY_APPLICATION_NUMBER"
					+ " WHERE BB.UUID = (?4) AND BRM.ROOM_APPLICATION_STATUS LIKE (%?1%) AND BRM.TYPE_OF_ROOM LIKE (%?2%)"
					+ " AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?3%) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getEmployeeCommunityCenterRoomBooking( String applicationStatus, String typeOfRoom, String roomApplicationNumber, String uuid );
	
	/**
	 * Gets the employee community center room booking.
	 *
	 * @param applicationStatus the application status
	 * @param typeOfRoom the type of room
	 * @param roomApplicationNumber the room application number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param uuid the uuid
	 * @return the employee community center room booking
	 */
	@Query(
			value = "SELECT * FROM BK_ROOMS_MODEL AS BRM"
					+ " INNER JOIN BK_BOOKINGS AS BB ON BB.BK_APPLICATION_NUMBER = BRM.COMMUNITY_APPLICATION_NUMBER"
					+ " WHERE BB.UUID = (?6) AND BRM.ROOM_APPLICATION_STATUS LIKE (%?1%) AND BRM.TYPE_OF_ROOM LIKE (%?2%)"
					+ " AND BRM.ROOM_APPLICATION_NUMBER LIKE (%?3%) AND BRM.CREATED_DATE BETWEEN (?4) AND (?5) ORDER BY BRM.ROOM_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<RoomsModel> getEmployeeCommunityCenterRoomBooking( String applicationStatus, String typeOfRoom, String roomApplicationNumber, Date fromDate, Date toDate, String uuid );
	

}
