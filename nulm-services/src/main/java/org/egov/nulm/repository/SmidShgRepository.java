
package org.egov.nulm.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.Role;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.model.NulmShgRequest;
import org.egov.nulm.model.SmidShgGroup;
import org.egov.nulm.producer.Producer;
import org.egov.nulm.repository.builder.NULMQueryBuilder;
import org.egov.nulm.repository.rowmapper.ShgRowMapper;
import org.egov.tracer.model.CustomException;
import org.javers.common.collections.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SmidShgRepository {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private Producer producer;

	private NULMConfiguration config;

	private ShgRowMapper shgrowMapper;

	@Autowired
	public SmidShgRepository(JdbcTemplate jdbcTemplate, Producer producer, NULMConfiguration config,
			ShgRowMapper shgrowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.shgrowMapper = shgrowMapper;
	}

	public void createGroup(SmidShgGroup shg) {
		NulmShgRequest infoWrapper = NulmShgRequest.builder().nulmShgRequest(shg).build();
		producer.push(config.getSMIDSHGSaveTopic(), infoWrapper);
	}

	public List<SmidShgGroup> getGroup(SmidShgGroup shg, List<Role> role, Long userId) {
		List<SmidShgGroup> smid = new ArrayList<>();
		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("tenantId", shg.getTenantId());
		try {
			for (Role roleobj : role) {
				if ((roleobj.getCode()).equalsIgnoreCase(config.getRoleEmployee())) {
					List<Object> statusEmplyee = new ArrayList<>();
					if (shg.getStatus() == null) {
						statusEmplyee.add(SmidShgGroup.StatusEnum.APPROVED.toString());
						statusEmplyee.add(SmidShgGroup.StatusEnum.AWAITINGFORAPPROVAL.toString());
						statusEmplyee.add(SmidShgGroup.StatusEnum.DELETED.toString());
					} else {
						statusEmplyee.add(shg.getStatus().toString());
					}
					paramValues.put("status", statusEmplyee);
					paramValues.put("createdBy", "");
					paramValues.put("shgUuid", shg.getShgUuid());

					return smid = namedParameterJdbcTemplate.query(NULMQueryBuilder.GET_SHG_QUERY, paramValues,
							shgrowMapper);

				}
			}
			List<Object> statusCitizen = new ArrayList<>();
			if (shg.getStatus() == null) {
				statusCitizen.add(SmidShgGroup.StatusEnum.CREATED.toString());
				statusCitizen.add(SmidShgGroup.StatusEnum.DRAFTED.toString());
				statusCitizen.add(SmidShgGroup.StatusEnum.APPROVED.toString());
				statusCitizen.add(SmidShgGroup.StatusEnum.AWAITINGFORAPPROVAL.toString());
				statusCitizen.add(SmidShgGroup.StatusEnum.DELETED.toString());
			} else {
				statusCitizen.add(shg.getStatus().toString());
			}
			statusCitizen.add(shg.getStatus() == null ? "" : shg.getStatus().toString());
			paramValues.put("status", statusCitizen);
			paramValues.put("createdBy", userId.toString());
			paramValues.put("shgUuid", shg.getShgUuid());

			return smid = namedParameterJdbcTemplate.query(NULMQueryBuilder.GET_SHG_QUERY, paramValues, shgrowMapper);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(CommonConstants.ROLE, e.getMessage());
		}

	}

	public void updateGroup(SmidShgGroup shg) {
		NulmShgRequest infoWrapper = NulmShgRequest.builder().nulmShgRequest(shg).build();
		producer.push(config.getSMIDSHGUpdateTopic(), infoWrapper);
	}

	public void deleteGroup(SmidShgGroup shg) {
		NulmShgRequest infoWrapper = NulmShgRequest.builder().nulmShgRequest(shg).build();
		producer.push(config.getSMIDSHGDeleteTopic(), infoWrapper);
	}

	public void updateGroupStatus(SmidShgGroup shg) {
		NulmShgRequest infoWrapper = NulmShgRequest.builder().nulmShgRequest(shg).build();
		producer.push(config.getSMIDSHGUpdateStatusTopic(), infoWrapper);
	}

	public void checkMemberCount(SmidShgGroup shg) {
		Map<String, String> errorMap = new HashMap<>();
		int i = 0;
		i = jdbcTemplate.queryForObject(NULMQueryBuilder.GET_SHG_MEMBER_COUNT_QUERY,
				new Object[] { shg.getShgUuid(), shg.getTenantId() }, Integer.class);

		if (i < 10) {
			errorMap.put(CommonConstants.INVALID_SHG_REQUEST, CommonConstants.INVALID_SHG_REQUEST_MESSAGE);
			throw new CustomException(errorMap);
		}
	}
}
