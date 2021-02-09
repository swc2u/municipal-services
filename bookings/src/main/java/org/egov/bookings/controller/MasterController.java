package org.egov.bookings.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.ApproverBean;
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.CommonMasterFields;
import org.egov.bookings.contract.DocumentFields;
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
import org.egov.bookings.service.MasterService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Auto-generated Javadoc
/**
 * The Class MasterController.
 */
@RestController
@RequestMapping("/master")
public class MasterController {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger( MasterController.class.getName() );
	
	/** The master service. */
	@Autowired
	private MasterService masterService;
	
	/**
	 * Gets the park community inventory details.
	 *
	 * @param documentfields the documentfields
	 * @return the park community inventory details
	 */
	@PostMapping(value = "/park/community/hall/inventory/_search")
	private ResponseEntity<?> getParkCommunityInventoryDetails(@RequestBody DocumentFields documentfields )
	{
		if (BookingsFieldsValidator.isNullOrEmpty(documentfields)) 
		{
			throw new IllegalArgumentException("Invalid documentfields");
		}
		List< InventoryModel > inventoryModelList = new ArrayList<>();
		try
		{
			inventoryModelList = masterService.getParkCommunityInventoryDetails(documentfields.getVenue(), documentfields.getSector());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getParkCommunityInventoryDetails " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(inventoryModelList);
	}
	
	
	/**
	 * Creates the approver.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping(value = "/approver/_create")
	public ResponseEntity<?> createApprover(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> approverModelList = masterService.createApprover(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in approver table");
			rs.setData(approverModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update approver.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/approver/_update")
	public ResponseEntity<?> updateApprover(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList())) 
		{
			throw new IllegalArgumentException("Invalid Approver List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getApproverList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid Approver id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> approverModelList = masterService.updateApprover(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in approver table");
			rs.setData(approverModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Creates the OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osbm/fee/_create")
	public ResponseEntity<?> createOSBMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> osbmFeeModelList = masterService.createOSBMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in OSBM Fee table");
			rs.setData(osbmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createOSBMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update OSBM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osbm/fee/_update")
	public ResponseEntity<?> updateOSBMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsbmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSBM Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> osbmFeeModelList = masterService.updateOSBMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in OSBM Fee table");
			rs.setData(osbmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Creates the OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osujm/fee/_create")
	public ResponseEntity<?> createOSUJMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> osujmFeeModelList = masterService.createOSUJMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in OSUJM Fee table");
			rs.setData(osujmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createOSUJMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/osujm/fee/_update")
	public ResponseEntity<?> updateOSUJMFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getOsujmFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid OSUJM Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> osujmFeeModelList = masterService.updateOSUJMFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in OSUJM Fee table");
			rs.setData(osujmFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateOSUJMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Creates the GFCP fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/gfcp/fee/_create")
	public ResponseEntity<?> createGFCPFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> gfcpFeeModelList = masterService.createGFCPFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in GFCP Fee table");
			rs.setData(gfcpFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createGFCPFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update OSUJM fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/gfcp/fee/_update")
	public ResponseEntity<?> updateGFCPFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getGfcpFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid GFCP Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> gfcpFeeModelList = masterService.updateGFCPFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in GFCP Fee table");
			rs.setData(gfcpFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updateGFCPFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Creates the PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/pacc/fee/_create")
	public ResponseEntity<?> createPACCFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> paccFeeModelList = masterService.createPACCFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data submitted in PACC Fee table");
			rs.setData(paccFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the createPACCFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);

	}
	
	/**
	 * Update PACC fee.
	 *
	 * @param masterRequest the master request
	 * @return the response entity
	 */
	@PostMapping("/pacc/fee/_update")
	public ResponseEntity<?> updatePACCFee(@RequestBody MasterRequest masterRequest) {
		
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest)) 
		{
			throw new IllegalArgumentException("Invalid masterRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee List");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(masterRequest.getPaccFeeList().get(0).getId())) 
		{
			throw new IllegalArgumentException("Invalid PACC Fee id");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<CommonMasterFields> paccFeeModelList = masterService.updatePACCFee(masterRequest);
			rs.setStatus("200");
			rs.setMessage("Data updated in PACC Fee table");
			rs.setData(paccFeeModelList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the updatePACCFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all approver.
	 *
	 * @param userSearchRequest the user search request
	 * @return the response entity
	 */
	@PostMapping("all/approver/_fetch")
	public ResponseEntity<?> fetchAllApprover(@RequestBody UserSearchRequest userSearchRequest ) {
		if (BookingsFieldsValidator.isNullOrEmpty(userSearchRequest)) {
			throw new IllegalArgumentException("Invalid userSearchRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(userSearchRequest.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<BookingApprover> bookingApproverList = masterService.fetchAllApprover(userSearchRequest);
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(bookingApproverList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApprover " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all approver details.
	 *
	 * @param mdmsSearchRequest the mdms search request
	 * @return the response entity
	 */
	@PostMapping("/approver/_fetch")
	public ResponseEntity<?> fetchAllApproverDetails(@RequestBody MdmsSearchRequest mdmsSearchRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(mdmsSearchRequest)) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(mdmsSearchRequest.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<ApproverBean> approverList = masterService.fetchAllApproverDetails(mdmsSearchRequest); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(approverList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllApproverDetails " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all OSBM fee.
	 *
	 * @return the response entity
	 */
	@PostMapping("osbm/fee/_fetch")
	public ResponseEntity<?> fetchAllOSBMFee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<OsbmFeeModel> osbmFeeList = masterService.fetchAllOSBMFee(); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllOSBMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all OSUJM fee.
	 *
	 * @return the response entity
	 */
	@PostMapping("osujm/fee/_fetch")
	public ResponseEntity<?> fetchAllOSUJMFee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<OsujmFeeModel> osbmFeeList = masterService.fetchAllOSUJMFee(); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllOSUJMFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}

	/**
	 * Fetch all GFCP fee.
	 *
	 * @return the response entity
	 */
	@PostMapping("gfcp/fee/_fetch")
	public ResponseEntity<?> fetchAllGFCPFee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<CommercialGroundFeeModel> osbmFeeList = masterService.fetchAllGFCPFee(); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllGFCPFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Fetch all PACC fee.
	 *
	 * @return the response entity
	 */
	@PostMapping("pacc/fee/_fetch")
	public ResponseEntity<?> fetchAllPACCFee() {
		ResponseModel rs = new ResponseModel();
		try {
			List<ParkCommunityHallV1MasterModel> osbmFeeList = masterService.fetchAllPACCFee(); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(osbmFeeList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the fetchAllPACCFee " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Gets the users.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the users
	 */
	@PostMapping("user/_search")
	public ResponseEntity<?> getUsers(@RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
		{
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRoleCode())) 
		{
			throw new IllegalArgumentException("Invalid approver");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo())) 
		{
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		ResponseModel rs = new ResponseModel();
		try {
			List<UserDetails> userList = masterService.getUsers(searchCriteriaFieldsDTO); 
			rs.setStatus("200");
			rs.setMessage("Success");
			rs.setData(userList);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getUsers " + e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(rs);
	}
}
