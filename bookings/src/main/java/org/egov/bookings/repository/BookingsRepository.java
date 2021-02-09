package org.egov.bookings.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.bookings.model.BookingsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface BookingsRepository.
 */
@Repository
public interface BookingsRepository
		extends CrudRepository<BookingsModel, String> {
	
	
	/**
	 * Count by tenant id and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @return the int
	 */
	int countByTenantIdAndUuid( String tenantId, String uuId );
	
	/**
	 * Find by tenant id and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidOrderByBkApplicationNumberDesc( String tenantId, String uuId );
	
	/**
	 * Find by tenant id and bk application number and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc(String tenantId, String applicationNumber, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and uuid and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndUuid( String tenantId, String applicationStatus, String uuId );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuid( String tenantId, String applicationStatus, String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and uuid order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidOrderByBkApplicationNumberDesc( String tenantId,String mobileNumber, String uuId );
	
	/**
	 * Find by tenant id and bk mobile number and uuid and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and uuid and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String uuId, Date fromDate, Date toDate );
	
	/**
	 * Count by tenant id and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param sectorList the sector list
	 * @return the int
	 */
	int countByTenantIdAndBkSectorIn( String tenantId, List< String > sectorList );
	
	/**
	 * Count by tenant id and bk booking type and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the int
	 */
	int countByTenantIdAndBkBookingTypeAndBkSectorIn( String tenantId, String bookingType , List< String > sectorList );

	/**
	 * Count by tenant id and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumberList the application number list
	 * @return the int
	 */
	int countByTenantIdAndBkApplicationNumberIn( String tenantId, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberIn( String tenantId, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationNumberIn( String tenantId, String applicationNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkApplicationNumberIn( String tenantId, String applicationNumber, String applicationStatus, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberIn(String tenantId, String applicationNumber, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application status and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkApplicationNumberIn( String tenantId, String applicationStatus, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberIn( String tenantId, String applicationStatus, String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk mobile number and bk application number in.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkApplicationNumberIn( String tenantId,String mobileNumber, List< String > applicationNumberList );
	
	/**
	 * Find by tenant id and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param aplicationNumberList the aplication number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, List< String > aplicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk application number in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param applicationNumberList the application number list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkApplicationNumberInAndBkDateCreatedBetween( String tenantId, String mobileNumber, List< String > applicationNumberList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate ); 
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk booking type and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, String bookingType, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk mobile number and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkMobileNumberAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk booking type and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkBookingTypeAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String bookingType, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Count by tenant id and bk booking type and uuid.
	 *
	 * @param tenantId the tenant id
	 * @param bookingType the booking type
	 * @param uuid the uuid
	 * @return the int
	 */
	int countByTenantIdAndBkBookingTypeAndUuid( String tenantId, String bookingType, String uuid );
	
	/**
	 * Find by tenant id and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param sectorList the sector list
	 * @return the list
	 */
	List< BookingsModel > findByTenantIdAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application number and bk application status and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, String bookingType );

	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc(String tenantId, String applicationNumber, String mobileNumber, String uuId, String bookingType );

	/**
	 * Find by tenant id and bk application number and bk mobile number and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String mobileNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationStatus, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and uuid and bk booking type order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeOrderByBkApplicationNumberDesc( String tenantId,String mobileNumber, String uuId, String bookingType );
	
	/**
	 * Find by tenant id and bk mobile number and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and uuid and bk booking type and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param uuId the uu id
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndUuidAndBkBookingTypeAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String uuId, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk application status and bk mobile number and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String applicationStatus, String mobileNumber, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application number and bk mobile number and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, String mobileNumber, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application number and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param applicationNumber the application number
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String applicationNumber, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk application status and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkSectorIn( String tenantId, String applicationStatus, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk sector in.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorIn( String tenantId, String applicationStatus, String mobileNumber, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk application status and bk mobile number and bk sector in and bk date created between.
	 *
	 * @param tenantId the tenant id
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkApplicationStatusAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetween( String tenantId, String applicationStatus, String mobileNumber, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk mobile number and bk sector in order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkSectorInOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, List< String > sectorList );
	
	/**
	 * Find by tenant id and bk mobile number and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param mobileNumber the mobile number
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkMobileNumberAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, String mobileNumber, List< String > sectorList, Date fromDate, Date toDate );
	
	/**
	 * Find by tenant id and bk sector in and bk date created between order by bk application number desc.
	 *
	 * @param tenantId the tenant id
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the list
	 */
	List<BookingsModel> findByTenantIdAndBkSectorInAndBkDateCreatedBetweenOrderByBkApplicationNumberDesc( String tenantId, List< String > sectorList, Date fromDate, Date toDate );
	
	
	/**
	 * Gets the citizen search booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param uuid the uuid
	 * @return the citizen search booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS WHERE BK_APPLICATION_NUMBER LIKE (%?1%) AND BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND BK_MOBILE_NUMBER LIKE (%?3%) AND BK_BOOKING_TYPE LIKE (%?4%) AND UUID LIKE (?5) ORDER BY BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, String uuid );
	
	/**
	 * Gets the citizen search booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param uuid the uuid
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the citizen search booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS WHERE BK_APPLICATION_NUMBER LIKE (%?1%) AND BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND BK_MOBILE_NUMBER LIKE (%?3%) AND BK_BOOKING_TYPE LIKE (%?4%) AND UUID LIKE (?5) AND BK_DATE_CREATED BETWEEN (?6) AND (?7) ORDER BY BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, String uuid, Date fromDate, Date toDate );
	

	/**
	 * Gets the citizen search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @param uuid the uuid
	 * @return the citizen search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS WHERE BK_APPLICATION_NUMBER LIKE (%?1%) AND BK_APPLICATION_STATUS LIKE (%?2%) " 
					+ "AND BK_MOBILE_NUMBER LIKE (%?3%) AND (BK_BOOKING_TYPE = (?4) OR BK_BOOKING_TYPE = (?5)) AND UUID LIKE (?6) ORDER BY BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType, String uuid );
	
	
	/**
	 * Gets the citizen search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @param uuid the uuid
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the citizen search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS WHERE BK_APPLICATION_NUMBER LIKE (%?1%) AND BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND BK_MOBILE_NUMBER LIKE (%?3%) AND (BK_BOOKING_TYPE = (?4) OR BK_BOOKING_TYPE = (?5)) AND UUID LIKE (?6) AND BK_DATE_CREATED BETWEEN (?7) AND (?8) ORDER BY BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getCitizenSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType, String uuid, Date fromDate, Date toDate );
	
	
	/**
	 * Find by bk application number.
	 *
	 * @param bkApplicationNumber the bk application number
	 * @return the bookings model
	 */
	BookingsModel findByBkApplicationNumber(String bkApplicationNumber);

	/**
	 * Find by bk booking venue and bk to date and bk from date and bk booking type.
	 *
	 * @param bookingVenue the booking venue
	 * @param toDate the to date
	 * @param fromDate the from date
	 * @param bookingType the booking type
	 * @return the bookings model
	 */
	BookingsModel findByBkBookingVenueAndBkToDateAndBkFromDateAndBkBookingType(String bookingVenue,
			java.sql.Date toDate, java.sql.Date fromDate, String bookingType);

	
	/**
	 * Gets the employee search booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param applicationNumberSet the application number set
	 * @return the employee search booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE LIKE (%?4%) AND TB.BK_SECTOR IN (?5) AND TB.BK_APPLICATION_NUMBER IN (?6) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, List< String > sectorList, Set< String > applicationNumberSet );
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param sectorList the sector list
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param applicationNumberSet the application number set
	 * @return the employee search booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE LIKE (%?4%) AND TB.BK_SECTOR IN (?5) AND TB.BK_DATE_CREATED BETWEEN (?6) AND (?7) AND TB.BK_APPLICATION_NUMBER IN (?8) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, List< String > sectorList, Date fromDate, Date toDate, Set< String > applicationNumberSet );
	
	/**
	 * Gets the employee search BWT booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param applicationNumberSet the application number set
	 * @return the employee search BWT booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE LIKE (%?4%) AND TB.BK_APPLICATION_NUMBER IN (?5) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchBWTBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, Set< String > applicationNumberSet );
	
	/**
	 * Gets the employee search BWT booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param applicationNumberSet the application number set
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee search BWT booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE LIKE (%?4%) AND TB.BK_APPLICATION_NUMBER IN (?5) AND TB.BK_DATE_CREATED BETWEEN (?6) AND (?7) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchBWTBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, Set< String > applicationNumberSet, Date fromDate, Date toDate );
	
	/**
	 * Gets the employee search GFCP booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @return the employee search GFCP booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE = (?4) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchGFCPBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType );
	
	/**
	 * Gets the employee search GFCP booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param bookingType the booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee search GFCP booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND TB.BK_BOOKING_TYPE = (?4) AND TB.BK_DATE_CREATED BETWEEN (?5) AND (?6) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchGFCPBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String bookingType, Date fromDate, Date toDate );
	
	/**
	 * Gets the employee search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @return the employee search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND (TB.BK_BOOKING_TYPE = (?4) OR TB.BK_BOOKING_TYPE = (?5)) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType );
	
	/**
	 * Gets the employee search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND (TB.BK_BOOKING_TYPE = (?4) OR TB.BK_BOOKING_TYPE = (?5)) AND TB.BK_DATE_CREATED BETWEEN (?6) AND (?7) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType, Date fromDate, Date toDate );
	
	/**
	 * Gets the employee search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @param applicationNumberSet the application number set
	 * @return the employee search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND (TB.BK_BOOKING_TYPE = (?4) OR TB.BK_BOOKING_TYPE = (?5)) AND TB.BK_APPLICATION_NUMBER IN (?6) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType, Set< String > applicationNumberSet );
	
	/**
	 * Gets the employee search PACC booking.
	 *
	 * @param applicationNumber the application number
	 * @param applicationStatus the application status
	 * @param mobileNumber the mobile number
	 * @param parksBookingType the parks booking type
	 * @param communityCenterBookingType the community center booking type
	 * @param applicationNumberSet the application number set
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the employee search PACC booking
	 */
	@Query(
			value = "SELECT * FROM BK_BOOKINGS AS TB WHERE TB.BK_APPLICATION_NUMBER LIKE (%?1%) AND TB.BK_APPLICATION_STATUS LIKE (%?2%) "
					+ "AND TB.BK_APPLICATION_STATUS != 'INITIATED' AND TB.BK_MOBILE_NUMBER LIKE (%?3%) AND (TB.BK_BOOKING_TYPE = (?4) OR TB.BK_BOOKING_TYPE = (?5)) AND TB.BK_APPLICATION_NUMBER IN (?6) AND TB.BK_DATE_CREATED BETWEEN (?7) AND (?8) ORDER BY TB.BK_APPLICATION_NUMBER DESC",
			nativeQuery = true )
			List<BookingsModel> getEmployeeSearchPACCBooking( String applicationNumber, String applicationStatus, String mobileNumber
					, String parksBookingType, String communityCenterBookingType, Set< String > applicationNumberSet, Date fromDate, Date toDate );
}