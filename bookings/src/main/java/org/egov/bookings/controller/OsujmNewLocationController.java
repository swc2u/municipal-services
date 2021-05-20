package org.egov.bookings.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.service.OsujmNewLocationService;
import org.egov.bookings.service.impl.EnrichmentService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/newLocation/")
public class OsujmNewLocationController {

	@Autowired
	private OsujmNewLocationService newLocationService;
	
	@Autowired
	private Environment env;

	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	BookingsFieldsValidator bookingsFieldsValidator;
	
	
	private static final Logger LOGGER = LogManager.getLogger( OsujmNewLocationController.class.getName() );
	
	
	/**
	 * Adds the new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the response entity
	 */
	@PostMapping("_create")
	private ResponseEntity<?> addNewLocation(
			@RequestBody NewLocationRequest newLocationRequest) {
		
		bookingsFieldsValidator.validateNewLocationRequest(newLocationRequest);
		OsujmNewLocationModel osujmNewLocationModel = newLocationService
				.addNewLocation(newLocationRequest);
		ResponseModel rs = new ResponseModel();
		if (osujmNewLocationModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while creating New Location");
			rs.setData(osujmNewLocationModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("New Location Created Successfully");
			rs.setData(osujmNewLocationModel);
		}

		return ResponseEntity.ok(rs);
	}
	
	

	@PostMapping("_update")
	private ResponseEntity<?> updateNewLocation(
			@RequestBody NewLocationRequest newLocationRequest) {
		
		bookingsFieldsValidator.validateNewLocationRequestForUpdate(newLocationRequest);
		OsujmNewLocationModel osujmNewLocationModel = newLocationService
				.updateNewLocation(newLocationRequest);
		ResponseModel rs = new ResponseModel();
		if (osujmNewLocationModel == null) {
			rs.setStatus("400");
			rs.setMessage("Error while Updating New Location");
			rs.setData(osujmNewLocationModel);
		} else {
			rs.setStatus("200");
			rs.setMessage("New Location Updated Successfully");
			rs.setData(osujmNewLocationModel);
		}
		return ResponseEntity.ok(rs);
	}
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee newlocation search
	 */
	@PostMapping(value = "/employee/osujm/_search")
	public ResponseEntity<?> getEmployeeNewlocationSearch( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		Booking booking = new Booking();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getUuid())) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			booking = newLocationService.getEmployeeNewlocationSearch(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getEmployeeNewlocationSearch " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen newlocation search
	 */
	@PostMapping(value = "/citizen/osujm/_search")
	public ResponseEntity<?> getCitizenNewlocationSearch( @RequestBody SearchCriteriaFieldsDTO searchCriteriaFieldsDTO )
	{
		Booking booking = new Booking();
		try
		{
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) 
			{
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getUuid())) 
			{
				throw new IllegalArgumentException("Invalid uuId");
			}
			booking = newLocationService.getCitizenNewlocationSearch(searchCriteriaFieldsDTO);
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	@PostMapping(value = "/citizen/osujm/_all")
	public ResponseEntity<?> getAllCitizenNewlocation()
	{
		Booking booking = new Booking();
		try
		{
			booking = newLocationService.getAllCitizenNewlocation();
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getAllCitizenNewlocation " + e);
		}
		return ResponseEntity.ok(booking);
	}
	
	/**
	 * Gets the new location document.
	 *
	 * @param documentfields the documentfields
	 * @return the new location document
	 */
	@PostMapping(value = "/osujm/_document")
	public ResponseEntity<?> getNewLocationDocument(@RequestBody DocumentFields documentfields)
	{
		if (BookingsFieldsValidator.isNullOrEmpty(documentfields)) 
		{
			throw new IllegalArgumentException("Invalid documentfields");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(documentfields.getVenue())) 
		{
			throw new IllegalArgumentException("Invalid venue");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(documentfields.getSector())) 
		{
			throw new IllegalArgumentException("Invalid sector");
		}
		List<DocumentFields> documentsList = new ArrayList<>();
		try
		{
			documentsList = newLocationService.getNewLocationDocument(documentfields.getVenue(), documentfields.getSector());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getNewLocationDocument " + e);
		}
		return ResponseEntity.ok(documentsList);
	}
	
}
