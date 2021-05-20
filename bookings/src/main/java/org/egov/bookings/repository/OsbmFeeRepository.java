package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.OsbmFeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OsbmFeeRepository extends JpaRepository<OsbmFeeModel, String> {

	/**
	 * Find by village city and residential commercial and storage and duration in months and construction type.
	 *
	 * @param villageCity the village city
	 * @param residentialCommercial the residential commercial
	 * @param storage the storage
	 * @param durationInMonths the duration in months
	 * @param constructionType the construction type
	 * @return the osbm fee model
	 */
	List<OsbmFeeModel> findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(
			String villageCity, String residentialCommercial, String storage, String durationInMonths,
			String constructionType);
	
	/**
	 * Find OSBM fee records by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_osbm_fee LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<OsbmFeeModel> findOSBMFeeRecordsByLimit( int offSet );
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the osbm fee model
	 */
	public OsbmFeeModel findById(String id);

}
