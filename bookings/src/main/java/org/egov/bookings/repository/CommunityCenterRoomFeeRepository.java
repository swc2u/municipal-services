package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;

import org.egov.bookings.model.RoomMasterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityCenterRoomFeeRepository extends JpaRepository<RoomMasterModel, String>{
	
	/**
	 * Find by id.
	 *
	 * @param bookingVenue the booking venue
	 * @return the room master model
	 */
	RoomMasterModel findById(String bookingVenue);

	/**
	 * Find by type of room and sector name and community center name.
	 *
	 * @param typeOfRoom the type of room
	 * @param sector the sector
	 * @param communityCenterName the community center name
	 * @return the list
	 */
	List<RoomMasterModel> findByTypeOfRoomAndSectorNameAndCommunityCenterName(String typeOfRoom,
			String sector, String communityCenterName);
	
	/**
	 * Find room master list.
	 *
	 * @param currentDate the current date
	 * @param communityCenterName the community center name
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM BK_ROOM_MASTER WHERE FROM_DATE <=cast((?1) AS timestamp) AND TO_DATE >= cast((?1) AS timestamp) "
					+ "AND COMMUNITY_CENTER_NAME = (?2) AND TYPE_OF_ROOM in ('AC','NON-AC')",
			nativeQuery = true )
			List<RoomMasterModel> findRoomMasterList( Date currentDate, String communityCenterName );


	
	
	
	
	/**
	 * Find by sector name and type of room and community center name.
	 *
	 * @param sector the sector
	 * @param typeOfRomm the type of romm
	 * @param communityCenterName the community center name
	 * @return the list
	 */
	List<RoomMasterModel> findBySectorNameAndTypeOfRoomAndCommunityCenterName(String sector, String typeOfRomm, String communityCenterName);
	
	/**
	 * Gets the room master list.
	 *
	 * @param currentDate the current date
	 * @param communityCenterName the community center name
	 * @return the room master list
	 */
	@Query(
			value = "SELECT * FROM BK_ROOM_MASTER WHERE FROM_DATE <=cast((?1) AS timestamp) AND COMMUNITY_CENTER_NAME = (?2) AND TYPE_OF_ROOM in ('AC','NON-AC')",
			nativeQuery = true )
			List<RoomMasterModel> getRoomMasterList( Date currentDate, String communityCenterName );
}
