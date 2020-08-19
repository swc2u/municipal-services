
package org.egov.nulm.repository;

import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.model.NulmSusvRequest;
import org.egov.nulm.model.SusvApplication;
import org.egov.nulm.model.SusvApplicationDocument;
import org.egov.nulm.producer.Producer;
import org.egov.nulm.repository.builder.NULMQueryBuilder;
import org.egov.nulm.repository.rowmapper.SEPRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SusvRepository {

	private JdbcTemplate jdbcTemplate;

	private Producer producer;

	private NULMConfiguration config;

	private SEPRowMapper seprowMapper;

	@Autowired
	public SusvRepository(JdbcTemplate jdbcTemplate, Producer producer, NULMConfiguration config,
			SEPRowMapper seprowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.seprowMapper = seprowMapper;
	}

	public void createSusvApplication(SusvApplication susvApplication) {
		NulmSusvRequest infoWrapper = NulmSusvRequest.builder().nulmSusvRequest(susvApplication).build();
		producer.push(config.getSusvApplicationSaveTopic(), infoWrapper);
	}
	
	public void updateSusvApplication(SusvApplication susvApplication) {
		NulmSusvRequest infoWrapper = NulmSusvRequest.builder().nulmSusvRequest(susvApplication).build();
		producer.push(config.getSusvApplicationUpdateTopic(), infoWrapper);
	}
	
	public int checkDocExist(SusvApplicationDocument susvApplication, String appId, String tenantId) {

		return jdbcTemplate.queryForObject(NULMQueryBuilder.GET_SUSV_DOCUMENT_QUERY,
				new Object[] { appId, tenantId, susvApplication.getFilestoreId(), susvApplication.getDocumentType() },
				Integer.class);
	}
}
