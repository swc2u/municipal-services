package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.transaction.Transactional;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.Booking;
import org.egov.bookings.contract.DocumentFields;
import org.egov.bookings.contract.Message;
import org.egov.bookings.contract.MessagesResponse;
import org.egov.bookings.contract.NewLocationKafkaRequest;
import org.egov.bookings.contract.OsujmNewLocationFields;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.OsujmNewLocationModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsujmNewLocationRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.OsujmNewLocationService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.NewLocationCreateDate;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.NewLocationRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class OsujmNewLocationServiceImpl.
 */
@Service
@Transactional
public class OsujmNewLocationServiceImpl implements OsujmNewLocationService{

	/** The bookings service. */
	@Autowired
	private BookingsService bookingsService;
	
	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;
	
	/** The config. */
	@Autowired
	private BookingsConfiguration config;
	
	/** The workflow integrator. */
	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	/** The new location repository. */
	@Autowired
	OsujmNewLocationRepository newLocationRepository;
	
	/** The common repository. */
	@Autowired
	CommonRepository commonRepository;
	
	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;
	
	/** The bookings service impl. */
	@Autowired
	private BookingsServiceImpl bookingsServiceImpl;
	
	@Autowired
	private BookingsProducer bookingsProducer; 

	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(OsujmNewLocationServiceImpl.class.getName());
	
	/**
	 * Adds the new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the osujm new location model
	 */
	@Override
	public OsujmNewLocationModel addNewLocation(NewLocationRequest newLocationRequest) {
		try
		{
			boolean flag = bookingsService.isBookingExists(newLocationRequest.getNewLocationModel().getApplicationNumber());

			if (!flag)
				enrichmentService.enrichNewLocationCreateRequest(newLocationRequest);

			if (config.getIsExternalWorkFlowEnabled()) {
				if (!flag)
					workflowIntegrator.callWorkFlow(newLocationRequest);
			}
			enrichmentService.enrichNewLocationDetails(newLocationRequest);
			NewLocationKafkaRequest newLocationKafkaRequest = enrichmentService.enrichKafkaForNewLocation(newLocationRequest);
			bookingsProducer.push(config.getSaveNewLocationTopic(), newLocationKafkaRequest);
			//osujmNewLocationModel = newLocationRepository.save(newLocationRequest.getNewLocationModel());
			if (!BookingsFieldsValidator.isNullOrEmpty(newLocationRequest.getNewLocationModel())) {
				bookingsProducer.push(config.getSaveNLUJMBookingSMSTopic(), newLocationRequest);
			}
		}
		catch (Exception e) {
			throw new CustomException("NEW_LOCATION_SAVE_ERROR",e.getLocalizedMessage());
		}
		return newLocationRequest.getNewLocationModel();

	}
	
	/**
	 * Prepare application status.
	 *
	 * @param requestInfo the request info
	 * @param osujmNewLocationModel the osujm new location model
	 * @return the string
	 */
	public String prepareApplicationStatus(RequestInfo requestInfo, OsujmNewLocationModel osujmNewLocationModel) {
		MessagesResponse messageResponse = bookingsServiceImpl.getLocalizationMessage(requestInfo);
		String applicationStatus = "";
		String status = "";
		if(!BookingsFieldsValidator.isNullOrEmpty(messageResponse))
		{
			if(BookingsConstants.BUSINESS_SERVICE_NLUJM.equals(osujmNewLocationModel.getBusinessService())) {
				applicationStatus = BookingsConstants.BK_WF_NLUJM + osujmNewLocationModel.getApplicationStatus();
			}
			for (Message message : messageResponse.getMessages()) {
				if(message.getCode().equals(applicationStatus)){
					status = message.getMessage();
				}
			}
		}
		return status;
	}

	/**
	 * Update new location.
	 *
	 * @param newLocationRequest the new location request
	 * @return the osujm new location model
	 */
	@Override
	public OsujmNewLocationModel updateNewLocation(NewLocationRequest newLocationRequest) {

		String businessService = newLocationRequest.getNewLocationModel().getBusinessService();
		
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(newLocationRequest);

		// bookingsRequest.getBookingsModel().setUuid(bookingsRequest.getRequestInfo().getUserInfo().getUuid());
		OsujmNewLocationModel newLocaltionModel = null;
		try {
			if (!BookingsConstants.APPLY.equals(newLocationRequest.getNewLocationModel().getAction())
					&& BookingsConstants.BUSINESS_SERVICE_NLUJM.equals(businessService)) {

				newLocaltionModel = enrichmentService.enrichNlujmDetails(newLocationRequest);
				newLocationRequest.setNewLocationModel(newLocaltionModel);
				NewLocationKafkaRequest newLocationKafkaRequest = enrichmentService
						.enrichKafkaForNewLocation(newLocationRequest);
				bookingsProducer.push(config.getUpdateNewLocationTopic(), newLocationKafkaRequest);
				//newLocaltionModel = newLocationRepository.save(newLocaltionModel);
			}
			else {
				NewLocationKafkaRequest newLocationKafkaRequest = enrichmentService
						.enrichKafkaForNewLocation(newLocationRequest);
				bookingsProducer.push(config.getUpdateNewLocationTopic(), newLocationKafkaRequest);
				// newLocationRepository.save(newLocationRequest.getNewLocationModel());
				newLocaltionModel = newLocationRequest.getNewLocationModel();
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(newLocaltionModel)) {
				bookingsProducer.push(config.getUpdateNLUJMBookingSMSTopic(), newLocationRequest);
			}
		} catch (Exception e) {
			throw new CustomException("OSUJM_NEW_LOCATION_ERROR", e.getLocalizedMessage());
		}
		return newLocationRequest.getNewLocationModel();
	}
	
	/**
	 * Gets the employee newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee newlocation search
	 */
	@Override
	public Booking getEmployeeNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<OsujmNewLocationModel> osujmNewLocationModelList = new ArrayList<>();
		Set<OsujmNewLocationModel> osujmNewLocationModelSet = new HashSet<>();
		List<?> documentList = new ArrayList<>();
		List<DocumentFields> newLocationDocumentList = new ArrayList<>();
		Set<String> applicationNumberSet = new HashSet<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo()) 
					|| BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo().getUserInfo())) {
				throw new IllegalArgumentException("Invalid request info details");
			}
			List< Role > roles = searchCriteriaFieldsDTO.getRequestInfo().getUserInfo().getRoles();
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			
			if (BookingsFieldsValidator.isNullOrEmpty(roles)) {
				throw new IllegalArgumentException("Invalid roles");
			}
			for (Role role : roles) {
				if(!BookingsConstants.CITIZEN.equals(role.getCode()) && !BookingsConstants.EMPLOYEE.equals(role.getCode())) {
					applicationNumberSet.addAll(commonRepository.findApplicationNumber(role.getCode()));
				}
			}
			for (Role role : roles) {
				if(!BookingsConstants.CITIZEN.equals(role.getCode()) && !BookingsConstants.EMPLOYEE.equals(role.getCode()) ) {
					
					if(BookingsConstants.MCC_APPROVER.equals(role.getCode()) || BookingsConstants.OSD_APPROVER.equals(role.getCode()) || BookingsConstants.ADMIN_APPROVER.equals(role.getCode()))
					{
						List<String> sectorList = commonRepository.findSectorList(uuid);
						if (sectorList == null || sectorList.isEmpty()) {
							return booking;
						}
						if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							osujmNewLocationModelSet.addAll( newLocationRepository.getEmployeeNewlocationSearch(applicationNumber,
									applicationStatus, mobileNumber, sectorList, applicationNumberSet));
						}
						else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							osujmNewLocationModelSet.addAll( newLocationRepository.getEmployeeNewlocationSearch(applicationNumber,
									applicationStatus, mobileNumber, sectorList, applicationNumberSet, fromDate, toDate ));
						}
					}
				}
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber) && !BookingsFieldsValidator.isNullOrEmpty(osujmNewLocationModelSet)) {
				documentList = commonRepository.findDocuments(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					String[] strArray = documentStrArray[1].split("/");
					documentFields.setFileName(strArray[strArray.length - 1].substring(13,(strArray[strArray.length - 1].length() - 2)));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					newLocationDocumentList.add(documentFields);
				}
			}
			osujmNewLocationModelList.addAll(osujmNewLocationModelSet);
			Collections.sort(osujmNewLocationModelList,new NewLocationCreateDate());  
			Collections.reverse(osujmNewLocationModelList);
			booking.setDocumentList(newLocationDocumentList);
			booking.setOsujmNewLocationModelList(osujmNewLocationModelList);
			booking.setBookingsCount(osujmNewLocationModelSet.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getEmployeeNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the citizen newlocation search.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen newlocation search
	 */
	@Override
	public Booking getCitizenNewlocationSearch(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		Booking booking = new Booking();
		List<OsujmNewLocationModel> osujmNewLocationModelList = new ArrayList<>();
		List<?> documentList = new ArrayList<>();
		List<DocumentFields> newLocationDocumentList = new ArrayList<>();
		try {
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
				throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
			}
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();

			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				osujmNewLocationModelList = newLocationRepository.getCitizenNewlocationSearch(applicationNumber,
						applicationStatus, mobileNumber, uuid);
			} else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
					&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
				osujmNewLocationModelList = newLocationRepository.getCitizenNewlocationSearch(applicationNumber,
						applicationStatus, mobileNumber, uuid, fromDate, toDate);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber) && !BookingsFieldsValidator.isNullOrEmpty(osujmNewLocationModelList)) {
				documentList = commonRepository.findDocuments(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}

			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					String[] strArray = documentStrArray[1].split("/");
					documentFields.setFileName(strArray[strArray.length - 1].substring(13,(strArray[strArray.length - 1].length() - 2)));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					newLocationDocumentList.add(documentFields);
				}
			}
			booking.setDocumentList(newLocationDocumentList);
			booking.setOsujmNewLocationModelList(osujmNewLocationModelList);
			booking.setBookingsCount(osujmNewLocationModelList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the all citizen newlocation.
	 *
	 * @return the all citizen newlocation
	 */
	@Override
	public Booking getAllCitizenNewlocation() {
		Booking booking = new Booking();
		List< OsujmNewLocationModel > osujmNewLocationModelsList = new ArrayList<>();
		List< OsujmNewLocationFields > newLocationList = null;
		Map< String, List< OsujmNewLocationFields > > newLocationMap = new HashMap<>();
		int count;
		try {
			osujmNewLocationModelsList = newLocationRepository.getAllCitizenNewlocation();
			if (!BookingsFieldsValidator.isNullOrEmpty(osujmNewLocationModelsList)) {
				for (OsujmNewLocationModel osujmNewLocationModel : osujmNewLocationModelsList) {
					OsujmNewLocationFields osujmNewLocationFields = new OsujmNewLocationFields();
					if( newLocationMap.containsKey(osujmNewLocationModel.getSector())) {
						count = 1;
						newLocationList = newLocationMap.get(osujmNewLocationModel.getSector());
						for (OsujmNewLocationFields osujmNewLocationFields2 : newLocationList) {
							if(count < osujmNewLocationFields2.getId()) {
								count = osujmNewLocationFields2.getId();
							}
						}
						osujmNewLocationFields.setActive(true);
						osujmNewLocationFields.setCode(osujmNewLocationModel.getLocalityAddress());
						osujmNewLocationFields.setName(osujmNewLocationModel.getLocalityAddress());
						osujmNewLocationFields.setId(count+1);
						osujmNewLocationFields.setTenantId(osujmNewLocationModel.getTenantId());
						osujmNewLocationFields.setAreRequirement(osujmNewLocationModel.getAreaRequirement());
						newLocationList.add(osujmNewLocationFields);
					}
					else {
						newLocationList = new ArrayList<>();
						count = 1;
						osujmNewLocationFields.setActive(true);
						osujmNewLocationFields.setCode(osujmNewLocationModel.getLocalityAddress());
						osujmNewLocationFields.setName(osujmNewLocationModel.getLocalityAddress());
						osujmNewLocationFields.setId(count);
						osujmNewLocationFields.setTenantId(osujmNewLocationModel.getTenantId());
						osujmNewLocationFields.setAreRequirement(osujmNewLocationModel.getAreaRequirement());
						newLocationList.add(osujmNewLocationFields);
					}
					newLocationMap.put(osujmNewLocationModel.getSector(), newLocationList);
				}
			}
			booking.setOsujmNewlocationMap(newLocationMap);
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenNewlocationSearch " + e);
		}
		return booking;
	}

	/**
	 * Gets the new location document.
	 *
	 * @param venue the venue
	 * @param sector the sector
	 * @return the new location document
	 */
	@Override
	public List<DocumentFields> getNewLocationDocument(String venue, String sector) {
		if (BookingsFieldsValidator.isNullOrEmpty(venue)) 
		{
			throw new IllegalArgumentException("Invalid venue");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(sector)) 
		{
			throw new IllegalArgumentException("Invalid sector");
		}
		List<DocumentFields> documentsList = new ArrayList<>();
		String applicationNumber = "";
		List<?> documentList = new ArrayList<>();
		try
		{
			applicationNumber = newLocationRepository.findApplicationNumber(venue, sector);
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocuments(applicationNumber);
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					DocumentFields documentFields = new DocumentFields();
					documentFields.setFileStoreId(documentStrArray[0].substring(2,documentStrArray[0].length()-1));
					documentFields.setFileName(documentStrArray[1].substring(1,documentStrArray[1].length()-1));
					if(!"null".equals(documentStrArray[2].substring(0,documentStrArray[2].length()-1)))
					{
						documentFields.setDocumentType(documentStrArray[2].substring(1,documentStrArray[2].length()-2));
					}
					documentsList.add(documentFields);
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.error("Exception occur in the getNewLocationDocument " + e);
		}
		return documentsList;
	}

}
