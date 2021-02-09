package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.contract.ApproverBean;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.CommonMasterFields;
import org.egov.bookings.contract.MasterRequest;
import org.egov.bookings.contract.MdmsSearchRequest;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.InventoryModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.model.user.UserSearchRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface MasterService.
 */
public interface MasterService {
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the park community inventory details
	 */
	public List< InventoryModel > getParkCommunityInventoryDetails(String venue, String sector);
	
	/**
	 * Creates the approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> createApprover(MasterRequest masterRequest);
	
	/**
	 * Update approver.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> updateApprover(MasterRequest masterRequest);
	
	/**
	 * Creates the OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> createOSBMFee(MasterRequest masterRequest);
	
	/**
	 * Update OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> updateOSBMFee(MasterRequest masterRequest);
	
	/**
	 * Creates the OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> createOSUJMFee(MasterRequest masterRequest);
	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> updateOSUJMFee(MasterRequest masterRequest);
	
	/**
	 * Creates the GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> createGFCPFee(MasterRequest masterRequest);
	
	/**
	 * Update GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> updateGFCPFee(MasterRequest masterRequest);
	
	/**
	 * Creates the PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> createPACCFee(MasterRequest masterRequest);
	
	/**
	 * Update PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the list
	 */
	public List<CommonMasterFields> updatePACCFee(MasterRequest masterRequest);
	
	/**
	 * Fetch all approver.
	 *
	 * @param userSearchRequest the user search request
	 * @return the list
	 */
	public List<BookingApprover> fetchAllApprover(UserSearchRequest userSearchRequest);
	
	/**
	 * Fetch all approver details.
	 *
	 * @param mdmsSearchRequest the mdms search request
	 * @return the list
	 */
	public List<ApproverBean> fetchAllApproverDetails(MdmsSearchRequest mdmsSearchRequest);
	
	/**
	 * Fetch all OSBM fee.
	 *
	 * @return the list
	 */
	public List<OsbmFeeModel> fetchAllOSBMFee();
	
	/**
	 * Fetch all OSUJM fee.
	 *
	 * @return the list
	 */
	public List<OsujmFeeModel> fetchAllOSUJMFee();
	
	/**
	 * Fetch all GFCP fee.
	 *
	 * @return the list
	 */
	public List<CommercialGroundFeeModel> fetchAllGFCPFee();
	
	/**
	 * Fetch all PACC fee.
	 *
	 * @return the list
	 */
	public List<ParkCommunityHallV1MasterModel> fetchAllPACCFee();
	
	/**
	 * Gets the users.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the users
	 */
	public List<UserDetails> getUsers(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);

}
