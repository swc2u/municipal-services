package org.egov.ps.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.ps.config.Configuration;
import org.egov.ps.model.Application;
import org.egov.ps.model.ApplicationCriteria;
import org.egov.ps.model.BillV2;
import org.egov.ps.model.OfflinePaymentDetails;
import org.egov.ps.producer.Producer;
import org.egov.ps.repository.ApplicationRepository;
import org.egov.ps.repository.PropertyRepository;
import org.egov.ps.service.calculation.DemandRepository;
import org.egov.ps.service.calculation.DemandService;
import org.egov.ps.util.PSConstants;
import org.egov.ps.validator.ApplicationValidatorService;
import org.egov.ps.web.contracts.ApplicationRequest;
import org.egov.ps.web.contracts.RequestInfoMapper;
import org.egov.ps.web.contracts.State;
import org.egov.ps.workflow.WorkflowIntegrator;
import org.egov.ps.workflow.WorkflowService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ApplicationService {

	@Autowired
	private ApplicationEnrichmentService applicationEnrichmentService;

	@Autowired
	private ApplicationsNotificationService applicationNotificationService;

	@Autowired
	private Configuration config;

	@Autowired
	private Producer producer;

	@Autowired
	ApplicationValidatorService validator;

	@Autowired
	PropertyRepository repository;

	@Autowired
	ApplicationRepository applicationRepository;

	@Autowired
	WorkflowIntegrator wfIntegrator;

	@Autowired
	private DemandService demandService;

	@Autowired
	private WorkflowService wfService;

	@Autowired
	private DemandRepository demandRepository;

	public List<Application> createApplication(ApplicationRequest request) {
		validator.validateCreateRequest(request);
		applicationEnrichmentService.enrichCreateApplication(request);
		producer.push(config.getSaveApplicationTopic(), request);
		return request.getApplications();
	}

	public List<Application> searchApplication(ApplicationCriteria criteria, RequestInfo requestInfo) {
		if (criteria.getFileNumber() != null) {
			criteria.setFileNumber(criteria.getFileNumber().toUpperCase());
		}
		List<Application> applications = applicationRepository.getApplications(criteria);

		if (CollectionUtils.isEmpty(applications)) {
			return Collections.emptyList();
		}
		return applications;
	}

	public List<Application> updateApplication(ApplicationRequest applicationRequest) {
		validator.getApplications(applicationRequest);
		applicationEnrichmentService.enrichUpdateApplication(applicationRequest);
		String action = applicationRequest.getApplications().get(0).getAction();
		String state = applicationRequest.getApplications().get(0).getState();

		if (state.contains(PSConstants.EM_STATE_PENDING_DA_FEE)) {
			demandService.createDemand(applicationRequest.getRequestInfo(), applicationRequest.getApplications());
		}
		if (config.getIsWorkflowEnabled() && !action.contentEquals("")) {
			wfIntegrator.callApplicationWorkFlow(applicationRequest);
		}
		producer.push(config.getUpdateApplicationTopic(), applicationRequest);

		applicationNotificationService.processNotifications(applicationRequest);
		return applicationRequest.getApplications();
	}

	public List<State> getStates(RequestInfoMapper requestInfoWrapper) {

		List<State> status = wfService.getApplicationStatus("ch", requestInfoWrapper);

		return status;
	}

	public List<Application> generateFinanceDemand(ApplicationRequest applicationRequest) {
		ApplicationCriteria criteria = validator.getApplicationCriteria(applicationRequest);

		List<Application> applications = applicationRepository.getApplications(criteria);
		Application application = applications.get(0);

		if (CollectionUtils.isEmpty(applications)) {
			return Collections.emptyList();
		}
		/**
		 * Get the bill generated.
		 */
		List<BillV2> bills = demandRepository.fetchBill(applicationRequest.getRequestInfo(), application.getTenantId(),
				application.getPaymentConsumerCode(), application.getBillingBusinessService());
		if (CollectionUtils.isEmpty(bills)) {
			throw new CustomException("BILL_NOT_GENERATED",
					"No bills were found for the consumer code " + application.getPaymentConsumerCode());
		}

		/**
		 * create an offline payment.
		 */
		demandService.createCashPayment(applicationRequest.getRequestInfo(), application.getPaymentAmount(),
				bills.get(0).getId(), application, application.getBillingBusinessService());

		OfflinePaymentDetails offlinePaymentDetails = OfflinePaymentDetails.builder().id(UUID.randomUUID().toString())
				.propertyId(application.getId()).demandId(bills.get(0).getBillDetails().get(0).getDemandId())
				.amount(application.getPaymentAmount()).bankName(application.getBankName())
				.transactionNumber(application.getTransactionId()).build();
		application.setOfflinePaymentDetails(Collections.singletonList(offlinePaymentDetails));
		applicationRequest.setApplications(Collections.singletonList(application));
		producer.push(config.getUpdatePropertyTopic(), applicationRequest);
		return null;
	}
}
