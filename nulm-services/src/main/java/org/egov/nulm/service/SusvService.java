
package org.egov.nulm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.idgen.model.IdGenerationResponse;
import org.egov.nulm.model.NulmSusvRequest;
import org.egov.nulm.model.ResponseInfoWrapper;
import org.egov.nulm.model.SusvApplication;
import org.egov.nulm.model.SusvApplicationDocument;
import org.egov.nulm.repository.SusvRepository;
import org.egov.nulm.util.AuditDetailsUtil;
import org.egov.nulm.util.IdGenRepository;
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
	
	private AuditDetailsUtil auditDetailsUtil;
	
	@Autowired
	public SusvService(SusvRepository repository, ObjectMapper objectMapper, IdGenRepository idgenrepository,
			NULMConfiguration config,AuditDetailsUtil auditDetailsUtil) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.idgenrepository = idgenrepository;
		this.config = config;
		this.auditDetailsUtil=auditDetailsUtil;

	}

	public ResponseEntity<ResponseInfoWrapper> createSusvApplication(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			
			String susvid = UUID.randomUUID().toString();
			susvApplication.setApplicationUuid(susvid);
			susvApplication.setDependentFamilyMembersArray(susvApplication.getDependentFamilyMembers()!=null?susvApplication.getDependentFamilyMembers().toJSONString():null);
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
			repository.createSusvApplication(susvApplication);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(susvApplication).build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	public ResponseEntity<ResponseInfoWrapper> updateSusvApplication(NulmSusvRequest request) {
		try {
			SusvApplication susvApplication = objectMapper.convertValue(request.getNulmSusvRequest(),
					SusvApplication.class);
			
			susvApplication.setDependentFamilyMembersArray(susvApplication.getDependentFamilyMembers()!=null?susvApplication.getDependentFamilyMembers().toJSONString():null);
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
			repository.updateSusvApplication(susvApplication);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(susvApplication).build(), HttpStatus.OK);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SUSV_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
}