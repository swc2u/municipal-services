
package org.egov.nulm.service;

import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.Role;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.idgen.model.IdGenerationResponse;
import org.egov.nulm.model.NulmSepRequest;
import org.egov.nulm.model.NulmSmidAlfRequest;
import org.egov.nulm.model.ResponseInfoWrapper;
import org.egov.nulm.model.SepApplication;
import org.egov.nulm.model.SmidAlfApplication;
import org.egov.nulm.repository.SmidAlfRepository;
import org.egov.nulm.repository.SmidShgRepository;
import org.egov.nulm.util.AuditDetailsUtil;
import org.egov.nulm.util.IdGenRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SmidALFService {

	private final ObjectMapper objectMapper;

	private NULMConfiguration config;

	private SmidAlfRepository repository;

	private IdGenRepository idgenrepository;

	private AuditDetailsUtil auditDetailsUtil;

	@Autowired
	public SmidALFService(SmidAlfRepository repository, ObjectMapper objectMapper, IdGenRepository idgenrepository,
			NULMConfiguration config, AuditDetailsUtil auditDetailsUtil) {
		this.objectMapper = objectMapper;
		this.repository = repository;
		this.idgenrepository = idgenrepository;
		this.config = config;
		this.auditDetailsUtil = auditDetailsUtil;

	}

	public ResponseEntity<ResponseInfoWrapper> createAlfApplication(NulmSmidAlfRequest request) {
		try {
			SmidAlfApplication shg = objectMapper.convertValue(request.getNulmSmidAlfRequest(), SmidAlfApplication.class);

			String uuid = UUID.randomUUID().toString();
			shg.setUuid(uuid);
			shg.setIsActive(true);
			shg.setAuditDetails(
					auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_CREATE));
			// idgen service call to genrate event id
			IdGenerationResponse id = idgenrepository.getId(request.getRequestInfo(), shg.getTenantId(),
					config.getSmidShgIdgenName(), config.getSmidShgIdgenFormat(), 1);
			if (id.getIdResponses() != null && id.getIdResponses().get(0) != null)
				shg.setId(id.getIdResponses().get(0).getId());
			else
				throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), CommonConstants.ID_GENERATION);

			repository.createGroup(shg);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(shg)
					.build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SMID_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	public ResponseEntity<ResponseInfoWrapper> updateAlfApplication(NulmSmidAlfRequest request) {
		try {
			SmidAlfApplication shg = objectMapper.convertValue(request.getNulmSmidAlfRequest(), SmidAlfApplication.class);

			shg.setAuditDetails(
					auditDetailsUtil.getAuditDetails(request.getRequestInfo(), CommonConstants.ACTION_UPDATE));
			
			repository.updateAlfApplication(shg);

			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build()).responseBody(shg)
					.build(), HttpStatus.CREATED);

		} catch (Exception e) {
			throw new CustomException(CommonConstants.SMID_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}
	
	public ResponseEntity<ResponseInfoWrapper> getAlfApplication(NulmSmidAlfRequest request) {
		try {

			SmidAlfApplication application = objectMapper.convertValue(request.getNulmSmidAlfRequest(),
					SmidAlfApplication.class);
			List<Role> role = request.getRequestInfo().getUserInfo().getRoles();
			List<SmidAlfApplication> applicationresult = repository.getAlfApplication(application, role,
					request.getRequestInfo().getUserInfo().getId());
			return new ResponseEntity<>(ResponseInfoWrapper.builder()
					.responseInfo(ResponseInfo.builder().status(CommonConstants.SUCCESS).build())
					.responseBody(applicationresult).build(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(CommonConstants.SMID_APPLICATION_EXCEPTION_CODE, e.getMessage());
		}
	}


}