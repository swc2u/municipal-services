package org.egov.rti.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.rti.common.CommonConstants;
import org.egov.rti.model.MinistryMaster;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RtiRowMapper implements ResultSetExtractor<List<MinistryMaster>> {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<MinistryMaster> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, MinistryMaster> map = new HashMap<>();
		List<MinistryMaster> list = new ArrayList<>();

		try {
			while (rs.next()) {
				String id = rs.getString("ministry_code");
				
				if (!map.containsKey(id)) {
									
					MinistryMaster master = MinistryMaster.builder()
							.effectiveFrom(rs.getString("effective_from"))
							.effectiveTo(rs.getString("effective_to"))
							.ministryCode(rs.getString("ministry_code"))
							.ministryName(rs.getString("ministry_name"))
							.userId(rs.getString("user_id"))
							.username(rs.getString("username"))
							.userTenantId(rs.getString("user_tenantId"))
							.build();
					
					
					map.put(id, master);
					list.add(master);
				}
			}

		} catch (Exception e) {
			throw new CustomException(CommonConstants.RTI_EXCEPTION_CODE, e.getMessage());
		}
		return list;
	}

}
