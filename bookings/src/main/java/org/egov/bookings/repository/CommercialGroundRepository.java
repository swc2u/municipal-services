package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.CommercialGroundFeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommercialGroundRepository.
 */
@Repository
public interface CommercialGroundRepository extends JpaRepository<CommercialGroundFeeModel, String> {

	/**
	 * Find by locality and category.
	 *
	 * @param locality the locality
	 * @param category the category
	 * @return the commercial ground fee model
	 */
	CommercialGroundFeeModel findByLocalityAndCategory(String locality, String category);

	/**
	 * Find by booking venue and category.
	 *
	 * @param bookingVenue the booking venue
	 * @param category the category
	 * @return the list of commercial ground fee model
	 */
	List<CommercialGroundFeeModel> findByBookingVenueAndCategory(String bookingVenue, String category);
	
	/**
	 * Find GFCP fee records by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_commercial_ground_fee LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<CommercialGroundFeeModel> findGFCPFeeRecordsByLimit( int offSet );
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the commercial ground fee model
	 */
	public CommercialGroundFeeModel findById(String id);

}
