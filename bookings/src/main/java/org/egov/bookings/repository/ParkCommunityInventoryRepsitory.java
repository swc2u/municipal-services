package org.egov.bookings.repository;

import java.util.List;
import org.egov.bookings.model.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The Interface ParkCommunityInventoryRepsitory.
 */
@Repository
public interface ParkCommunityInventoryRepsitory extends JpaRepository<InventoryModel, Integer> {

	/**
	 * Gets the park community inventory details.
	 *
	 * @param venue  the venue
	 * @param sector the sector
	 * @return the park community inventory details
	 */
	@Query(value = "SELECT * FROM TM_PARK_COMMUNITY_HALL AS TPCH WHERE TPCH.VENUE_TYPE LIKE (%?1%) AND TPCH.SECTOR_NAME LIKE (%?2%)", nativeQuery = true)
	List<InventoryModel> getParkCommunityInventoryDetails(String venue, String sector);
}
