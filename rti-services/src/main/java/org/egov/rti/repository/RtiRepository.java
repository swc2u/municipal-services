
package org.egov.rti.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.rti.model.MinistryMaster;
import org.egov.rti.model.RequestInfoWrapper;
import org.egov.rti.repository.builder.RtiQueryBuilder;
import org.egov.rti.repository.rowmapper.RtiRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;




@Repository
public class RtiRepository {

	private JdbcTemplate jdbcTemplate;


	private RtiRowMapper rowMapper;
	
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public RtiRepository(JdbcTemplate jdbcTemplate, RtiRowMapper rowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = rowMapper;
	}

	public List<MinistryMaster> getDepartment(RequestInfoWrapper request) {
		List<MinistryMaster> list = new ArrayList<>();
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("userId", request.getRequestInfo().getUserInfo().getId());
	
		return list = namedParameterJdbcTemplate.query(RtiQueryBuilder.GET_MINISTRY_MASTER_QUERY, paramValues, rowMapper);
		
	}
}
