package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandRequest;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.models.demand.TaxHeadMaster;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.IdGenRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.service.OsujmService;
import org.egov.bookings.service.ParkAndCommunityService;
import org.egov.bookings.service.RoomsService;
import org.egov.bookings.utils.BookingsCalculatorConstants;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.demands.model.enums.Category;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsCalculatorServiceImpl.
 */
@Service
@Transactional
public class BookingsCalculatorServiceImpl implements BookingsCalculatorService {

	/** The demand repository. */
	@Autowired
	private DemandRepository demandRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	/** The bookings calculator service. */
	@Autowired
	private BookingsCalculatorService bookingsCalculatorService;

	/** The bookings utils. */
	@Autowired
	private BookingsUtils bookingsUtils;

	/** The service request repository. */
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/** The commercial ground service. */
	@Autowired
	private CommercialGroundService commercialGroundService;

	/** The osujm service. */
	@Autowired
	private OsujmService osujmService;
	
	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;
	
	/** The park and community service. */
	@Autowired
	private ParkAndCommunityService parkAndCommunityService;
	
	/** The demand service. */
	@Autowired
	private DemandService demandService;
	
	@Autowired
	private RoomsService roomsService;
	
	
	/**
	 * Search demand.
	 *
	 * @param tenantId        the tenant id
	 * @param consumerCodes   the consumer codes
	 * @param requestInfo     the request info
	 * @param businessService the business service
	 * @return the list
	 */
	public List<Demand> searchDemand(String tenantId, Set<String> consumerCodes, RequestInfo requestInfo,
			String businessService) {
		String uri = bookingsUtils.getDemandSearchURL();
		uri = uri.replace("{1}", tenantId);
		uri = uri.replace("{2}", businessService);
		uri = uri.replace("{3}", StringUtils.join(consumerCodes, ','));

		Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),
				RequestInfoWrapper.builder().requestInfo(requestInfo).build());

		DemandResponse response;
		try {
			response = mapper.convertValue(result, DemandResponse.class);
		} catch (IllegalArgumentException e) {
			throw new CustomException("PARSING ERROR", "Failed to parse response from Demand Search");
		}

		if (CollectionUtils.isEmpty(response.getDemands()))
			return null;

		else
			return response.getDemands();

	}

	/**
	 * Gets the tax head estimate.
	 *
	 * @param bookingsRequest the bookings request
	 * @param taxHeadCode1    the tax head code 1
	 * @param taxHeadCode2    the tax head code 2
	 * @return the tax head estimate
	 */
	public List<TaxHeadEstimate> getTaxHeadEstimate(BookingsRequest bookingsRequest, String taxHeadCode1,
			String taxHeadCode2, String taxHeadCode3, String taxHeadCode4, String taxHeadCode5) {
		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();
		String bussinessService = bookingsRequest.getBookingsModel().getBusinessService();
		List<TaxHeadMasterFields> taxHeadMasterFieldList = getTaxHeadMasterData(bookingsRequest, bussinessService);

		switch (bussinessService) {

		case BookingsConstants.BUSINESS_SERVICE_OSBM:
			BigDecimal osbmAmount = getOsbmAmount(bookingsRequest);
			
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), osbmAmount, taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					BigDecimal osbmTax = BookingsUtils.roundOffToNearest(osbmAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))));
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							osbmTax,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode3)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO, taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode4)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO, taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode5)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO, taxHeadEstimate.getCategory()));
				}

			}
			break;
		case BookingsConstants.BUSINESS_SERVICE_BWT:
			BigDecimal bwtAmount = taxHeadMasterFieldList.get(0).getTaxAmount();
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(
							new TaxHeadEstimate(taxHeadEstimate.getCode(), bwtAmount, taxHeadEstimate.getCategory()));
				}
				/*if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							bwtAmount.multiply((taxHeadEstimate.getTaxAmount().divide(new BigDecimal(100)))),
							taxHeadEstimate.getCategory()));
				}*/

			}
			break;
		case BookingsConstants.BUSINESS_SERVICE_GFCP:
			CommercialGroundFeeModel commercialGroundFeeModel = getCommercialAmount(bookingsRequest);
			BigDecimal commercialDays = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			BigDecimal commercialAmount = commercialDays.multiply(BigDecimal.valueOf(commercialGroundFeeModel.getRatePerDay()));
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), commercialAmount,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					BigDecimal commercialTax = BookingsUtils.roundOffToNearest(commercialAmount.multiply((BookingsCalculatorConstants.UGST_AND_CGST_TAX.divide(new BigDecimal(100)))));
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							commercialTax,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode3)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.valueOf(Long.valueOf(commercialGroundFeeModel.getRefundabelSecurity())),
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode4)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode5)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO,
							taxHeadEstimate.getCategory()));
				}

			}
			break;
		case BookingsConstants.BUSINESS_SERVICE_OSUJM:
			BigDecimal mccJurisdictionAmount = getJurisdicationAmount(bookingsRequest);
			//BigDecimal mccJurisdictionAmount = new BigDecimal(4400);
			for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
				if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), mccJurisdictionAmount,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
					BigDecimal openSpaceJurisdictionTax = BookingsUtils.roundOffToNearest(mccJurisdictionAmount.multiply((BookingsCalculatorConstants.UGST_AND_CGST_TAX.divide(new BigDecimal(100)))));
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							openSpaceJurisdictionTax,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode3)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode4)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO,
							taxHeadEstimate.getCategory()));
				}
				if (taxHeadEstimate.getCode().equals(taxHeadCode5)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(), BigDecimal.ZERO,
							taxHeadEstimate.getCategory()));
				}
			}
			break;
			
		case BookingsConstants.BUSINESS_SERVICE_PACC:
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal finalAmount = BigDecimal.ZERO;
			BigDecimal rent = BigDecimal.ZERO;
			BigDecimal rentBeforeDiscount = BigDecimal.ZERO;
			BigDecimal cleaningCharges = BigDecimal.ZERO;
			ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster = getParkAndCommunityAmount(bookingsRequest);
			BigDecimal days = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			if(BookingsConstants.EMPLOYEE.equals(bookingsRequest.getRequestInfo().getUserInfo().getType())) {
				rentBeforeDiscount = BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getRent()));
			    cleaningCharges = BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getCleaningCharges()));
				amount = days.multiply(rentBeforeDiscount);
				BigDecimal rentAfterDiscount = amount.multiply(bookingsRequest.getBookingsModel().getDiscount().divide(new BigDecimal(100))); 
				rent = amount.subtract(rentAfterDiscount);
				//finalAmount = cleaningCharges.add(rentAfterDiscount1);
			}
			else {
				 rent = BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getRent()));
				 cleaningCharges = BigDecimal.valueOf(Long.valueOf(parkCommunityHallV1FeeMaster.getCleaningCharges()));
				 rent = days.multiply(rent);
				// finalAmount = cleaningCharges.add(amount);
			}
			taxHeadEstimate1 = enrichmentService.enrichPaccAmountForBookingChange(bookingsRequest, rent, cleaningCharges,
					taxHeadCode1, taxHeadCode2, taxHeadCode3, taxHeadCode4, taxHeadCode5, taxHeadMasterFieldList, parkCommunityHallV1FeeMaster);	
			break;
			
		}
		return taxHeadEstimate1;
	}

	/**
	 * Gets the park and community amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the park and community amount
	 */
	private ParkCommunityHallV1MasterModel getParkAndCommunityAmount(BookingsRequest bookingsRequest) {

		ParkCommunityHallV1MasterModel parkCommunityHallV1FeeMaster = null;
		try {
			 parkCommunityHallV1FeeMaster = parkAndCommunityService
					.findParkAndCommunityFee(bookingsRequest.getBookingsModel().getBkBookingVenue());
			if(BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1FeeMaster)) {
				throw new IllegalArgumentException("There is not any amount for this park and community criteria in database");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return parkCommunityHallV1FeeMaster;
	}

	/* (non-Javadoc)
	 * @see org.egov.bookings.service.BookingsCalculatorService#getJurisdicationAmount(org.egov.bookings.web.models.BookingsRequest)
	 */
	public BigDecimal getJurisdicationAmount(BookingsRequest bookingsRequest) {

		OsujmFeeModel osujmFeeModel = null;
		Long area = null;
		BigDecimal finalAmount = null;
		try {
			area = Long.valueOf(bookingsRequest.getBookingsModel().getBkAreaRequired());
			/*OsujmFeeModel commercialGroundFeeSearchCriteria = CommercialGroundFeeSearchCriteria
					.builder().bookingVenue(bookingVenue).category(category).build();*/
			osujmFeeModel = osujmService
					.findJurisdictionFee(bookingsRequest);
			if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel)) {
				throw new IllegalArgumentException("There is not any amount for this mcc jurisdiction criteria in database");
			}
			BigDecimal days = enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
			finalAmount = days.multiply(new BigDecimal(osujmFeeModel.getRatePerSqrFeetPerDay()*area));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return finalAmount;
	}

	/**
	 * Gets the tax head master data.
	 *
	 * @param bookingsRequest  the bookings request
	 * @param bussinessService the bussiness service
	 * @return the tax head master data
	 */
	public List<TaxHeadMasterFields> getTaxHeadMasterData(BookingsRequest bookingsRequest, String bussinessService) {

		List<TaxHeadMasterFields> taxHeadMasterFieldList = new ArrayList<>();
		JSONArray mdmsArrayList = null;
		try {
			Object mdmsData = bookingsUtils.prepareMdMsRequestForTaxHeadMaster(bookingsRequest);
			String jsonString = mapper.writeValueAsString(mdmsData);
			MdmsResponse mdmsResponse = mapper.readValue(jsonString, MdmsResponse.class);
			Map<String, Map<String, JSONArray>> mdmsResMap = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mdmsRes = mdmsResMap.get("BillingService");
			mdmsArrayList = mdmsRes.get("TaxHeadMaster");

			for (int i = 0; i < mdmsArrayList.size(); i++) {
				jsonString = mapper.writeValueAsString(mdmsArrayList.get(i));
				TaxHeadMasterFields taxHeadFields = mapper.readValue(jsonString, TaxHeadMasterFields.class);
				if (taxHeadFields.getService().equals(bookingsRequest.getBookingsModel().getFinanceBusinessService())) {
					taxHeadMasterFieldList.add(taxHeadFields);
				}
			}
		} catch (Exception e) {
			throw new CustomException("MDMS_MASTER_ERROR", "Error while fetching mdms TaxHeadMaster data");
		}
		return taxHeadMasterFieldList;
	}

	/**
	 * Gets the osbm amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the osbm amount
	 */
	public BigDecimal getOsbmAmount(BookingsRequest bookingsRequest) {

		List<OsbmFeeModel> osbmFeeModel = null;
		BigDecimal amount = null;
		try {
			
			LocalDate currentDate = LocalDate.now();
			String constructionType = bookingsRequest.getBookingsModel().getBkConstructionType();
			String durationInMonths = bookingsRequest.getBookingsModel().getBkDuration();
			String storage = bookingsRequest.getBookingsModel().getBkAreaRequired();
			String villageCity = bookingsRequest.getBookingsModel().getBkVillCity();
			String residentialCommercial = bookingsRequest.getBookingsModel().getBkType();
			//String toDateInString = "";
			osbmFeeModel = osbmFeeRepository
					.findByVillageCityAndResidentialCommercialAndStorageAndDurationInMonthsAndConstructionType(
							villageCity, residentialCommercial, storage, durationInMonths, constructionType);
			
			if(BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel)) {
				throw new CustomException("DATA_NOT_FOUND","There is not any amount for this osbm criteria in database");
			}
			
			for(OsbmFeeModel osbmFeeModel1 : osbmFeeModel) {
				if(BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel1.getFromDate())) {
					throw new CustomException("DATA_NOT_FOUND","There is no from date for this osbm criteria in database");
				}
				String pattern = "yyyy-MM-dd";
				DateFormat df = new SimpleDateFormat(pattern);
				String fromDateInString = df.format(osbmFeeModel1.getFromDate());
				LocalDate fromDate = LocalDate.parse(fromDateInString);
				//LocalDate toDate = LocalDate.parse(toDateInString);
				if(BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel1.getToDate()) && currentDate.isAfter(fromDate) || currentDate.isEqual(fromDate)) {
					//toDateInString = df.format(osbmFeeModel1.getToDate());
					amount = new BigDecimal(osbmFeeModel1.getAmount());
				}
				if (!BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel1.getToDate())
						&& (fromDate.isEqual(currentDate) || fromDate.isBefore(currentDate))
						&& (currentDate.isBefore(LocalDate.parse(df.format(osbmFeeModel1.getToDate()))))) {
					amount = new BigDecimal(osbmFeeModel1.getAmount());
					break;
				}
			}
			
		if(BookingsFieldsValidator.isNullOrEmpty(amount)) {
			throw new CustomException("DATA_NOT_FOUND","There is not any amount for this osbm criteria in database");
		}
			
		} catch (Exception e) {
			throw new IllegalArgumentException("Exception while fetching osbm amount from database : "+e.getLocalizedMessage());
		}
		return amount;
	}

	/**
	 * Gets the commercial amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the commercial amount
	 */
	public CommercialGroundFeeModel getCommercialAmount(BookingsRequest bookingsRequest) {
		CommercialGroundFeeModel commercialGroundFeeModel = null;
		//BigDecimal finalAmount = null;
		try {
			String category = bookingsRequest.getBookingsModel().getBkCategory();
			String bookingVenue = bookingsRequest.getBookingsModel().getBkBookingVenue();
			CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria = CommercialGroundFeeSearchCriteria
					.builder().bookingVenue(bookingVenue).category(category).build();
			commercialGroundFeeModel = commercialGroundService
					.searchCommercialGroundFee(commercialGroundFeeSearchCriteria);
			if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel)) {
				throw new CustomException("DATA_NOT_FOUND","There is not any amount for this commercial ground criteria in database");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return commercialGroundFeeModel;
	}
	
	
	
	
	
	public List<TaxHeadEstimate> getTaxHeadEstimateForRoom(BookingsRequest bookingsRequest, String taxHeadCode1,
			String taxHeadCode2) {
		List<TaxHeadEstimate> taxHeadEstimate1 = new ArrayList<>();
		String bussinessService = bookingsRequest.getBookingsModel().getRoomsModel().get(0).getRoomBusinessService();
		List<TaxHeadMasterFields> taxHeadMasterFieldList = getTaxHeadMasterData(bookingsRequest, bussinessService);
		BigDecimal amount = roomsService.getRoomAmount(bookingsRequest);
		BigDecimal finalAmount = BigDecimal.ZERO;
		if (BookingsConstants.EMPLOYEE.equals(bookingsRequest.getRequestInfo().getUserInfo().getType())) {
			finalAmount = amount.multiply((BookingsCalculatorConstants.ONE_HUNDRED
					.subtract(bookingsRequest.getBookingsModel().getRoomsModel().get(0).getDiscount()))
							.divide(BookingsCalculatorConstants.ONE_HUNDRED));
		} else {
			finalAmount = amount;
		}

		// BigDecimal days =
		// enrichmentService.extractDaysBetweenTwoDates(bookingsRequest);
		// BigDecimal finalAmount = roomAmount.multiply(days);
		// BigDecimal mccJurisdictionAmount = new BigDecimal(4400);
		for (TaxHeadMasterFields taxHeadEstimate : taxHeadMasterFieldList) {
			if (taxHeadEstimate.getCode().equals(taxHeadCode1)) {
				taxHeadEstimate1.add(
						new TaxHeadEstimate(taxHeadEstimate.getCode(), finalAmount, taxHeadEstimate.getCategory()));
			}
			if (taxHeadEstimate.getCode().equals(taxHeadCode2)) {
				BigDecimal roomTax = BookingsUtils.roundOffToNearest(finalAmount.multiply((BookingsCalculatorConstants.UGST_AND_CGST_TAX.divide(new BigDecimal(100)))));
				taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
						roomTax,
						taxHeadEstimate.getCategory()));
			}
			/*if(bookingsRequest.getRequestInfo().getUserInfo().getType().equals(BookingsConstants.EMPLOYEE)) {
				if (taxHeadEstimate.getCode().equals(BookingsConstants.ROOM_TAXHEAD_CODE_FACILITATION_CHARGE)) {
					taxHeadEstimate1.add(new TaxHeadEstimate(taxHeadEstimate.getCode(),
							taxHeadEstimate.getFacilitationCharge(),
							taxHeadEstimate.getCategory()));
					}
			}*/
		}
		return taxHeadEstimate1;
	}

}
