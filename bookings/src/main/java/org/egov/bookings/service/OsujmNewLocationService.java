package org.egov.bookings.service;

import java.util.List;

import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.web.models.NewLocationRequest;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsujmNewLocationService.
 */
public interface OsujmNewLocationService {

	/**
	 * Adds the new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the osujm new location model
	 */
	public OsujmNewLocationModel addNewLocation(NewLocationRequest newLocationRequest);

	/**
	 * Update new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the osujm new location model
	 */
	public OsujmNewLocationModel updateNewLocation(NewLocationRequest newLocationRequest);
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee newlocation search
	 */
	public Booking getEmployeeNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);
	
	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen newlocation search
	 */
	public Booking getCitizenNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO);
	
	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	public Booking getAllCitizenNewlocation();
	
	/**
	 * Gets the new location document.
	 *
	 * @param venue the venue
	 * @param location the location
	 * @return the new location document
	 */
	public List<DocumentFields> getNewLocationDocument(String venue, String location);

}
