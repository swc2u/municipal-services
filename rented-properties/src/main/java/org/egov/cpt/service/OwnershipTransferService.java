package org.egov.cpt.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.cpt.config.PropertyConfiguration;
import org.egov.cpt.models.BillV2;
import org.egov.cpt.models.DuplicateCopySearchCriteria;
import org.egov.cpt.models.Owner;
import org.egov.cpt.models.Property;
import org.egov.cpt.models.PropertyCriteria;
import org.egov.cpt.models.RentAccount;
import org.egov.cpt.models.RentDemand;
import org.egov.cpt.models.RentSummary;
import org.egov.cpt.models.calculation.BusinessService;
import org.egov.cpt.models.calculation.State;
import org.egov.cpt.producer.Producer;
import org.egov.cpt.repository.OwnershipTransferRepository;
import org.egov.cpt.repository.PropertyRepository;
import org.egov.cpt.service.calculation.DemandRepository;
import org.egov.cpt.service.calculation.DemandService;
import org.egov.cpt.service.notification.PropertyNotificationService;
import org.egov.cpt.util.PTConstants;
import org.egov.cpt.util.PropertyUtil;
import org.egov.cpt.validator.PropertyValidator;
import org.egov.cpt.web.contracts.OwnershipTransferRequest;
import org.egov.cpt.workflow.WorkflowIntegrator;
import org.egov.cpt.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OwnershipTransferService {

	@Autowired
	private PropertyValidator propertyValidator;

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private PropertyConfiguration config;

	@Autowired
	private Producer producer;

	@Autowired
	private WorkflowIntegrator wfIntegrator;

	@Autowired
	private OwnershipTransferRepository repository;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private DemandService demandService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	PropertyNotificationService notificationService;

	@Autowired
	private IRentCollectionService rentCollectionService;

	@Autowired
	private UserService userService;

	@Autowired
	private PropertyUtil propertyUtil;

	@Autowired
	private DemandRepository demandRepository;

	public List<Owner> createOwnershipTransfer(OwnershipTransferRequest request) {
		// propertyValidator.validateCreateRequest(request);
		propertyValidator.validatePropertyRentDetails(request);
		List<Property> propertyFromSearch = propertyValidator.getPropertyForOT(request);
		enrichmentService.enrichCreateOwnershipTransfer(request, propertyFromSearch);
		if (config.getIsWorkflowEnabled()) {
			wfIntegrator.callOwnershipTransferWorkFlow(request);
		}
		producer.push(config.getOwnershipTransferSaveTopic(), request);

		/**
		 * calling rent Summary
		 */

		addRentSummary(request.getOwners());

		return request.getOwners();
	}

	public List<Owner> searchOwnershipTransfer(DuplicateCopySearchCriteria criteria, RequestInfo requestInfo) {
		if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_CITIZEN)) {
			criteria.setCreatedBy(requestInfo.getUserInfo().getUuid());
		}
		if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_EMPLOYEE)
				&& CollectionUtils.isEmpty(criteria.getStatus())) {
			String wfbusinessServiceName = PTConstants.BUSINESS_SERVICE_OT;
			BusinessService otBusinessService = workflowService.getBusinessService(criteria.getTenantId(), requestInfo,
					wfbusinessServiceName);
			List<State> stateList = otBusinessService.getStates();
			List<String> states = new ArrayList<String>();

			for (State state : stateList) {
				states.add(state.getState());
			}
			states.remove("");
			states.remove(PTConstants.OT_DRAFTED);
			log.info("states:" + states);
			criteria.setStatus(states);
		}
		List<Owner> owners = repository.searchOwnershipTransfer(criteria);

		if (CollectionUtils.isEmpty(owners)) {
			if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_CITIZEN)
					&& criteria.getApplicationNumber() != null)
				throw new CustomException("INVALID ACCESS", "You can not access this application.");
			else
				return Collections.emptyList();
		}

		/**
		 * calling rent Summary
		 */
		addRentSummary(owners);

		return owners;
	}

	public List<Owner> updateOwnershipTransfer(OwnershipTransferRequest request) {
		propertyValidator.validatePropertyRentDetails(request);
		List<Owner> ownersFromSearch = propertyValidator.validateUpdateRequest(request);
		enrichmentService.enrichUpdateOwnershipTransfer(request, ownersFromSearch);
		String applicationState = request.getOwners().get(0).getApplicationState(); // demand generation
		/*
		 * if (applicationState.equalsIgnoreCase(PTConstants.
		 * OT_STATE_PENDING_SA_VERIFICATION)) {
		 * demandService.updateDemand(request.getRequestInfo(), request.getOwners()); }
		 */
		if (applicationState.equalsIgnoreCase(PTConstants.OT_STATE_PENDING_APRO)) {
			demandService.generateDemand(request.getRequestInfo(), request.getOwners());
		}
		if (config.getIsWorkflowEnabled()) {
			wfIntegrator.callOwnershipTransferWorkFlow(request);
		}
		producer.push(config.getOwnershipTransferUpdateTopic(), request);

		if (request.getOwners().get(0).getApplicationState().equalsIgnoreCase(PTConstants.OT_STATUS_APPROVED)) {
			enrichmentService.postStatusEnrichment(request);
		}
//		notificationService.process(request);

		/**
		 * calling rent Summary
		 */
		addRentSummary(request.getOwners());

		return request.getOwners();
	}

	private void addRentSummary(List<Owner> owners) {
		owners.stream().filter(owner -> owner.getProperty().getId() != null).forEach(owner -> {

			PropertyCriteria propertyCriteria = PropertyCriteria.builder().relations(Arrays.asList("owner"))
					.propertyId(owner.getProperty().getId()).build();

			List<Property> propertiesFromDB = propertyRepository.getProperties(propertyCriteria);
			List<RentDemand> demands = propertyRepository.getPropertyRentDemandDetails(propertyCriteria);

			RentAccount rentAccount = propertyRepository.getPropertyRentAccountDetails(propertyCriteria);
			if (!CollectionUtils.isEmpty(demands) && null != rentAccount
					&& !CollectionUtils.isEmpty(propertiesFromDB)) {
				long interestStartDate = propertyUtil.getInterstStartFromMDMS(propertiesFromDB.get(0).getColony(),
						propertiesFromDB.get(0).getTenantId());
				owner.getProperty().setRentSummary(rentCollectionService.calculateRentSummary(demands, rentAccount,
						propertiesFromDB.get(0).getPropertyDetails().getInterestRate(), interestStartDate));
			} else
				owner.getProperty().setRentSummary(new RentSummary());
		});

	}

	public List<Owner> collectPayment(OwnershipTransferRequest otRequest) {
		/**
		 * Validate not empty
		 */
		if (CollectionUtils.isEmpty(otRequest.getOwners())) {
			return Collections.emptyList();
		}
		Owner ownerFromRequest = otRequest.getOwners().get(0);
		
		/**
		 * Validate that this is a valid application number.
		 */
		if (ownerFromRequest.getOwnerDetails().getApplicationNumber() == null) {
			throw new CustomException(Collections.singletonMap("NO_APPLICATION_NUMBER_FOUND",
					"No application number found to process payment"));
		}
		/**
		 * Validate payment amount
		 */
		if(ownerFromRequest.getOwnerDetails().getPaymentAmount()==null) {
			throw new CustomException(Collections.singletonMap("INVALID_PAYMENT_AMOUNT",
					"Payment amount should valid"));
		}
		
		DuplicateCopySearchCriteria otCriteria = DuplicateCopySearchCriteria.builder()
				.applicationNumber(ownerFromRequest.getOwnerDetails().getApplicationNumber()).status(Collections.singletonList(PTConstants.OT_PENDINGPAYMENT))
				.build();

		/**
		 * Retrieve owner from db with the given ids.
		 */
		List<Owner> ownersFromDB = repository.searchOwnershipTransfer(otCriteria);
		if (CollectionUtils.isEmpty(ownersFromDB)) {
			throw new CustomException(
					Collections.singletonMap("APPLICATION_NOT_FOUND", String.format("Could not find any valid application %s with pending payment state",
							ownerFromRequest.getOwnerDetails().getApplicationNumber())));
		}

		Owner ownerFromDB = ownersFromDB.get(0);
		BigDecimal totalDue;
		if(ownerFromDB.getOwnerDetails().getAproCharge()!=null) {
			totalDue=ownerFromDB.getOwnerDetails().getDueAmount().add(ownerFromDB.getOwnerDetails().getAproCharge());
		}else {
			totalDue=ownerFromDB.getOwnerDetails().getDueAmount();
		}
		
		/**
		 * Validate payment amount
		 */
		if(ownerFromRequest.getOwnerDetails().getPaymentAmount() < totalDue.doubleValue()) {
			throw new CustomException(Collections.singletonMap("INVALID_PAYMENT_AMOUNT",
					"Payment amount should be equal to due amount"));
		}

		ownerFromDB.getOwnerDetails().setTransactionId(ownerFromRequest.getOwnerDetails().getTransactionId());
		ownerFromDB.getOwnerDetails().setBankName(ownerFromRequest.getOwnerDetails().getBankName());
		ownerFromDB.getOwnerDetails().setPaymentAmount(ownerFromRequest.getOwnerDetails().getPaymentAmount());

		/**
		 * Create egov user if not already present.
		 */
		userService.createUser(otRequest.getRequestInfo(), ownerFromDB.getOwnerDetails().getPhone(),
				ownerFromDB.getOwnerDetails().getName(), ownerFromDB.getTenantId());
		
		enrichmentService.enrichOwnerDemandCalculation(ownerFromDB);

		/**
		 * Generate an actual finance demand
		 */
		demandService.generateFinanceOTDemand(otRequest.getRequestInfo(), ownerFromDB);

		/**
		 * Get the bill generated.
		 */
		List<BillV2> bills = demandRepository.fetchBill(otRequest.getRequestInfo(), ownerFromDB.getTenantId(),
				ownerFromDB.getOwnerDetails().getApplicationNumber(), ownerFromDB.getBillingBusinessService());
		if (CollectionUtils.isEmpty(bills)) {
			throw new CustomException("BILL_NOT_GENERATED", "No bills were found for the consumer code "
					+ ownerFromDB.getOwnerDetails().getApplicationNumber());
		}

		if (otRequest.getRequestInfo().getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_EMPLOYEE)) {
			log.info("OFFLINE PAYMENT");
			/**
			 * if offline, create a payment.
			 */
			log.info("Payment Amount:"+ownerFromDB.getOwnerDetails().getPaymentAmount());
			
			demandService.createCashPayment(otRequest.getRequestInfo(), ownerFromDB.getOwnerDetails().getPaymentAmount(),
					bills.get(0).getId(), ownerFromDB, ownerFromDB.getBillingBusinessService());

			otRequest.setOwners(Collections.singletonList(ownerFromDB));
			producer.push(config.getOwnershipTransferUpdateTopic(), otRequest);

		}
		/*
		 * else {
		 *//**
			 * We return the application along with the consumerCode that we set earlier.
			 * Also save it so the consumer code gets persisted.
			 *//*
				 * otRequest.setOwners(Collections.singletonList(ownerFromDB));
				 * producer.push(config.getOwnershipTransferUpdateTopic(), otRequest); }
				 */
		return Collections.singletonList(ownerFromDB);

	}

}
