package org.egov.cpt.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.cpt.config.PropertyConfiguration;
import org.egov.cpt.models.Applicant;
import org.egov.cpt.models.BillV2;
import org.egov.cpt.models.DuplicateCopy;
import org.egov.cpt.models.DuplicateCopySearchCriteria;
import org.egov.cpt.models.Owner;
import org.egov.cpt.models.OwnerDetails;
import org.egov.cpt.models.Property;
import org.egov.cpt.models.PropertyCriteria;
import org.egov.cpt.models.RentAccount;
import org.egov.cpt.models.RentDemand;
import org.egov.cpt.models.RentSummary;
import org.egov.cpt.models.calculation.BusinessService;
import org.egov.cpt.models.calculation.State;
import org.egov.cpt.producer.Producer;
import org.egov.cpt.repository.PropertyRepository;
import org.egov.cpt.service.calculation.DemandRepository;
import org.egov.cpt.service.calculation.DemandService;
import org.egov.cpt.service.notification.DuplicateCopyNotificationService;
import org.egov.cpt.util.PTConstants;
import org.egov.cpt.util.PropertyUtil;
import org.egov.cpt.validator.PropertyValidator;
import org.egov.cpt.web.contracts.DuplicateCopyRequest;
import org.egov.cpt.workflow.WorkflowIntegrator;
import org.egov.cpt.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DuplicateCopyService {
	@Autowired
	private PropertyValidator propertyValidator;

	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private PropertyConfiguration config;

	@Autowired
	private Producer producer;

	@Autowired
	private PropertyRepository repository;

	@Autowired
	private WorkflowIntegrator wfIntegrator;

	@Autowired
	private DemandService demandService;

	@Autowired
	private DuplicateCopyNotificationService notificationService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private IRentCollectionService rentCollectionService;

	@Autowired
	private PropertyUtil propertyUtil;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private UserService userService;

	public List<DuplicateCopy> createApplication(DuplicateCopyRequest duplicateCopyRequest) {
		propertyValidator.isPropertyExist(duplicateCopyRequest);
		propertyValidator.validateDuplicateCopyCreateRequest(duplicateCopyRequest);
		enrichmentService.enrichDuplicateCopyCreateRequest(duplicateCopyRequest);
		propertyValidator.validateDuplicateCreate(duplicateCopyRequest);
		if (config.getIsWorkflowEnabled()) {
			wfIntegrator.callDuplicateCopyWorkFlow(duplicateCopyRequest);
		}
		producer.push(config.getSaveDuplicateCopyTopic(), duplicateCopyRequest);
		/**
		 * calling Rent summary
		 */

		addRentSummary(duplicateCopyRequest.getDuplicateCopyApplications());


		return duplicateCopyRequest.getDuplicateCopyApplications();
	}

	public List<DuplicateCopy> searchApplication(DuplicateCopySearchCriteria criteria, RequestInfo requestInfo) {
		propertyValidator.validateDuplicateCopySearch(requestInfo, criteria);
		enrichmentService.enrichDuplicateCopySearchCriteria(requestInfo, criteria);
		if (requestInfo.getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_EMPLOYEE)
				&& CollectionUtils.isEmpty(criteria.getStatus())) {
			BusinessService otBusinessService = workflowService.getBusinessService(criteria.getTenantId(), requestInfo,
					PTConstants.BUSINESS_SERVICE_DC);
			List<State> stateList = otBusinessService.getStates();
			List<String> states = new ArrayList<String>();

			for (State state : stateList) {
				states.add(state.getState());
			}
			states.remove("");
			states.remove(PTConstants.DC_DRAFTED);

			log.info("states:" + states);

			criteria.setStatus(states);
		}

		List<DuplicateCopy> properties = getApplication(criteria, requestInfo);
		return properties;
	}

	private List<DuplicateCopy> getApplication(DuplicateCopySearchCriteria criteria, RequestInfo requestInfo) {
		List<DuplicateCopy> applications = repository.getDuplicateCopyProperties(criteria);
		if (applications.isEmpty()){
			if(requestInfo.getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_CITIZEN)&& criteria.getApplicationNumber()!=null)
				throw new CustomException("INVALID ACCESS", "You can not access this application.");
			else
				return Collections.emptyList();
		}

		addRentSummary(applications);

		return applications;
	}

	public List<DuplicateCopy> updateApplication(DuplicateCopyRequest duplicateCopyRequest) {

		List<DuplicateCopy> searchedProperty = propertyValidator
				.validateDuplicateCopyUpdateRequest(duplicateCopyRequest);
		enrichmentService.enrichDuplicateCopyUpdateRequest(duplicateCopyRequest, searchedProperty);
		String applicationState = duplicateCopyRequest.getDuplicateCopyApplications().get(0).getState();

		if (applicationState.equalsIgnoreCase(PTConstants.DC_STATE_PENDING_SA_VERIFICATION)) {
			demandService.generateDuplicateCopyDemand(duplicateCopyRequest.getRequestInfo(),
					duplicateCopyRequest.getDuplicateCopyApplications());
		}
		propertyValidator.validateDuplicateUpdate(duplicateCopyRequest);
		if (config.getIsWorkflowEnabled()) {
			wfIntegrator.callDuplicateCopyWorkFlow(duplicateCopyRequest);
		}
		producer.push(config.getUpdateDuplicateCopyTopic(), duplicateCopyRequest);
		notificationService.process(duplicateCopyRequest);

		/**
		 * calling Rent summary
		 */
		addRentSummary(duplicateCopyRequest.getDuplicateCopyApplications());

		return duplicateCopyRequest.getDuplicateCopyApplications();
	}

	private void addRentSummary(List<DuplicateCopy> duplicateCopyApplications) {
		duplicateCopyApplications.stream().filter(application -> application.getProperty().getId() != null)
		.forEach(application -> {

			PropertyCriteria propertyCriteria = PropertyCriteria.builder().relations(Arrays.asList("owner"))
					.propertyId(application.getProperty().getId()).build();

			List<Property> propertiesFromDB = repository.getProperties(propertyCriteria);
			List<RentDemand> demands = repository
					.getPropertyRentDemandDetails(propertyCriteria);

			RentAccount rentAccount = repository
					.getPropertyRentAccountDetails(propertyCriteria);
			if (!CollectionUtils.isEmpty(demands) && null != rentAccount && !CollectionUtils.isEmpty(propertiesFromDB)) {
				long interestStartDate = propertyUtil.getInterstStartFromMDMS(propertiesFromDB.get(0).getColony(),propertiesFromDB.get(0).getTenantId());
				application.getProperty().setRentSummary(rentCollectionService.calculateRentSummary(demands, rentAccount,
						propertiesFromDB.get(0).getPropertyDetails().getInterestRate(),interestStartDate));
			}
			else 
				application.getProperty().setRentSummary(new RentSummary());
		});
	}

	public List<DuplicateCopy> collectPayment(DuplicateCopyRequest dcRequest) {
		/**
		 * Validate not empty
		 */
		if (CollectionUtils.isEmpty(dcRequest.getDuplicateCopyApplications())) {
			return Collections.emptyList();
		}
		DuplicateCopy dcApplicationFromRequest = dcRequest.getDuplicateCopyApplications().get(0);

		propertyValidator.validatePaymentRequest(dcApplicationFromRequest.getApplicationNumber(),dcApplicationFromRequest.getPaymentAmount());


		DuplicateCopySearchCriteria dcCriteria = DuplicateCopySearchCriteria.builder()
				.applicationNumber(dcApplicationFromRequest.getApplicationNumber()).status(Collections.singletonList(PTConstants.DC_PENDINGPAYMENT))
				.build();

		/**
		 * Retrieve owner from db with the given ids.
		 */
		List<DuplicateCopy> dcApplicationsFromDB = repository.getDuplicateCopyProperties(dcCriteria);
		if (CollectionUtils.isEmpty(dcApplicationsFromDB)) {
			throw new CustomException(
					Collections.singletonMap("APPLICATION_NOT_FOUND", String.format("Could not find any valid application %s with pending payment state",
							dcApplicationFromRequest.getApplicationNumber())));
		}

		DuplicateCopy dcApplicationFromDB = dcApplicationsFromDB.get(0);
		BigDecimal totalDue=dcApplicationFromDB.getApplicant().get(0).getFeeAmount();

		/**
		 * Validate payment amount
		 */
		if(dcApplicationFromRequest.getPaymentAmount() < totalDue.doubleValue()) {
			throw new CustomException(Collections.singletonMap("INVALID_PAYMENT_AMOUNT",
					"Payment amount should be equal to due amount"));
		}

		dcApplicationFromDB.setTransactionId(dcApplicationFromRequest.getTransactionId());
		dcApplicationFromDB.setBankName(dcApplicationFromRequest.getBankName());
		dcApplicationFromDB.setPaymentAmount(dcApplicationFromRequest.getPaymentAmount());
		dcApplicationFromDB.setPaymentMode(dcApplicationFromRequest.getPaymentMode());

		/**
		 * Create egov user if not already present.
		 */
		userService.createUser(dcRequest.getRequestInfo(), dcApplicationFromDB.getApplicant().get(0).getPhone(),
				dcApplicationFromDB.getApplicant().get(0).getName(), dcApplicationFromDB.getApplicant().get(0).getTenantId());

		/**
		 * Get the bill generated.
		 */
		List<BillV2> bills = demandRepository.fetchBill(dcRequest.getRequestInfo(), dcApplicationFromDB.getTenantId(),
				dcApplicationFromDB.getApplicationNumber(), dcApplicationFromDB.getBillingBusinessService());
		if (CollectionUtils.isEmpty(bills)) {
			throw new CustomException("BILL_NOT_GENERATED", "No bills were found for the consumer code "
					+ dcApplicationFromDB.getApplicationNumber());
		}

		if (dcRequest.getRequestInfo().getUserInfo().getType().equalsIgnoreCase(PTConstants.ROLE_EMPLOYEE)) {
			/**
			 * if offline, create a payment.
			 */
			Applicant applicant = dcApplicationFromDB.getApplicant().get(0);

			OwnerDetails ownerDetail = OwnerDetails.builder().name(applicant.getName()).phone(applicant.getPhone()).build();

			Owner owner = Owner.builder().ownerDetails(ownerDetail).tenantId(applicant.getTenantId()).build();

			demandService.createCashPayment(dcRequest.getRequestInfo(), dcApplicationFromDB.getPaymentAmount(),dcApplicationFromDB.getTransactionId(),
					bills.get(0).getId(), owner, dcApplicationFromDB.getBillingBusinessService(),dcApplicationFromDB.getPaymentMode());

			dcRequest.setDuplicateCopyApplications(Collections.singletonList(dcApplicationFromDB));
			producer.push(config.getOwnershipTransferUpdateTopic(), dcRequest);

		}
		return Collections.singletonList(dcApplicationFromDB);

	}

}
