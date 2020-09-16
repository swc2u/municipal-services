
package org.egov.nulm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.idgen.model.IdGenerationResponse;
import org.egov.nulm.model.NulmShgMemberRequest;
import org.egov.nulm.model.NulmSusvRequest;
import org.egov.nulm.model.ResponseInfoWrapper;
import org.egov.nulm.model.SmidShgMemberApplication;
import org.egov.nulm.model.SusvApplication;
import org.egov.nulm.model.SusvApplicationDocument;
import org.egov.nulm.repository.SusvRepository;
import org.egov.nulm.util.AuditDetailsUtil;
import org.egov.nulm.util.IdGenRepository;
import org.egov.nulm.util.WorkFlowRepository;
import org.egov.nulm.workflow.model.ProcessInstance;
import org.egov.nulm.workflow.model.ProcessInstanceRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SusvService {

	private final ObjectMapper objectMapper;

	private NULMConfiguration config;

	private SusvRepository repository;

	private IdGenRepository idgenrepository;
	
	private WorkFlowRepository workFlowRepository;
	
	private AuditDetailsUtil auditDetailsUtil;
	
	@Autowired
	public SusvService(SusvRepository repository, ObjectMapper objectMapper, IdGenRepository idgenrepository,
			NULMConfiguration config,AuditDetailsUtil auditDetailsUtil,WorkFlowRepository workFlowRepository) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.idgenrepository = idgenrepository;
		this.config = config;
		this.auditDetailsUtil=auditDetailsUtil;
		this.workFlowRepository=workFlowRepository;

	}

	public ResponseEntity<ResponseInfoWrapper> createSusvApplication(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			
			String susvid = UUID.randomUUID().toString();
			susvApplication.setApplicationUuid(susvid);
			susvApplication.setIsActive(true);
			susvApplication.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
		   // idgen service call to genrate event id
			IdGenerationResponse id = idgenrepository.getId(request.getRequestInfo(), susvApplication.getTenantId(),
					config.getSusvApplicationNumberIdgenName(), config.getSusvApplicationNumberIdgenFormat(), 1);
			if (id.getIdResponses() != null && id.getIdResponses().get(0) != null)
				susvApplication.setApplicationId(id.getIdResponses().get(0).getId());
			else
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), CommonConstants.ID_GENERATION);

			// save document to nulm_susv_application_document table
			List<SusvApplicationDocument> susvdoc = new ArrayList<>();
			for (SusvApplicationDocument docobj : susvApplication.getApplicationDocument()) {
				SusvApplicationDocument documnet = new SusvApplicationDocument();
				documnet.setDocumnetUuid(UUID.randomUUID().toString());
				documnet.setApplicationUuid(susvid);
				documnet.setDocumentType(docobj.getDocumentType());
				documnet.setFilestoreId(docobj.getFilestoreId());
				documnet.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
				documnet.setIsActive(true);
				documnet.setTenantId(susvApplication.getTenantId());
				susvdoc.add(documnet);

			}
			susvApplication.setApplicationDocument(susvdoc);
			susvApplication.getSusvApplicationFamilyDetails().stream().forEach(element -> {
				element.setUuid( UUID.randomUUID().toString());
				element.setApplicationUuid(susvid);
				element.setIsActive(true);
				element.setTenantId(susvApplication.getTenantId());
				element.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
			
			});
		if(susvApplication.getApplicationStatus().toString()!=SusvApplication.StatusEnum.DRAFTED.toString()) {
			 //workflow service call to integrate tender workflow
			workflowIntegration(request.getRequestInfo(), susvApplication);
			
		}
			repository.createSusvApplication(susvApplication);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(susvApplication).build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	/**
	 * Method to integrate with workflow
	 *
	 * takes the tender request as parameter constructs the work-flow request
	 *
	 * and sets the resultant status from wf-response back to susv object
	 *
	 * @param susv object
	 */
	private void workflowIntegration(RequestInfo requestInfo ,SusvApplication susvApplication) {
		try {
			ProcessInstanceRequest workflowRequest = new ProcessInstanceRequest();
			workflowRequest.setRequestInfo(requestInfo);
			ProcessInstance processInstances = new ProcessInstance();
			processInstances.setTenantId(susvApplication.getTenantId());
			processInstances.setAction(susvApplication.getApplicationStatus()!=null ? susvApplication.getApplicationStatus().toString() :"");
			processInstances.setBusinessId(susvApplication.getApplicationId());
			processInstances.setModuleName(config.getBusinessservice());
			processInstances.setBusinessService(config.getBusinessservice());
			List<ProcessInstance> processList = Arrays.asList(processInstances);
			workflowRequest.setProcessInstances(processList);
			ResponseInfo workflowResponse = workFlowRepository.createWorkflowRequest(workflowRequest);
			if (workflowResponse == null || !workflowResponse.getStatus().equals(CommonConstants.SUCCESSFUL)) {
				throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE,
						(workflowResponse != null ? workflowResponse.getMsgId() : CommonConstants.WORKFLOW_MESSAGE));
			}
		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	public ResponseEntity<ResponseInfoWrapper> updateSusvApplication(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			
			
			susvApplication.setIsActive(true);
			susvApplication.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_UPDATE));
		   // update document to nulm_susv_application_document table
			List<SusvApplicationDocument> susvdoc = new ArrayList<>();
			for (SusvApplicationDocument docobj : susvApplication.getApplicationDocument()) {
				SusvApplicationDocument document = new SusvApplicationDocument();
				document.setDocumnetUuid(UUID.randomUUID().toString());
				document.setApplicationUuid(susvApplication.getApplicationUuid());
				document.setDocumentType(docobj.getDocumentType());
				document.setFilestoreId(docobj.getFilestoreId());
				document.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
				document.setIsActive(true);
				document.setTenantId(susvApplication.getTenantId());
				susvdoc.add(document);
				

			}
			susvApplication.setApplicationDocument(susvdoc);
			susvApplication.getSusvApplicationFamilyDetails().stream().forEach(element -> {
				element.setUuid( UUID.randomUUID().toString());
				element.setApplicationUuid(susvApplication.getApplicationUuid());
				element.setIsActive(true);
				element.setTenantId(susvApplication.getTenantId());
				element.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
			
			});
			if(susvApplication.getApplicationStatus().toString()!=SusvApplication.StatusEnum.DRAFTED.toString()) {
				 //workflow service call to integrate tender workflow
				workflowIntegration(request.getRequestInfo(), susvApplication);
				
			}
			repository.updateSusvApplication(susvApplication);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(susvApplication).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	public ResponseEntity<ResponseInfoWrapper> updateAppStatus(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			susvApplication.setIsActive(true);
			susvApplication.setAuditDetails(auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_UPDATE));
			if(susvApplication.getApplicationStatus().toString()!=SusvApplication.StatusEnum.DRAFTED.toString()) {
				 //workflow service call to integrate tender workflow
				workflowIntegration(request.getRequestInfo(), susvApplication);
				
			}
			repository.updateSusvApplicationStatus(susvApplication);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(susvApplication).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	public ResponseEntity<ResponseInfoWrapper> getSusvApplication(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			List<Role> role=request.getRequestInfo().getUserInfo().getRoles();
			List<SusvApplication> result = repository.getSusvApplication(susvApplication,role,request.getRequestInfo().getUserInfo().getId());
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(result).build(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
}