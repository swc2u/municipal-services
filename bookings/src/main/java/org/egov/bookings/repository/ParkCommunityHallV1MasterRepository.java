package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The Interface ParkCommunityHallV1MasterRepository.
 */
@Repository
public interface ParkCommunityHallV1MasterRepository extends JpaRepository<ParkCommunityHallV1MasterModel, String> {

	/**
	 * Find by id.
	 *
	 * @param bookingVenue the booking venue
	 * @return the park community hall V 1 master model
	 */
	ParkCommunityHallV1MasterModel findById(String bookingVenue);

	/**
	 * Find by venue type and sector and is active.
	 *
	 * @param venueType the venue type
	 * @param sector the sector
	 * @param isActive the is active
	 * @return the list
	 */
	List<ParkCommunityHallV1MasterModel> findByVenueTypeAndSectorAndIsActive(String venueType, String sector, boolean isActive);
	
	/**
	 * Find pacc fee records by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_park_community_hall_v1 LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<ParkCommunityHallV1MasterModel> findPaccFeeRecordsByLimit( int offSet );
	
	/**
	 * Find by venue type.
	 *
	 * @param venueType the venue type
	 * @return the list
	 */
	public List<ParkCommunityHallV1MasterModel> findByVenueType(String venueType);
	
	/**
	 * Find PACC fee details.
	 *
	 * @param name the name
	 * @param sector the sector
	 * @param venueType the venue type
	 * @return the park community hall V 1 master model
	 */
	@Query(
			value = "SELECT * FROM bk_park_community_hall_v1 where name = (?1) and sector = (?2) and venue_type = (?3)",
			nativeQuery = true )
			ParkCommunityHallV1MasterModel findPACCFeeDetails( String name, String sector, String venueType );
}
