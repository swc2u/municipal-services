package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.model.OsujmNewLocationModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsujmNewLocationRepository.
 */
@Repository
public interface OsujmNewLocationRepository extends CrudRepository<OsujmNewLocationModel, String> {
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param applicationNumberSet the application number set
	 * @return the employee newlocation search
	 */
	@Query(
			value = "SELECT * FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.APPLICATION_NUMBER LIKE (%?1%) AND TONL.APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TONL.APPLICATION_STATUS != 'INITIATED' AND TONL.CONTACT LIKE (%?3%) AND TONL.SECTOR IN (?4) AND TONL.APPLICATION_NUMBER IN (?5) ORDER BY TONL.APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<OsujmNewLocationModel> getEmployeeNewlocationSearch( String applicationNumber, String applicationStatus, String mobileNumber
					, List< String > sectorList, Set< String > applicationNumberSet );
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param applicationNumberSet the application number set
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee newlocation search
	 */
	@Query(
			value = "SELECT * FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.APPLICATION_NUMBER LIKE (%?1%) AND TONL.APPLICATION_STATUS LIKE (%?2%) " 
					+ "AND TONL.APPLICATION_STATUS != 'INITIATED' AND TONL.CONTACT LIKE (%?3%) AND TONL.SECTOR IN (?4) AND TONL.APPLICATION_NUMBER IN (?5) AND TONL.DATE_CREATED BETWEEN (?6) AND (?7) ORDER BY TONL.APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<OsujmNewLocationModel> getEmployeeNewlocationSearch( String applicationNumber, String applicationStatus, String mobileNumber
					, List< String > sectorList, Set< String > applicationNumberSet, Date fromDate, Date toDate );
	
	
	
	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuid the uuid
	 * @return the citizen newlocation search
	 */
	@Query(
			value = "SELECT * FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.APPLICATION_NUMBER LIKE (%?1%) AND TONL.APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TONL.CONTACT LIKE (%?3%) AND TONL.UUID = (?4) ORDER BY TONL.APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<OsujmNewLocationModel> getCitizenNewlocationSearch( String applicationNumber, String applicationStatus, String mobileNumber
					, String uuid );
	
	
	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuid the uuid
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the citizen newlocation search
	 */
	@Query(
			value = "SELECT * FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.APPLICATION_NUMBER LIKE (%?1%) AND TONL.APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TONL.CONTACT LIKE (%?3%) AND TONL.UUID = (?4) AND TONL.DATE_CREATED BETWEEN (?5) AND (?6) ORDER BY TONL.APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<OsujmNewLocationModel> getCitizenNewlocationSearch( String applicationNumber, String applicationStatus, String mobileNumber
					, String uuid, Date fromDate, Date toDate );
	
	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	@Query(
			value = "SELECT * FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.APPLICATION_STATUS = 'PUBLISHED' ORDER BY TONL.APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<OsujmNewLocationModel> getAllCitizenNewlocation();
	
	
	/**
	 * Find application number.
	 *
	 * @param localityAddress the locality address
	 * @param sector the sector
	 * @return the string
	 */
	@Query(
			value = "SELECT APPLICATION_NUMBER FROM BK_OSUJM_NEW_LOCATION AS TONL WHERE TONL.LOCALITY_ADDRESS = (?1) AND TONL.SECTOR = (?2)",
			nativeQuery = true )
			String findApplicationNumber(String localityAddress, String sector);

	OsujmNewLocationModel findByApplicationNumber(String applicationNumber);
	

}
