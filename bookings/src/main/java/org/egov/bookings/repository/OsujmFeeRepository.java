package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.repository.querybuilder.BookingsQueryBuilder;
import org.egov.bookings.utils.BookingsConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OsujmFeeRepository extends JpaRepository<OsujmFeeModel, String> {

	@Query(value = BookingsQueryBuilder.FIND_JURISDICTION_AMOUNT, nativeQuery = true)
	List<OsujmFeeModel> findJurisdictionFee(@Param(BookingsConstants.AREA) Long area, @Param(BookingsConstants.SECTOR)String sector);

	/**
	 * Find OSUJM fee records by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_osujm_fee LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<OsujmFeeModel> findOSUJMFeeRecordsByLimit( int offSet );
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the osujm fee model
	 */
	public OsujmFeeModel findById(String id);

}
