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
import org.egov.bookings.contract.BookingApprover;
import org.egov.bookings.contract.BookingConfigJsonFields;
import org.egov.bookings.contract.BookingsRequestKafka;
import org.egov.bookings.contract.MdmsJsonFields;
import org.egov.bookings.contract.Message;
import org.egov.bookings.contract.MessagesResponse;
import org.egov.bookings.contract.ProcessInstanceSearchCriteria;
import org.egov.bookings.contract.RefundTransactionRequest;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.Transaction;
import org.egov.bookings.contract.Transaction.TxnStatusEnum;
import org.egov.bookings.contract.UserDetails;
import org.egov.bookings.dto.SearchCriteriaFieldsDTO;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.user.OwnerInfo;
import org.egov.bookings.model.user.UserDetailResponse;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.utils.CreateDateComparator;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.workflow.WorkflowIntegrator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

/**
 * The Class BookingsServiceImpl.
 */
@Service
@Transactional
public class BookingsServiceImpl implements BookingsService {

	/** The bookings repository. */
	@Autowired
	private BookingsRepository bookingsRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The workflow integrator. */
	@Autowired
	private WorkflowIntegrator workflowIntegrator;

	/** The osbm approver repository. */
	@Autowired
	OsbmApproverRepository osbmApproverRepository;

	/** The common repository. */
	@Autowired
	CommonRepository commonRepository;

	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	/** The bookings producer. */
	@Autowired
	private BookingsProducer bookingsProducer;
	
	/** The user service. */
	@Autowired
	private UserService userService;

	/** The mail notification service. */
	/*
	 * @Autowired private MailNotificationService mailNotificationService;
	 */

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(BookingsServiceImpl.class.getName());

	/**
	 * Save.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	@Override
	public BookingsModel save(BookingsRequest bookingsRequest) {
		boolean flag = isBookingExists(bookingsRequest.getBookingsModel().getBkApplicationNumber());

		if (!flag)
			enrichmentService.enrichBookingsCreateRequest(bookingsRequest);
		if (!BookingsConstants.ACTION_DELIVER.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& !BookingsConstants.ACTION_FAILURE_APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())) {
			enrichmentService.generateDemand(bookingsRequest);
		}

		if (config.getIsExternalWorkFlowEnabled()) {
			if (!flag)
				workflowIntegrator.callWorkFlow(bookingsRequest);
		}
		enrichmentService.enrichBookingsDetails(bookingsRequest);
		try {
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getSaveBookingTopic(), kafkaBookingRequest);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		// bookingsModel = bookingsRepository.save(bookingsRequest.getBookingsModel());
		// bookingsRequest.setBookingsModel(bookingsModel);
		String bookingType = bookingsRequest.getBookingsModel().getBkBookingType();
		if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel())) {
			Map<String, MdmsJsonFields> mdmsJsonFieldsMap = mdmsJsonField(bookingsRequest);
			if (!BookingsFieldsValidator.isNullOrEmpty(mdmsJsonFieldsMap)) {
				if (!BookingsConstants.BUSINESS_SERVICE_PACC
						.equals(bookingsRequest.getBookingsModel().getBusinessService())) {
					bookingsRequest.getBookingsModel().setBkBookingType(
							mdmsJsonFieldsMap.get(bookingsRequest.getBookingsModel().getBkBookingType()).getName());
				}
				bookingsProducer.push(config.getSaveBookingSMSTopic(), bookingsRequest);
			}
		}
		bookingsRequest.getBookingsModel().setBkBookingType(bookingType);
		return bookingsRequest.getBookingsModel();

	}

	/**
	 * Prepare application status.
	 *
	 * @param requestInfo   the request info
	 * @param bookingsModel the bookings model
	 * @return the string
	 */
	public String prepareApplicationStatus(RequestInfo requestInfo, BookingsModel bookingsModel) {
		MessagesResponse messageResponse = getLocalizationMessage(requestInfo);
		String applicationStatus = "";
		String status = "";
		if (!BookingsFieldsValidator.isNullOrEmpty(messageResponse)) {
			if (BookingsConstants.BUSINESS_SERVICE_OSBM.equals(bookingsModel.getBusinessService())) {
				applicationStatus = BookingsConstants.BK_WF_OSBM + bookingsModel.getBkApplicationStatus();
			} else if (BookingsConstants.BUSINESS_SERVICE_BWT.equals(bookingsModel.getBusinessService())) {
				applicationStatus = BookingsConstants.BK_WF_BWT + bookingsModel.getBkApplicationStatus();
			} else if (BookingsConstants.BUSINESS_SERVICE_GFCP.equals(bookingsModel.getBusinessService())) {
				if (BookingsConstants.INITIATED.equals(bookingsModel.getBkApplicationStatus())) {
					applicationStatus = BookingsConstants.BK + bookingsModel.getBkApplicationStatus();
				} else {
					applicationStatus = BookingsConstants.BK_CGB + bookingsModel.getBkApplicationStatus();
				}
			} else if (BookingsConstants.BUSINESS_SERVICE_OSUJM.equals(bookingsModel.getBusinessService())) {
				applicationStatus = BookingsConstants.BK_WF_OSUJM + bookingsModel.getBkApplicationStatus();
			} else if (BookingsConstants.BUSINESS_SERVICE_PACC.equals(bookingsModel.getBusinessService())) {
				applicationStatus = BookingsConstants.BK_WF_PACC + bookingsModel.getBkApplicationStatus();
			}
			for (Message message : messageResponse.getMessages()) {
				if (message.getCode().equals(applicationStatus)) {
					status = message.getMessage();
				}
			}
		}
		return status;
	}

	/**
	 * Gets the localization message.
	 *
	 * @param requestInfo the request info
	 * @return the localization message
	 */
	public MessagesResponse getLocalizationMessage(RequestInfo requestInfo) {
		Object result = new Object();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		StringBuilder url = new StringBuilder(config.getLocalizationHost());
		url.append(config.getLocalizationContextPath());
		url.append(config.getLocalizationSearchEndpoint());
		url.append("?module=rainmaker-services");
		url.append("&locale=en_IN");
		url.append("&tenantId=ch");
		MessagesResponse messageResponse = null;
		try {
			result = serviceRequestRepository.fetchResult(url, requestInfoWrapper);
			if (!BookingsFieldsValidator.isNullOrEmpty(result)) {
				messageResponse = objectMapper.convertValue(result, MessagesResponse.class);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur during get localization message " + e);
			throw new CustomException("Exception occur during get localization message", e.getLocalizedMessage());
		}
		return messageResponse;
	}

	/**
	 * Mdms json field.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	private Map<String, MdmsJsonFields> mdmsJsonField(BookingsRequest bookingsRequest) {
		JSONArray mdmsArrayList = null;
		Map<String, MdmsJsonFields> mdmsJsonFieldsMap = new HashMap<>();
		try {
			if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)
					&& !BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getRequestInfo())) {
				Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
				String jsonString = objectMapper.writeValueAsString(mdmsData);
				MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
				Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
				Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
				mdmsArrayList = mdmsRes.get("BookingType");
				if (!BookingsFieldsValidator.isNullOrEmpty(mdmsArrayList)) {
					for (int i = 0; i < mdmsArrayList.size(); i++) {
						jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
						MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
						mdmsJsonFieldsMap.put(mdmsJsonFields.getCode(), mdmsJsonFields);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur during mdmsJsonField " + e);
		}
		return mdmsJsonFieldsMap;
	}

	/**
	 * Checks if is booking exists.
	 *
	 * @param bkApplicationnumber the bk applicationnumber
	 * @return true, if is booking exists
	 */
	public boolean isBookingExists(String bkApplicationnumber) {

		BookingsModel bookingsModel = bookingsRepository.findByBkApplicationNumber(bkApplicationnumber);

		if (null == bookingsModel) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Gets the citizen search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the citizen search booking
	 */
	@Override
	public Booking getCitizenSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		Booking booking = new Booking();
		List<BookingsModel> myBookingList = new ArrayList<>();
		List<?> documentList = new ArrayList<>();
		Map<String, String> documentMap = new HashMap<>();
		try {
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			String bookingType = searchCriteriaFieldsDTO.getBookingType();
			String parksBookingType = "";
			String communityCenterBookingType = "";
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(bookingType) && BookingsConstants.PARKS_AND_COMMUNITY_CENTER.equals(bookingType)) {
				String[] bookingArray = bookingType.split(BookingsConstants.AND);
				parksBookingType = bookingArray[0].trim();
				communityCenterBookingType = bookingArray[1].trim();
				if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
					myBookingList = bookingsRepository.getCitizenSearchPACCBooking(applicationNumber, applicationStatus,
							mobileNumber, parksBookingType, communityCenterBookingType, uuid);
				} 
				else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
					myBookingList = bookingsRepository.getCitizenSearchPACCBooking(applicationNumber, applicationStatus,
							mobileNumber, parksBookingType, communityCenterBookingType, uuid, fromDate, toDate);
				}
			}
			else {
				if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
					myBookingList = bookingsRepository.getCitizenSearchBooking(applicationNumber, applicationStatus,
							mobileNumber, bookingType, uuid);
				} 
				else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
					myBookingList = bookingsRepository.getCitizenSearchBooking(applicationNumber, applicationStatus,
							mobileNumber, bookingType, uuid, fromDate, toDate);
				}
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)) {
				documentList = commonRepository.findDocumentList(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}

			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/");
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length() - 1);
					String document = strArray[strArray.length - 1].substring(13,
							(strArray[strArray.length - 1].length() - 2));
					documentMap.put(fileStoreId, document);
				}
			}
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList(myBookingList);
			booking.setBookingsCount(myBookingList.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getCitizenSearchBooking " + e);
		}
		return booking;
	}

	/**
	 * Gets the employee search booking.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the employee search booking
	 */
	@Override
	public Booking getEmployeeSearchBooking(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		Booking booking = new Booking();
		List<BookingsModel> bookingsList = new ArrayList<>();
		Set<BookingsModel> bookingsSet = new HashSet<>();
		List<?> documentList = new ArrayList<>();
		Map<String, String> documentMap = new HashMap<>();
		Set<String> applicationNumberSet = new HashSet<>();
		Set<String> applicationNumbers = new HashSet<>();
		try {
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String applicationStatus = searchCriteriaFieldsDTO.getApplicationStatus();
			String mobileNumber = searchCriteriaFieldsDTO.getMobileNumber();
			Date fromDate = searchCriteriaFieldsDTO.getFromDate();
			Date toDate = searchCriteriaFieldsDTO.getToDate();
			String uuid = searchCriteriaFieldsDTO.getUuid();
			String bookingType = searchCriteriaFieldsDTO.getBookingType();
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo())) {
				throw new IllegalArgumentException("Invalid RequestInfo");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo().getUserInfo())) {
				throw new IllegalArgumentException("Invalid UserInfo");
			}
			if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
				throw new IllegalArgumentException("Invalid uuId");
			}
			List<Role> roles = searchCriteriaFieldsDTO.getRequestInfo().getUserInfo().getRoles();
			if (BookingsFieldsValidator.isNullOrEmpty(roles)) {
				throw new IllegalArgumentException("Invalid roles");
			}
			for (Role role : roles) {
				if (!BookingsConstants.CITIZEN.equals(role.getCode()) && !BookingsConstants.EMPLOYEE.equals(role.getCode())) {
					if (BookingsConstants.OSBM_APPROVER.equals(role.getCode()) || BookingsConstants.MCC_APPROVER.equals(role.getCode())) {
						applicationNumberSet.addAll(commonRepository.findApplicationNumber(role.getCode()));
						applicationNumbers.addAll(applicationNumberSet);
						List<String> sectorList = commonRepository.findSectorList(uuid);
						if (!BookingsFieldsValidator.isNullOrEmpty(sectorList) && !BookingsFieldsValidator.isNullOrEmpty(applicationNumberSet)) {
							if (BookingsFieldsValidator.isNullOrEmpty(fromDate)&& BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchBooking(applicationNumber,applicationStatus, mobileNumber, bookingType, sectorList, applicationNumberSet));
							} 
							else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchBooking(applicationNumber,
										applicationStatus, mobileNumber, bookingType, sectorList, fromDate, toDate,
										applicationNumberSet));
							}
						}
						applicationNumberSet.removeAll(applicationNumbers);
					} 
					else if (BookingsConstants.MCC_HELPDESK_USER.equals(role.getCode())) {
						String action = mdmsSearch(searchCriteriaFieldsDTO.getRequestInfo(),
								BookingsConstants.BOOKING_MDMS_MODULE_NAME, BookingsConstants.BOOKING_MDMS_FILE_NAME);
						String approver = BookingsConstants.MCC_HELPDESK_USER;
						if (!BookingsFieldsValidator.isNullOrEmpty(action)) {
							List<String> applicationList = commonRepository.findApplicationList(action, approver);
							applicationNumberSet.addAll(applicationList);
						}
						applicationNumberSet.addAll(commonRepository.findApplicationNumber(role.getCode()));
						applicationNumbers.addAll(applicationNumberSet);
						if(!BookingsFieldsValidator.isNullOrEmpty(applicationNumberSet)) {
							if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchBWTBooking(applicationNumber,
										applicationStatus, mobileNumber, bookingType, applicationNumberSet));
							} 
							else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchBWTBooking(applicationNumber, applicationStatus,
												mobileNumber, bookingType, applicationNumberSet, fromDate, toDate));
							}
						}
						applicationNumberSet.removeAll(applicationNumbers);
					} 
					else if (BookingsConstants.COMMERCIAL_GROUND_VIEWER.equals(role.getCode())) {
						if (BookingsFieldsValidator.isNullOrEmpty(bookingType)) {
							bookingType = BookingsConstants.GROUND_FOR_COMMERCIAL_PURPOSE;
						}
						if (BookingsFieldsValidator.isNullOrEmpty(fromDate)
								&& BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							bookingsSet.addAll(bookingsRepository.getEmployeeSearchGFCPBooking(applicationNumber,
									applicationStatus, mobileNumber, bookingType));
						} 
						else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
								&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							bookingsSet.addAll(bookingsRepository.getEmployeeSearchGFCPBooking(applicationNumber,
									applicationStatus, mobileNumber, bookingType, fromDate, toDate));
						}
					} 
					else if (BookingsConstants.PARKS_AND_COMMUNITY_VIEWER.equals(role.getCode())) {
						String parksBookingType = BookingsConstants.PARKS;
						String communityCenterBookingType = BookingsConstants.COMMUNITY_CENTER;
						if (!BookingsFieldsValidator.isNullOrEmpty(bookingType)) {
							String[] bookingArray = bookingType.split(BookingsConstants.AND);
							parksBookingType = bookingArray[0].trim();
							communityCenterBookingType = bookingArray[1].trim();
//							parksBookingType = bookingType;
//							communityCenterBookingType = bookingType;
						}
						if (BookingsFieldsValidator.isNullOrEmpty(fromDate)
								&& BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							bookingsSet.addAll(bookingsRepository.getEmployeeSearchPACCBooking(applicationNumber,
									applicationStatus, mobileNumber, parksBookingType, communityCenterBookingType));
						} 
						else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate)
								&& !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
							bookingsSet.addAll(bookingsRepository.getEmployeeSearchPACCBooking(applicationNumber,
									applicationStatus, mobileNumber, parksBookingType, communityCenterBookingType,
									fromDate, toDate));
						}
					} 
					else if (BookingsConstants.DEO.equals(role.getCode()) || BookingsConstants.CLERK.equals(role.getCode())
									|| BookingsConstants.SENIOR_ASSISTANT.equals(role.getCode())
									|| BookingsConstants.AUDIT_DEPARTMENT.equals(role.getCode())
									|| BookingsConstants.CHIEF_ACCOUNT_OFFICER.equals(role.getCode())
									|| BookingsConstants.PAYMENT_PROCESSING_AUTHORITY.equals(role.getCode())
									|| BookingsConstants.E_SAMPARK_CENTER.equals(role.getCode())
									|| BookingsConstants.MCC_USER.equals(role.getCode())
									|| BookingsConstants.SUPERVISOR.equals(role.getCode())
									|| BookingsConstants.OSD.equals(role.getCode())
							) {
						String parksBookingType = BookingsConstants.PARKS;
						String communityCenterBookingType = BookingsConstants.COMMUNITY_CENTER;
						if (!BookingsFieldsValidator.isNullOrEmpty(bookingType)) {
							String[] bookingArray = bookingType.split(BookingsConstants.AND);
							parksBookingType = bookingArray[0].trim();
							communityCenterBookingType = bookingArray[1].trim();
						}
						if(BookingsConstants.DEO.equals(role.getCode()) || BookingsConstants.CLERK.equals(role.getCode())
								|| BookingsConstants.E_SAMPARK_CENTER.equals(role.getCode())
								|| BookingsConstants.MCC_USER.equals(role.getCode())) {
							applicationNumberSet.addAll(commonRepository.findBusinessId(role.getCode()));
						}
						applicationNumbers.addAll(applicationNumberSet);
						if(!BookingsFieldsValidator.isNullOrEmpty(applicationNumberSet)) {
							if (BookingsFieldsValidator.isNullOrEmpty(fromDate) && BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchPACCBooking(applicationNumber,
										applicationStatus, mobileNumber, parksBookingType, communityCenterBookingType,
										applicationNumberSet));
							} 
							else if (!BookingsFieldsValidator.isNullOrEmpty(fromDate) && !BookingsFieldsValidator.isNullOrEmpty(fromDate)) {
								bookingsSet.addAll(bookingsRepository.getEmployeeSearchPACCBooking(applicationNumber,
										applicationStatus, mobileNumber, parksBookingType, communityCenterBookingType,
										applicationNumberSet, fromDate, toDate));
							}
						}
						applicationNumberSet.removeAll(applicationNumbers);
					}
				}
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(applicationNumber)
					&& !BookingsFieldsValidator.isNullOrEmpty(bookingsSet)) {
				documentList = commonRepository.findDocumentList(applicationNumber);
				booking.setBusinessService(commonRepository.findBusinessService(applicationNumber));
			}
			if (!BookingsFieldsValidator.isNullOrEmpty(documentList)) {
				for (Object documentObject : documentList) {
					String jsonString = objectMapper.writeValueAsString(documentObject);
					String[] documentStrArray = jsonString.split(",");
					String[] strArray = documentStrArray[1].split("/");
					String fileStoreId = documentStrArray[0].substring(2, documentStrArray[0].length() - 1);
					String document = strArray[strArray.length - 1].substring(13,
							(strArray[strArray.length - 1].length() - 2));
					documentMap.put(fileStoreId, document);
				}
			}
			bookingsList.addAll(bookingsSet);
			Collections.sort(bookingsList, new CreateDateComparator());
			Collections.reverse(bookingsList);
			booking.setDocumentMap(documentMap);
			booking.setBookingsModelList(bookingsList);
			booking.setBookingsCount(bookingsSet.size());
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getEmployeeSearchBooking " + e);
		}
		return booking;

	}

	/**
	 * Mdms search.
	 *
	 * @param requestInfo    the request info
	 * @param mdmsModuleName the mdms module name
	 * @param mdmsFileName   the mdms file name
	 * @return the string
	 */
	public String mdmsSearch(RequestInfo requestInfo, String mdmsModuleName, String mdmsFileName) {
		JSONArray mdmsArrayList = null;
		String action = "";
		try {
			Object mdmsData = bookingsUtils.getMdmsSearchRequest(requestInfo, mdmsModuleName, mdmsFileName);
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get(mdmsModuleName);
			mdmsArrayList = mdmsRes.get(mdmsFileName);
			if(!BookingsFieldsValidator.isNullOrEmpty(mdmsArrayList)) {
				for (int i = 0; i < mdmsArrayList.size(); i++) {
					jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
					BookingConfigJsonFields bookingConfigJsonFields = objectMapper.readValue(jsonString, BookingConfigJsonFields.class);
						if (BookingsConstants.BK_WATER_TANKER_DELIVER_ACTION_KEY.equals(bookingConfigJsonFields.getKey())) {
							action = bookingConfigJsonFields.getValue();
							break;
						}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the mdmsSearch " + e);
		}
		return action;
	}

	/**
	 * Update.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the bookings model
	 */
	@Override
	public BookingsModel update(BookingsRequest bookingsRequest) {
		String businessService = bookingsRequest.getBookingsModel().getBusinessService();
		if (BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& !BookingsConstants.BUSINESS_SERVICE_GFCP.equals(businessService))
			enrichmentService.enrichBookingsAssignee(bookingsRequest);
		
		if (BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction()) && BookingsConstants.BUSINESS_SERVICE_OSBM.equals(businessService)) {
			BookingsModel booking = bookingsRepository.findByBkApplicationNumber(bookingsRequest.getBookingsModel().getBkApplicationNumber());
			enrichmentService.enrichAssignee(bookingsRequest, booking);
		}
		
		if (config.getIsExternalWorkFlowEnabled())
			workflowIntegrator.callWorkFlow(bookingsRequest);

		BookingsModel bookingsModel = null;
		if (!BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_OSBM.equals(businessService)) {

			bookingsModel = enrichmentService.enrichOsbmDetails(bookingsRequest);
			bookingsRequest.setBookingsModel(bookingsModel);
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			// bookingsModel = bookingsRepository.save(bookingsModel);

		}

		else if (!BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_BWT.equals(businessService)) {

			bookingsModel = enrichmentService.enrichBwtDetails(bookingsRequest);
			bookingsRequest.setBookingsModel(bookingsModel);
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			// bookingsModel = bookingsRepository.save(bookingsModel);

		} else if (!BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
				&& BookingsConstants.BUSINESS_SERVICE_OSUJM.equals(businessService)) {
			bookingsModel = enrichmentService.enrichOsujmDetails(bookingsRequest);
			bookingsRequest.setBookingsModel(bookingsModel);
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			// bookingsModel = bookingsRepository.save(bookingsModel);
			if (BookingsConstants.PAY.equals(bookingsRequest.getBookingsModel().getBkAction())) {
				config.setJurisdictionLock(true);
			}
		} else {
			if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel().getBkPaymentStatus())) {
				bookingsRequest.getBookingsModel()
						.setBkPaymentStatus(bookingsRequest.getBookingsModel().getBkPaymentStatus());
			}
			BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
			bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
			// bookingsModel = bookingsRepository.save(bookingsRequest.getBookingsModel());
			if (BookingsConstants.APPLY.equals(bookingsRequest.getBookingsModel().getBkAction())
					&& BookingsConstants.BUSINESS_SERVICE_GFCP.equals(businessService)) {
				config.setCommercialLock(true);
			}
		}
		String bookingType = bookingsRequest.getBookingsModel().getBkBookingType();
		if (!BookingsFieldsValidator.isNullOrEmpty(bookingsRequest.getBookingsModel())) {
			Map<String, MdmsJsonFields> mdmsJsonFieldsMap = mdmsJsonField(bookingsRequest);
			if (!BookingsFieldsValidator.isNullOrEmpty(mdmsJsonFieldsMap)) {
				bookingsRequest.getBookingsModel()
						.setBkBookingType(mdmsJsonFieldsMap.get(bookingsRequest.getBookingsModel().getBkBookingType()).getName());
				bookingsProducer.push(config.getUpdateBookingSMSTopic(), bookingsRequest);
			}
		}
		bookingsRequest.getBookingsModel().setBkBookingType(bookingType);
		return bookingsRequest.getBookingsModel();
	}

	/**
	 * Employee records count.
	 *
	 * @param tenantId        the tenant id
	 * @param uuid            the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map<String, Integer> employeeRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) {
			throw new IllegalArgumentException("Invalid bookingsRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
			throw new IllegalArgumentException("Invalid uuId");
		}
		Map<String, Integer> bookingCountMap = new HashMap<>();
		try {
			List<String> sectorList = commonRepository.findSectorList(uuid);
			int allRecordsCount = bookingsRepository.countByTenantIdAndBkSectorIn(tenantId, sectorList);
			bookingCountMap.put("allRecordsCount", allRecordsCount);
			int bookingCount = 0;
			JSONArray mdmsArrayList = null;
			Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
			mdmsArrayList = mdmsRes.get("BookingType");
			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
				MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndBkSectorIn(tenantId,
						mdmsJsonFields.getCode(), sectorList);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the employeeRecordsCount " + e);
		}
		return bookingCountMap;
	}

	/**
	 * Citizen records count.
	 *
	 * @param tenantId        the tenant id
	 * @param uuid            the uuid
	 * @param bookingsRequest the bookings request
	 * @return the map
	 */
	@Override
	public Map<String, Integer> citizenRecordsCount(String tenantId, String uuid, BookingsRequest bookingsRequest) {
		if (BookingsFieldsValidator.isNullOrEmpty(bookingsRequest)) {
			throw new IllegalArgumentException("Invalid bookingsRequest");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(tenantId)) {
			throw new IllegalArgumentException("Invalid tentantId");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(uuid)) {
			throw new IllegalArgumentException("Invalid uuId");
		}
		Map<String, Integer> bookingCountMap = new HashMap<>();
		try {
			int allRecordsCount = bookingsRepository.countByTenantIdAndUuid(tenantId, uuid);
			bookingCountMap.put("allRecordsCount", allRecordsCount);
			int bookingCount = 0;
			JSONArray mdmsArrayList = null;
			Object mdmsData = bookingsUtils.prepareMdMsRequestForBooking(bookingsRequest.getRequestInfo());
			String jsonString = objectMapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = objectMapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("Booking");
			mdmsArrayList = mdmsRes.get("BookingType");
			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = objectMapper.writeValueAsString(mdmsArrayList.get(i));
				MdmsJsonFields mdmsJsonFields = objectMapper.readValue(jsonString, MdmsJsonFields.class);
				bookingCount = bookingsRepository.countByTenantIdAndBkBookingTypeAndUuid(tenantId,
						mdmsJsonFields.getCode(), uuid);
				bookingCountMap.put(mdmsJsonFields.getCode(), bookingCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the citizenRecordsCount " + e);
		}
		return bookingCountMap;
	}

	/**
	 * Gets the workflow process instances.
	 *
	 * @param requestInfoWrapper the request info wrapper
	 * @param criteria           the criteria
	 * @return the workflow process instances
	 */
	@Override
	public Object getWorkflowProcessInstances(RequestInfoWrapper requestInfoWrapper,
			ProcessInstanceSearchCriteria criteria) {
		if (BookingsFieldsValidator.isNullOrEmpty(requestInfoWrapper)) {
			throw new IllegalArgumentException("Invalid requestInfoWrapper");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(criteria)) {
			throw new IllegalArgumentException("Invalid criteria");
		}
		Object result = new Object();
		try {
			StringBuilder url = new StringBuilder(config.getWfHost());
			url.append(config.getWorkflowProcessInstancePath());
			url.append("?businessIds=");
			url.append(criteria.getBusinessIds().get(0));
			url.append("&history=");
			url.append(criteria.getHistory());
			url.append("&tenantId=");
			url.append(criteria.getTenantId());
			result = serviceRequestRepository.fetchResult(url, requestInfoWrapper);
		} catch (Exception e) {
			LOGGER.error("Exception occur in the getWorkflowProcessInstances " + e);
		}
		return result;
	}

	/**
	 * Fetch all approver.
	 *
	 * @return the list
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.BookingsService#fetchAllApprover()
	 */
	@Override
	public List<BookingApprover> fetchAllApprover() {
		List<BookingApprover> bookingApprover1 = new ArrayList<>();
		List<?> userList = new ArrayList<>();
		try {

			String type = "EMPLOYEE";
			userList = commonRepository.fetchAllApprover(type);
			if (null == userList || CollectionUtils.isEmpty(userList)) {
				throw new CustomException("APPROVER_ERROR", "NO APPROVER EXISTS IN EG_USER TABLE");
			} else {
				if (!BookingsFieldsValidator.isNullOrEmpty(userList)) {
					for (Object userObject : userList) {
						BookingApprover bookingApprover = new BookingApprover();
						String jsonString = objectMapper.writeValueAsString(userObject);
						String approverDetails = jsonString.substring(1, (jsonString.length() - 1));
						String[] documentStrArray = approverDetails.split(",");
						for (int i = 0; i < documentStrArray.length; i++) {
							switch (i) {
							case 0:
								bookingApprover.setUserName(
										documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 1:
								bookingApprover.setMobileNumber(
										documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 2:
								bookingApprover
										.setName(documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 3:
								bookingApprover
										.setUuid(documentStrArray[i].substring(1, documentStrArray[i].length() - 1));
								break;
							case 4:
								bookingApprover.setId(Long.parseLong(documentStrArray[i]));
								break;
							default:
								break;
							}

						}
						bookingApprover1.add(bookingApprover);

					}
				}
			}
		}

		catch (Exception e) {
			throw new CustomException("APPROVER_ERROR", e.getLocalizedMessage());
		}
		return bookingApprover1;
	}

	/**
	 * Gets the assignee.
	 *
	 * @param searchCriteriaFieldsDTO the search criteria fields DTO
	 * @return the assignee
	 */
	@Override
	public List<UserDetails> getAssignee(SearchCriteriaFieldsDTO searchCriteriaFieldsDTO) {
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO)) {
			throw new IllegalArgumentException("Invalid searchCriteriaFieldsDTO");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(searchCriteriaFieldsDTO.getRequestInfo())) {
			throw new IllegalArgumentException("Invalid requestInfo");
		}
		List<UserDetails> userDetailsList = new ArrayList<>();
		List<String> roleCodes = new ArrayList<>();
		UserDetailResponse userDetailResponse = new UserDetailResponse();
		try {
			String applicationNumber = searchCriteriaFieldsDTO.getApplicationNumber();
			String action = searchCriteriaFieldsDTO.getAction();
			String sector = searchCriteriaFieldsDTO.getSector();
			String businessService = searchCriteriaFieldsDTO.getBusinessService();
			String rolesName = "";
			String roles = "";
			StringBuilder url = prepareUrlForUserList();
			if (BookingsFieldsValidator.isNullOrEmpty(businessService)) {
				List<String> nextState = commonRepository.findNextState(applicationNumber, action);
				if (!BookingsFieldsValidator.isNullOrEmpty(nextState)) {
					for (String state : nextState) {
						if (BookingsFieldsValidator.isNullOrEmpty(rolesName)) {
							rolesName = commonRepository.findApproverName(state);
						}
						else {
							roles = commonRepository.findApproverName(state);
						}
						if (!BookingsFieldsValidator.isNullOrEmpty(roles)) {
							rolesName = rolesName + "," + roles;
							roles = "";
						}
					}
				}
				String[] approverArray = rolesName.split(",");
				if (!BookingsFieldsValidator.isNullOrEmpty(approverArray)) {
					for (String approver : approverArray) {
						if (!BookingsConstants.CITIZEN.equals(approver)) {
							roleCodes.add(approver);
						}
					}
					if (!BookingsFieldsValidator.isNullOrEmpty(roleCodes)) {
						userDetailResponse = userService.getUserSearchDetails(roleCodes, url, searchCriteriaFieldsDTO.getRequestInfo());
					}
					if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse)) {
						userDetailsList = prepareUserList(userDetailResponse, sector);
					}
				}
			} else if (!BookingsFieldsValidator.isNullOrEmpty(businessService)
					&& BookingsConstants.BUSINESS_SERVICE_GFCP.equals(businessService)) {
				roleCodes.add(BookingsConstants.COMMERCIAL_GROUND_VIEWER);
				userDetailResponse = userService.getUserSearchDetails(roleCodes, url, searchCriteriaFieldsDTO.getRequestInfo());
				if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse)) {
					userDetailsList = prepareUserList(userDetailResponse, sector);
				}
			} else if (!BookingsFieldsValidator.isNullOrEmpty(businessService)
					&& BookingsConstants.BUSINESS_SERVICE_PACC.equals(businessService)) {
				roleCodes.add(BookingsConstants.PARKS_AND_COMMUNITY_VIEWER);
				userDetailResponse = userService.getUserSearchDetails(roleCodes, url, searchCriteriaFieldsDTO.getRequestInfo());
				if (!BookingsFieldsValidator.isNullOrEmpty(userDetailResponse)) {
					userDetailsList = prepareUserList(userDetailResponse, sector);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception occur in the getAssignee " + e);
			e.printStackTrace();
		}
		return userDetailsList;
	}

	/**
	 * Prepare user list.
	 *
	 * @param userList the user list
	 * @param sector   the sector
	 * @return the list
	 */
	private List<UserDetails> prepareUserList(UserDetailResponse userDetailResponse, String sector) {
		List<UserDetails> userDetailsList = new ArrayList<>();
		Map<String, UserDetails> userDetailsMap = new HashMap<>();
		OsbmApproverModel osbmApproverModel = null;
		List<Role> rolesList = new ArrayList<>();
		if (BookingsFieldsValidator.isNullOrEmpty(userDetailResponse)) {
			throw new IllegalArgumentException("Invalid userDetailResponse");
		}
		if (BookingsFieldsValidator.isNullOrEmpty(userDetailResponse.getUser())) {
			throw new IllegalArgumentException("Invalid User List");
		}
		List<OwnerInfo> userList = userDetailResponse.getUser();
		try {
			for (OwnerInfo user : userList) {
				UserDetails userDetails = new UserDetails();
				userDetails.setUuid(user.getUuid());
				userDetails.setUserName(user.getUserName());
				rolesList = user.getRoles();
				for(Role role : rolesList) {
					osbmApproverModel = osbmApproverRepository.findBySectorAndUuidAndRoleCodeAndUserId(sector, userDetails.getUuid(), role.getCode(), user.getId());
				}
				if (!BookingsFieldsValidator.isNullOrEmpty(sector)
						&& !BookingsFieldsValidator.isNullOrEmpty(osbmApproverModel)) {
					if (!userDetailsMap.containsKey(userDetails.getUuid())) {
						userDetailsMap.put(userDetails.getUuid(), userDetails);
						userDetailsList.add(userDetails);
					}
				} else if (BookingsFieldsValidator.isNullOrEmpty(sector)) {
					if (!userDetailsMap.containsKey(userDetails.getUuid())) {
						userDetailsMap.put(userDetails.getUuid(), userDetails);
						userDetailsList.add(userDetails);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in the prepareUserList " + e);
			e.printStackTrace();
		}
		return userDetailsList;
	}
	
	/**
	 * Prepare url for user list.
	 *
	 * @return the string builder
	 */
	public StringBuilder prepareUrlForUserList() {
		StringBuilder url = new StringBuilder(config.getUserHost());
		url.append(config.getUserSearchEndpoint());
		return url;
	}

	@Override
	public void persistRefundStatus(RefundTransactionRequest refundTransactionRequest) {
		Transaction transaction = bookingsUtils.fetchPaymentTransaction(refundTransactionRequest);
		if(BookingsFieldsValidator.isNullOrEmpty(transaction)) {
			throw new CustomException("INVALID_REQUEST","No payments exists with respect to this transaction in database : "+refundTransactionRequest);
		}
		BookingsModel bookingsModel = bookingsRepository.findByBkApplicationNumber(transaction.getConsumerCode());
		if(BookingsFieldsValidator.isNullOrEmpty(bookingsModel)) {
			throw new CustomException("INVALID_CONSUMER_CODE","No booking exists with respect to this application number in database : "+transaction.getConsumerCode());
		}
		if(Transaction.TxnStatusEnum.SUCCESS.toString().equals(refundTransactionRequest.getRefundTransaction().getTxnStatus())){
			bookingsModel.setBkPaymentStatus(refundTransactionRequest.getRefundTransaction().getGatewayRefundStatusMsg());
		}
		bookingsModel.setBkStatusUpdateRequest(refundTransactionRequest.getRefundTransaction().getGatewayRefundStatusMsg());
		bookingsModel.setBkStatus(refundTransactionRequest.getRefundTransaction().getTxnStatus());
		BookingsRequest bookingsRequest = new BookingsRequest();
		bookingsRequest.setBookingsModel(bookingsModel);
		bookingsRequest.setRequestInfo(refundTransactionRequest.getRequestInfo());
		BookingsRequestKafka kafkaBookingRequest = enrichmentService.enrichForKafka(bookingsRequest);
		bookingsProducer.push(config.getUpdateBookingTopic(), kafkaBookingRequest);
	}
	
}
