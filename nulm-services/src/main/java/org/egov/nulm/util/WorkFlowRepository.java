package org.egov.nulm.util;


import static java.util.Objects.isNull;

import java.io.IOException;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.model.Errors;
import org.egov.nulm.workflow.model.ProcessInstanceRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class WorkFlowRepository {



    private RestTemplate restTemplate;

    private NULMConfiguration config;
    
    private ObjectMapper objectMapper;

    @Autowired
    public WorkFlowRepository(RestTemplate restTemplate, NULMConfiguration config,ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.objectMapper=objectMapper;
    }


	public ResponseInfo createWorkflowRequest(ProcessInstanceRequest workflowRequest) throws IOException {
		String url = config.getWorkFlowHost() + config.getWorkFlowPath();
		ResponseInfo responseInfo = null;
		try {
			JsonNode response = restTemplate.postForObject(url, workflowRequest, JsonNode.class);

			if (!isNull(response)) {
				responseInfo = objectMapper.convertValue(response.get("ResponseInfo"), ResponseInfo.class);
				log.info("Workflow Created Success : " + responseInfo.getMsgId());
			} else {
				log.info("Workflow Creation Failed : Reason " + response);
				throw new CustomException("WORKFLOW_EXCEPTION", "Server Down");
			}
		} catch (HttpStatusCodeException e) {
			log.debug(e.getResponseBodyAsString());
			Errors errors = objectMapper.readValue(e.getResponseBodyAsString(), Errors.class);
			throw new CustomException("WORKFLOW_EXCEPTION",
					errors == null ? "Server Down" : errors.getError().get(0).getMessage());
		}
		return responseInfo;
	}


}
