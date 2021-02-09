package org.egov.bookings.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.Demand.StatusEnum;
import org.egov.bookings.models.demand.DemandDetail;
import org.egov.bookings.models.demand.DemandResponse;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.repository.OsbmFeeRepository;
import org.egov.bookings.repository.impl.DemandRepository;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.service.BookingsCalculatorService;
import org.egov.bookings.service.DemandService;
import org.egov.bookings.utils.BookingsCalculatorConstants;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.utils.CalculationUtils;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.egov.bookings.service.impl.MDMSService;

import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class DemandServiceImpl.
 */
@Service
public class DemandServiceImpl implements DemandService {

	/** The demand repository. */
	@Autowired
	DemandRepository demandRepository;

	/** The config. */
	@Autowired
	private BookingsConfiguration config;

	/** The osbm fee repository. */
	@Autowired
	private OsbmFeeRepository osbmFeeRepository;

	/** The bookings calculator service. */
	@Autowired
	BookingsCalculatorService bookingsCalculatorService;

	/** The bookings utils. */
	@Autowired
	BookingsUtils bookingsUtils;

	/** The service request repository. */
	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	/** The mapper. */
	@Autowired
	private ObjectMapper mapper;

	/** The bookings calculator. */
	@Autowired
	BookingsCalculatorService bookingsCalculator;

	@Autowired
	MDMSService mdmsService;
	
	@Autowired
	CalculationUtils calculationUtils;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.DemandService#createDemand(org.egov.bookings.web.
	 * models.BookingsRequest)
	 */
	@Override
	public void createDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new ArrayList<>();

		switch (bookingsRequest.getBookingsModel().getBusinessService()) {

		case BookingsConstants.BUSINESS_SERVICE_OSBM:

			demands = getDemandsForOsbm(bookingsRequest);
			break;

		case BookingsConstants.BUSINESS_SERVICE_BWT:

			demands = getDemandsForBwt(bookingsRequest);
			break;

		case BookingsConstants.BUSINESS_SERVICE_GFCP:
			demands = getDemandsForGfcp(bookingsRequest);
			break;
			
		case BookingsConstants.BUSINESS_SERVICE_OSUJM:
			demands = getDemandsForOsujm(bookingsRequest);
			break;
			
		case BookingsConstants.BUSINESS_SERVICE_PACC:
			demands = getDemandsForPACC(bookingsRequest);
			break;	
		}

		 demandRepository.saveDemand(bookingsRequest.getRequestInfo(), demands);

	}

	/**
	 * Gets the demands for PACC.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the demands for PACC
	 */
	private List<Demand> getDemandsForPACC(BookingsRequest bookingsRequest) {


		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getBookingsModel().getTenantId();

			String taxHeadCode1 = BookingsCalculatorConstants.PACC_TAX_CODE_1;

			String taxHeadCode2 = BookingsCalculatorConstants.PACC_TAX_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest,
					taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

			
			 Object mdmsData = mdmsService.mDMSCall(bookingsRequest.getRequestInfo(), tenantId);

	            Long taxPeriodFrom = System.currentTimeMillis();
	            Long taxPeriodTo = System.currentTimeMillis();

	            Map<String, Long> taxPeriods = mdmsService.getTaxPeriods(bookingsRequest.getRequestInfo(), bookingsRequest.getBookingsModel(), mdmsData);
	            taxPeriodFrom = taxPeriods.get(BookingsCalculatorConstants.MDMS_STARTDATE);
	            taxPeriodTo = taxPeriods.get(BookingsCalculatorConstants.MDMS_ENDDATE);
			List<String> combinedBillingSlabs = new LinkedList<>();
			addRoundOffTaxHead(tenantId, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_PACC);
			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(tenantId).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", e.getLocalizedMessage());
		}
		return demands;

	
	}

	/**
	 * Gets the demands for osujm.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the demands for osujm
	 */
	private List<Demand> getDemandsForOsujm(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getBookingsModel().getTenantId();

			String taxHeadCode1 = BookingsCalculatorConstants.OSUJM_TAX_CODE_1;

			String taxHeadCode2 = BookingsCalculatorConstants.OSUJM_TAX_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest,
					taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

/*			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;*/
			
			 Object mdmsData = mdmsService.mDMSCall(bookingsRequest.getRequestInfo(), tenantId);

	            Long taxPeriodFrom = System.currentTimeMillis();
	            Long taxPeriodTo = System.currentTimeMillis();

	            Map<String, Long> taxPeriods = mdmsService.getTaxPeriods(bookingsRequest.getRequestInfo(), bookingsRequest.getBookingsModel(), mdmsData);
	            taxPeriodFrom = taxPeriods.get(BookingsCalculatorConstants.MDMS_STARTDATE);
	            taxPeriodTo = taxPeriods.get(BookingsCalculatorConstants.MDMS_ENDDATE);
			List<String> combinedBillingSlabs = new LinkedList<>();
			addRoundOffTaxHead(tenantId, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_OSUJM);
			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(tenantId).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", e.getLocalizedMessage());
		}
		return demands;

	}

	/**
	 * Creates the and get calculation and demand for osbm.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the list
	 */
	public List<Demand> getDemandsForOsbm(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getBookingsModel().getTenantId();

			String taxHeadCode1 = BookingsCalculatorConstants.OSBM_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsCalculatorConstants.OSBM_TAXHEAD_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest,
					taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

/*			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;
*/			
			 Object mdmsData = mdmsService.mDMSCall(bookingsRequest.getRequestInfo(), tenantId);

	            Long taxPeriodFrom = System.currentTimeMillis();
	            Long taxPeriodTo = System.currentTimeMillis();

	            Map<String, Long> taxPeriods = mdmsService.getTaxPeriods(bookingsRequest.getRequestInfo(), bookingsRequest.getBookingsModel(), mdmsData);
	            taxPeriodFrom = taxPeriods.get(BookingsCalculatorConstants.MDMS_STARTDATE);
	            taxPeriodTo = taxPeriods.get(BookingsCalculatorConstants.MDMS_ENDDATE);

			
			List<String> combinedBillingSlabs = new LinkedList<>();

			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(tenantId).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getFinanceBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", e.getLocalizedMessage());
		}
		return demands;

	}

	/**
	 * Creates the and get calculation and demand for bwt.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the list
	 */
	public List<Demand> getDemandsForBwt(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getBookingsModel().getTenantId();

			String taxHeadCode1 = BookingsCalculatorConstants.BWT_TAXHEAD_CODE_1;

			String taxHeadCode2 = BookingsCalculatorConstants.BWT_TAXHEAD_CODE_2;
			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest,
					taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

/*			Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;
*/			
			 Object mdmsData = mdmsService.mDMSCall(bookingsRequest.getRequestInfo(), tenantId);

            Long taxPeriodFrom = System.currentTimeMillis();
            Long taxPeriodTo = System.currentTimeMillis();

            Map<String, Long> taxPeriods = mdmsService.getTaxPeriods(bookingsRequest.getRequestInfo(), bookingsRequest.getBookingsModel(), mdmsData);
            taxPeriodFrom = taxPeriods.get(BookingsCalculatorConstants.MDMS_STARTDATE);
            taxPeriodTo = taxPeriods.get(BookingsCalculatorConstants.MDMS_ENDDATE);
            
			List<String> combinedBillingSlabs = new LinkedList<>();

			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(tenantId).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getFinanceBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", e.getLocalizedMessage());
		}
		return demands;

	}

	private List<Demand> getDemandsForGfcp(BookingsRequest bookingsRequest) {

		List<Demand> demands = new LinkedList<>();
		List<DemandDetail> demandDetails = new LinkedList<>();
		try {
			String tenantId = bookingsRequest.getBookingsModel().getTenantId();

			String taxHeadCode1 = BookingsCalculatorConstants.GFCP_TAX_CODE_1;

			String taxHeadCode2 = BookingsCalculatorConstants.GFCP_TAX_CODE_2;

			List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest,
					taxHeadCode1, taxHeadCode2);

			taxHeadEstimate1.forEach(taxHeadEstimate -> {
				demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(BigDecimal.ZERO)
						.tenantId(tenantId).build());
			});

			/*Long taxPeriodFrom = 1554057000000L;
			Long taxPeriodTo = 1869676199000L;*/
			
			 Object mdmsData = mdmsService.mDMSCall(bookingsRequest.getRequestInfo(), tenantId);

	            Long taxPeriodFrom = System.currentTimeMillis();
	            Long taxPeriodTo = System.currentTimeMillis();

	            Map<String, Long> taxPeriods = mdmsService.getTaxPeriods(bookingsRequest.getRequestInfo(), bookingsRequest.getBookingsModel(), mdmsData);
	            taxPeriodFrom = taxPeriods.get(BookingsCalculatorConstants.MDMS_STARTDATE);
	            taxPeriodTo = taxPeriods.get(BookingsCalculatorConstants.MDMS_ENDDATE);
			
			List<String> combinedBillingSlabs = new LinkedList<>();
			addRoundOffTaxHead(tenantId, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_GFCP);
			Demand singleDemand = Demand.builder().status(StatusEnum.ACTIVE)
					.consumerCode(bookingsRequest.getBookingsModel().getBkApplicationNumber())
					.demandDetails(demandDetails).payer(bookingsRequest.getRequestInfo().getUserInfo())
					.minimumAmountPayable(config.getMinimumPayableAmount())
					.tenantId(tenantId).taxPeriodFrom(taxPeriodFrom)
					.taxPeriodTo(taxPeriodTo).consumerType("bookings")
					.businessService(bookingsRequest.getBookingsModel().getBusinessService())
					.additionalDetails(Collections.singletonMap("calculationDes1cription", combinedBillingSlabs))
					.build();

			demands.add(singleDemand);
		} catch (Exception e) {
			throw new CustomException("DEMAND_ERROR", e.getLocalizedMessage());
		}
		return demands;

	}

	private void addRoundOffTaxHead(String tenantId, List<DemandDetail> demandDetails,String mdmsRoundOff) {
		BigDecimal totalTax = BigDecimal.ZERO;

		DemandDetail prevRoundOffDemandDetail = null;

		/*
		 * Sum all taxHeads except RoundOff as new roundOff will be calculated
		 */
		for (DemandDetail demandDetail : demandDetails) {
			if (!demandDetail.getTaxHeadMasterCode().equalsIgnoreCase(mdmsRoundOff))
				totalTax = totalTax.add(demandDetail.getTaxAmount());
			else
				prevRoundOffDemandDetail = demandDetail;
		}

		BigDecimal decimalValue = totalTax.remainder(BigDecimal.ONE);
		BigDecimal midVal = new BigDecimal(0.5);
		BigDecimal roundOff = BigDecimal.ZERO;

		/*
		 * If the decimal amount is greater than 0.5 we subtract it from 1 and put it as
		 * roundOff taxHead so as to nullify the decimal eg: If the tax is 12.64 we will
		 * add extra tax roundOff taxHead of 0.36 so that the total becomes 13
		 */
		if (decimalValue.compareTo(midVal) >= 0)
			roundOff = BigDecimal.ONE.subtract(decimalValue);

		/*
		 * If the decimal amount is less than 0.5 we put negative of it as roundOff
		 * taxHead so as to nullify the decimal eg: If the tax is 12.36 we will add
		 * extra tax roundOff taxHead of -0.36 so that the total becomes 12
		 */
		if (decimalValue.compareTo(midVal) < 0)
			roundOff = decimalValue.negate();

		/*
		 * If roundOff already exists in previous demand create a new roundOff taxHead
		 * with roundOff amount equal to difference between them so that it will be
		 * balanced when bill is generated. eg: If the previous roundOff amount was of
		 * -0.36 and the new roundOff excluding the previous roundOff is 0.2 then the
		 * new roundOff will be created with 0.2 so that the net roundOff will be 0.2
		 * -(-0.36)
		 */
		if (prevRoundOffDemandDetail != null) {
			roundOff = roundOff.subtract(prevRoundOffDemandDetail.getTaxAmount());
		}

		if (roundOff.compareTo(BigDecimal.ZERO) != 0) {
			DemandDetail roundOffDemandDetail = DemandDetail.builder().taxAmount(roundOff)
					.taxHeadMasterCode(mdmsRoundOff).tenantId(tenantId)
					.collectionAmount(BigDecimal.ZERO).build();

			demandDetails.add(roundOffDemandDetail);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.DemandService#updateDemand(org.egov.bookings.web.
	 * models.BookingsRequest)
	 */
	@Override
	public void updateDemand(BookingsRequest bookingsRequest) {

		List<Demand> demands = new ArrayList<>();
		switch (bookingsRequest.getBookingsModel().getBusinessService()) {

		case BookingsConstants.BUSINESS_SERVICE_OSBM:
			demands = updateDemandsForOsbm(bookingsRequest);
			break;

		case BookingsConstants.BUSINESS_SERVICE_GFCP:
			demands = updateDemandsForGfcp(bookingsRequest);
			break;
		case BookingsConstants.BUSINESS_SERVICE_OSUJM:
			demands = updateDemandsForOsujm(bookingsRequest);
			break;	
		case BookingsConstants.BUSINESS_SERVICE_PACC:
			demands = updateDemandsForPacc(bookingsRequest);
			break;
			/*if(config.isDemandFlag()) {
			demandRepository.updateDemand(bookingsRequest.getRequestInfo(), demands);
			return;
			}
			else {
				config.setDemandFlag(true);
				return;
			}*/
		}
		 demandRepository.updateDemand(bookingsRequest.getRequestInfo(), demands);

	}

	public List<Demand> updateDemandsForPacc(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();

		String taxHeadCode1 = BookingsCalculatorConstants.PACC_TAX_CODE_1;

		String taxHeadCode2 = BookingsCalculatorConstants.PACC_TAX_CODE_2;

		List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1,
				taxHeadCode2);

		RequestInfo requestInfo = bookingsRequest.getRequestInfo();

		if (config.isDemandFlag()) {
			List<Demand> searchResult = searchDemand(bookingsRequest.getBookingsModel().getTenantId(),
					Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
					bookingsRequest.getBookingsModel().getBusinessService());
			if (CollectionUtils.isEmpty(searchResult)) {
				throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
						+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
			}
			Demand demand = searchResult.get(0);
			List<DemandDetail> demandDetails = demand.getDemandDetails();
			List<DemandDetail> updatedDemandDetails = new ArrayList<>();

			updatedDemandDetails = getUpdatedDemandDetailsForPacc(taxHeadEstimate1, demandDetails,
					BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_PACC);
			demand.setDemandDetails(updatedDemandDetails);
			demands.add(demand);

			
		}
		return demands;
	}

	private List<Demand> updateDemandsForOsujm(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();

		String taxHeadCode1 = BookingsCalculatorConstants.OSUJM_TAX_CODE_1;

		String taxHeadCode2 = BookingsCalculatorConstants.OSUJM_TAX_CODE_2;

		List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1,
				taxHeadCode2);

		RequestInfo requestInfo = bookingsRequest.getRequestInfo();

		List<Demand> searchResult = searchDemand(bookingsRequest.getBookingsModel().getTenantId(),
				Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
				bookingsRequest.getBookingsModel().getBusinessService());

		Demand demand = searchResult.get(0);
		List<DemandDetail> demandDetails = demand.getDemandDetails();
		List<DemandDetail> updatedDemandDetails = getUpdatedDemandDetails(taxHeadEstimate1, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_OSUJM);
		demand.setDemandDetails(updatedDemandDetails);
		demands.add(demand);

		/*
		 * taxHeadEstimate1.forEach(taxHeadEstimate -> {
		 * demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.
		 * getEstimateAmount())
		 * .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(
		 * BigDecimal.ZERO) .tenantId(tenantId).build()); });
		 */

		// demands.add(demands);

		if (CollectionUtils.isEmpty(searchResult)) {
			throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
					+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
		}
		return demands;
	}

	private List<Demand> updateDemandsForOsbm(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();
		String tenantId = bookingsRequest.getBookingsModel().getTenantId();

		String taxHeadCode1 = BookingsCalculatorConstants.OSBM_TAXHEAD_CODE_1;

		String taxHeadCode2 = BookingsCalculatorConstants.OSBM_TAXHEAD_CODE_2;

		List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1,
				taxHeadCode2);

		RequestInfo requestInfo = bookingsRequest.getRequestInfo();

		List<Demand> searchResult = searchDemand(tenantId,
				Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
				bookingsRequest.getBookingsModel().getFinanceBusinessService());

		Demand demand = searchResult.get(0);
		List<DemandDetail> demandDetails = demand.getDemandDetails();
		List<DemandDetail> updatedDemandDetails = getUpdatedDemandDetails(taxHeadEstimate1, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_OSBM);
		demand.setDemandDetails(updatedDemandDetails);
		demands.add(demand);

		/*
		 * taxHeadEstimate1.forEach(taxHeadEstimate -> {
		 * demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.
		 * getEstimateAmount())
		 * .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(
		 * BigDecimal.ZERO) .tenantId(tenantId).build()); });
		 */

		// demands.add(demands);

		if (CollectionUtils.isEmpty(searchResult)) {
			throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
					+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
		}
		return demands;
	}

	private List<Demand> updateDemandsForGfcp(BookingsRequest bookingsRequest) {
		List<Demand> demands = new LinkedList<>();

		String taxHeadCode1 = BookingsCalculatorConstants.GFCP_TAX_CODE_1;

		String taxHeadCode2 = BookingsCalculatorConstants.GFCP_TAX_CODE_2;

		List<TaxHeadEstimate> taxHeadEstimate1 = bookingsCalculator.getTaxHeadEstimate(bookingsRequest, taxHeadCode1,
				taxHeadCode2);

		RequestInfo requestInfo = bookingsRequest.getRequestInfo();

		List<Demand> searchResult = searchDemand(bookingsRequest.getBookingsModel().getTenantId(),
				Collections.singleton(bookingsRequest.getBookingsModel().getBkApplicationNumber()), requestInfo,
				bookingsRequest.getBookingsModel().getBusinessService());

		Demand demand = searchResult.get(0);
		List<DemandDetail> demandDetails = demand.getDemandDetails();
		List<DemandDetail> updatedDemandDetails = getUpdatedDemandDetails(taxHeadEstimate1, demandDetails,BookingsCalculatorConstants.MDMS_ROUNDOFF_TAXHEAD_GFCP);
		demand.setDemandDetails(updatedDemandDetails);
		demands.add(demand);

		/*
		 * taxHeadEstimate1.forEach(taxHeadEstimate -> {
		 * demandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.
		 * getEstimateAmount())
		 * .taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).collectionAmount(
		 * BigDecimal.ZERO) .tenantId(tenantId).build()); });
		 */

		// demands.add(demands);

		if (CollectionUtils.isEmpty(searchResult)) {
			throw new CustomException("INVALID UPDATE", "No demand exists for applicationNumber: "
					+ bookingsRequest.getBookingsModel().getBkApplicationNumber());
		}
		return demands;
	}

	/**
	 * Gets the updated demand details.
	 *
	 * @param taxHeadEstimate1 the tax head estimate 1
	 * @param demandDetails the demand details
	 * @return the updated demand details
	 */
	private List<DemandDetail> getUpdatedDemandDetails(List<TaxHeadEstimate> taxHeadEstimate1,
			List<DemandDetail> demandDetails,String mdmsRoundOff) {

		List<DemandDetail> newDemandDetails = new ArrayList<>();
		Map<String, List<DemandDetail>> taxHeadToDemandDetail = new HashMap<>();

		demandDetails.forEach(demandDetail -> {
			if (!taxHeadToDemandDetail.containsKey(demandDetail.getTaxHeadMasterCode())) {
				List<DemandDetail> demandDetailList = new LinkedList<>();
				demandDetailList.add(demandDetail);
				taxHeadToDemandDetail.put(demandDetail.getTaxHeadMasterCode(), demandDetailList);
			} else
				taxHeadToDemandDetail.get(demandDetail.getTaxHeadMasterCode()).add(demandDetail);
		});

		BigDecimal diffInTaxAmount;
		List<DemandDetail> demandDetailList;
		BigDecimal total;

		for (TaxHeadEstimate taxHeadEstimate : taxHeadEstimate1) {
			if (!taxHeadToDemandDetail.containsKey(taxHeadEstimate.getTaxHeadCode()))
				newDemandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).tenantId(demandDetails.get(0).getTenantId())
						.collectionAmount(BigDecimal.ZERO).build());
			else {
				demandDetailList = taxHeadToDemandDetail.get(taxHeadEstimate.getTaxHeadCode());
				total = demandDetailList.stream().map(DemandDetail::getTaxAmount).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				diffInTaxAmount = taxHeadEstimate.getEstimateAmount().subtract(total);
				if (diffInTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
					newDemandDetails.add(DemandDetail.builder().taxAmount(diffInTaxAmount)
							.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).tenantId(demandDetails.get(0).getTenantId())
							.collectionAmount(BigDecimal.ZERO).build());
				}
			}
		}
		List<DemandDetail> combinedBillDetials = new LinkedList<>(demandDetails);
		combinedBillDetials.addAll(newDemandDetails);
		addRoundOffTaxHead(demandDetails.get(0).getTenantId(), combinedBillDetials,mdmsRoundOff);
		return combinedBillDetials;
	}

	
	private List<DemandDetail> getUpdatedDemandDetailsForPacc(List<TaxHeadEstimate> taxHeadEstimate1,
			List<DemandDetail> demandDetails,String mdmsRoundOff) {

		List<DemandDetail> newDemandDetails = new ArrayList<>();
		Map<String, List<DemandDetail>> taxHeadToDemandDetail = new HashMap<>();

		demandDetails.forEach(demandDetail -> {
			if (!taxHeadToDemandDetail.containsKey(demandDetail.getTaxHeadMasterCode())) {
				List<DemandDetail> demandDetailList = new LinkedList<>();
				demandDetailList.add(demandDetail);
				taxHeadToDemandDetail.put(demandDetail.getTaxHeadMasterCode(), demandDetailList);
			} else
				taxHeadToDemandDetail.get(demandDetail.getTaxHeadMasterCode()).add(demandDetail);
		});

		BigDecimal diffInTaxAmount;
		List<DemandDetail> demandDetailList;
		BigDecimal total;

		for (TaxHeadEstimate taxHeadEstimate : taxHeadEstimate1) {
			if (!taxHeadToDemandDetail.containsKey(taxHeadEstimate.getTaxHeadCode()))
				newDemandDetails.add(DemandDetail.builder().taxAmount(taxHeadEstimate.getEstimateAmount())
						.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).tenantId(demandDetails.get(0).getTenantId())
						.collectionAmount(BigDecimal.ZERO).build());
			else {
				demandDetailList = taxHeadToDemandDetail.get(taxHeadEstimate.getTaxHeadCode());
				total = demandDetailList.stream().map(DemandDetail::getTaxAmount).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				diffInTaxAmount = taxHeadEstimate.getEstimateAmount().subtract(total);
				if (diffInTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
					newDemandDetails.add(DemandDetail.builder().taxAmount(diffInTaxAmount)
							.taxHeadMasterCode(taxHeadEstimate.getTaxHeadCode()).tenantId(demandDetails.get(0).getTenantId())
							.collectionAmount(BigDecimal.ZERO).build());
				}
			}
		}
		List<DemandDetail> combinedBillDetials = new LinkedList<>(demandDetails);
		combinedBillDetials.addAll(newDemandDetails);
		if(BookingsFieldsValidator.isNullOrEmpty(newDemandDetails)) {
			return combinedBillDetials;
		}
		addRoundOffTaxHeadForPaccUpdate(demandDetails.get(0).getTenantId(), combinedBillDetials,mdmsRoundOff);
		return combinedBillDetials;
	}
	
	
	private void addRoundOffTaxHeadForPaccUpdate(String tenantId, List<DemandDetail> demandDetails,
			String mdmsRoundOff) {
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal paccFinalTax = BigDecimal.ZERO;
		BigDecimal paccFinalAmount = BigDecimal.ZERO;

		BigDecimal demo = BigDecimal.ZERO;
		
		for (DemandDetail demandDetail : demandDetails) {
			demo = demo.add(demandDetail.getTaxAmount());
			
		}
		
		
		/*
		 * Sum all taxHeads except RoundOff as new roundOff will be calculated
		 */
		/*for (DemandDetail demandDetail : demandDetails) {
			if (!demandDetail.getTaxHeadMasterCode().equalsIgnoreCase(mdmsRoundOff)) {
				totalTax = totalTax.add(demandDetail.getTaxAmount());
				if (BookingsConstants.PACC_TAXHEAD_CODE_PACC_TAX.equals(demandDetail.getTaxHeadMasterCode())) {
					paccFinalTax = demandDetail.getTaxAmount();
				}
				if (BookingsConstants.PACC_TAXHEAD_CODE_PACC.equals(demandDetail.getTaxHeadMasterCode())) {
					paccFinalAmount = demandDetail.getTaxAmount();
				}
			} 
		}*/

		BigDecimal midVal = new BigDecimal(0.5);
		BigDecimal roundOff = BigDecimal.ZERO;
		BigDecimal paccFinalTaxRoundOff = paccFinalTax.remainder(BigDecimal.ONE);
		BigDecimal paccFinalAmountRoundOff = paccFinalAmount.remainder(BigDecimal.ONE);
		
		BigDecimal demoRoundOff = demo.remainder(BigDecimal.ONE);
		
		
		
		if(demoRoundOff.compareTo(BigDecimal.ZERO) != 0) {
			if(demoRoundOff.compareTo(midVal) >= 0) {
				roundOff = BigDecimal.ONE.subtract(demoRoundOff);
			}	
			else {
				roundOff = demoRoundOff.negate();
			}
			DemandDetail roundOffDemandDetail = DemandDetail.builder().taxAmount(roundOff)
					.taxHeadMasterCode(mdmsRoundOff).tenantId(tenantId)
					.collectionAmount(BigDecimal.ZERO).build();

			demandDetails.add(roundOffDemandDetail);
		}
		
		
	/*	if(paccFinalAmountRoundOff.compareTo(BigDecimal.ZERO) != 0) {
			if(paccFinalAmountRoundOff.compareTo(midVal) >= 0) {
				roundOff = BigDecimal.ONE.subtract(paccFinalTaxRoundOff);
			}	
			else {
				roundOff = paccFinalAmountRoundOff.negate();
			}
			DemandDetail roundOffDemandDetail = DemandDetail.builder().taxAmount(roundOff)
					.taxHeadMasterCode(mdmsRoundOff).tenantId(tenantId)
					.collectionAmount(BigDecimal.ZERO).build();

			demandDetails.add(roundOffDemandDetail);
		}
		
		
		if(paccFinalTaxRoundOff.compareTo(BigDecimal.ZERO) != 0) {
			if(paccFinalTaxRoundOff.compareTo(midVal) >= 0) {
				roundOff = BigDecimal.ONE.subtract(paccFinalTaxRoundOff);
			}	
			else {
				roundOff = paccFinalTaxRoundOff.negate();
			}
			DemandDetail roundOffDemandDetail = DemandDetail.builder().taxAmount(roundOff)
					.taxHeadMasterCode(mdmsRoundOff).tenantId(tenantId)
					.collectionAmount(BigDecimal.ZERO).build();

			demandDetails.add(roundOffDemandDetail);
		}*/
	}

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
	
	
	public BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria){

        String consumerCode = billCriteria.getConsumerCode();
        String tenantId = billCriteria.getTenantId();

        List<Demand> demands = searchDemand(tenantId,Collections.singleton(consumerCode),requestInfo,billCriteria.getBusinessService());

        if(CollectionUtils.isEmpty(demands))
            throw new CustomException("INVALID CONSUMERCODE","Bill cannot be generated.No demand exists for the given consumerCode");

        String uri = calculationUtils.getBillGenerateURI();
        uri = uri.replace("{1}",billCriteria.getTenantId());
        uri = uri.replace("{2}",billCriteria.getConsumerCode());
        uri = uri.replace("{3}",billCriteria.getBusinessService());

        Object result = serviceRequestRepository.fetchResult(new StringBuilder(uri),RequestInfoWrapper.builder()
                                                             .requestInfo(requestInfo).build());
        BillResponse response;
         try{
              response = mapper.convertValue(result,BillResponse.class);
         }
         catch (IllegalArgumentException e){
            throw new CustomException("PARSING ERROR","Unable to parse response of generate bill");
         }
         return response;
    }
	

}
