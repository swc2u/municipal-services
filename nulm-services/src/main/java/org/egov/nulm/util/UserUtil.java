package org.egov.nulm.util;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Collections;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.model.Errors;
import org.egov.nulm.model.Organization;
import org.egov.nulm.model.OrganizationRequest;
import org.egov.nulm.user.model.CreateUserRequest;
import org.egov.nulm.user.model.UserDetailResponse;
import org.egov.nulm.user.model.UserRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserUtil {

	RestTemplate restTemplate;

	private NULMConfiguration config;

	private AuditDetailsUtil auditDetailsUtil;

	private final ObjectMapper objectMapper;

	@Autowired
	public UserUtil(RestTemplate restTemplate, NULMConfiguration config, AuditDetailsUtil auditDetailsUtil,
			ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.config = config;
		this.auditDetailsUtil = auditDetailsUtil;
		this.objectMapper = objectMapper;

	}
	
	
	public UserDetailResponse createUser(OrganizationRequest request) throws IOException {
		String url = config.getUserHost() + config.getUserPath();
		ResponseInfo responseInfo = null;
		UserDetailResponse userresponse=null;
		try {
			Organization organization = objectMapper.convertValue(request.getOrganizationRequest(), Organization.class);
			String tenantId = organization.getTenantId();
			CreateUserRequest req = new CreateUserRequest();
			UserRequest user = new UserRequest();
			user.setUserName(organization.getMobileNo());
			user.setMobileNumber(organization.getMobileNo());
			user.setTenantId(tenantId.split("\\.")[0]);
			user.setPermanentCity(tenantId.split("\\.")[0]);
			user.setName(organization.getRepresentativeName());
			user.setCorrespondenceAddress(organization.getAddress());
			user.setEmailId(organization.getEmailId());
			user.setCreatedBy(request.getRequestInfo().getUserInfo()!=null?request.getRequestInfo().getUserInfo().getId().toString():"");
			user.setLastModifiedBy(request.getRequestInfo().getUserInfo()!=null?request.getRequestInfo().getUserInfo().getId().toString():"");			
			user.setActive(true);
			user.setType("CITIZEN");
			Role role = getCitizenRole(organization.getTenantId());
			user.setRoles(Collections.singletonList(role));

			JsonNode response = restTemplate.postForObject(url, new CreateUserRequest(request.getRequestInfo(), user), JsonNode.class);

			if (!isNull(response)) {
				 userresponse = objectMapper.convertValue(response, UserDetailResponse.class);
				log.info("User Created Successfully : " + userresponse.getUser().get(0));
			} else {
				log.info("User Creation Failed : Reason " + response);
				throw new CustomException(CommonConstants.USER_CREATION, "Server Down");
			}
		} catch (HttpStatusCodeException e) {
			log.debug(e.getResponseBodyAsString());
			Errors errors = objectMapper.readValue(e.getResponseBodyAsString(), Errors.class);
			throw new CustomException(CommonConstants.USER_CREATION,
					errors == null ? "Server Down" : errors.getError().get(0).getCode());
		}
		return userresponse;
	}

	/**
	 * Creates citizen role
	 * 
	 * @return Role object for citizen
	 */
	private Role getCitizenRole(String tenantId) {
		Role role = new Role();
		role.setCode("NGO");
		role.setName("NGO");
		role.setTenantId(tenantId.split("\\.")[0]);
		return role;
	}

}
