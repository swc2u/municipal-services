package org.egov.bookings.service;

import java.util.List;
import java.util.Map;

import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RefundTransactionRequest;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.web.models.BookingsRequest;

/**
 * The Interface BookingsService.
 */
public interface BookingsService {

	/**
	 * Save.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel save(BookingsRequest bookingsRequest);

	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	public Booking getCitizenSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
	
	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	public Booking getEmployeeSearchBooking( SearchCriteriaFieldsDTO searchCriteriaFieldsDTO );
	
	/**
	 * Update.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	public BookingsModel update(BookingsRequest bookingsRequest);
	
	/**
	 * Employee records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	public Map< String, Integer > employeeRecordsCount( String tenantId, String uuid, BookingsRequest bookingsRequest );
	
	/**
	 * Citizen records count.
	 *
	 * @param tenantId the tenant id
	 * @param uuid the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	public Map< String, Integer > citizenRecordsCount( String tenantId, String uuid, BookingsRequest bookingsRequest );
	
	/**
	 * Gets the workflow process instances.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @param criteria the criteria
	 * @return the workflow process instances
	 */
	public Object getWorkflowProcessInstances(RequestInfoWrapper requestInfoWrapper, ProcessInstanceSearchCriteria criteria);

	/**
	 * Checks if is booking exists.
	 *
	 * @param bkApplicationNumber the bk application number
	 * @return true, if is booking exists
	 */
	public boolean isBookingExists(String bkApplicationNumber);


	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	public List<BookingApprover> fetchAllApprover();

	
	/**
	 * Gets the assignee.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the assignee
	 */
	public List<UserDetails> getAssignee(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);

	/**
	 * Persist refund status.
	 *
	 * @param refundTransactionRequest the refund transaction request
	 */
	public void persistRefundStatus(RefundTransactionRequest refundTransactionRequest);
	
	/**
	 * Gets the community center booking search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the community center booking search
	 */
	public Booking getCommunityCenterBookingSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);
	
	/**
	 * Gets the citizen community center room booking search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen community center room booking search
	 */
	public Booking getCitizenCommunityCenterRoomBookingSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);
	
	/**
	 * Gets the employee community center room booking search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee community center room booking search
	 */
	public Booking getEmployeeCommunityCenterRoomBookingSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);
}
