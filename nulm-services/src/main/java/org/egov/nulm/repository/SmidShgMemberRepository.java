
package org.egov.nulm.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.Role;
import org.egov.nulm.common.CommonConstants;
import org.egov.nulm.config.NULMConfiguration;
import org.egov.nulm.model.NulmShgMemberRequest;
import org.egov.nulm.model.SmidShgGroup;
import org.egov.nulm.model.SmidShgMemberApplication;
import org.egov.nulm.producer.Producer;
import org.egov.nulm.repository.builder.NULMQueryBuilder;
import org.egov.nulm.repository.rowmapper.SMIDRowMapper;
import org.egov.nulm.repository.rowmapper.ShgMemberRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SmidShgMemberRepository {

	private JdbcTemplate jdbcTemplate;

	private Producer producer;

	private NULMConfiguration config;

	private ShgMemberRowMapper shgMemberRowMapper;

	@Autowired
	public SmidShgMemberRepository(JdbcTemplate jdbcTemplate, Producer producer, NULMConfiguration config,
			ShgMemberRowMapper shgMemberRowMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.producer = producer;
		this.config = config;
		this.shgMemberRowMapper = shgMemberRowMapper;
	}

	public void createMembers(SmidShgMemberApplication smidApplication) {
		NulmShgMemberRequest infoWrapper = NulmShgMemberRequest.builder().SmidShgMemberApplication(smidApplication)
				.build();
		producer.push(config.getSmidShgMemberSaveTopic(), infoWrapper);
	}

	public void updateMembers(SmidShgMemberApplication smidApplication) {
		NulmShgMemberRequest infoWrapper = NulmShgMemberRequest.builder().SmidShgMemberApplication(smidApplication)
				.build();
		producer.push(config.getSmidShgMemberUpdateTopic(), infoWrapper);
	}

	public void deleteMembers(SmidShgMemberApplication smidApplication) {
		NulmShgMemberRequest infoWrapper = NulmShgMemberRequest.builder().SmidShgMemberApplication(smidApplication)
				.build();
		producer.push(config.getSmidShgMemberDeleteTopic(), infoWrapper);
	}

	public void checkShgUuid(SmidShgMemberApplication smidapplication) {
		Map<String, String> errorMap = new HashMap<>();
		int i = 0;
		i = jdbcTemplate.queryForObject(NULMQueryBuilder.SHG_UUID_EXIST_QUERY,
				new Object[] { smidapplication.getShgUuid(), smidapplication.getTenantId() }, Integer.class);

		if (i == 0) {
			errorMap.put(CommonConstants.INVALID_SHG_UUID, CommonConstants.INVALID_SHG_UUID_MESSAGE);
			throw new CustomException(errorMap);
		}
	}

	public List<SmidShgMemberApplication> getMembers(SmidShgMemberApplication memberrequest, List<Role> role,
			Long userId) {
		List<SmidShgMemberApplication> smid = new ArrayList<>();
		try {
			for (Role roleobj : role) {
				if ((roleobj.getCode()).equalsIgnoreCase(config.getRoleEmployee())) {
					return smid = jdbcTemplate.query(NULMQueryBuilder.GET_SHG_MEMBER_QUERY,
							new Object[] { memberrequest.getApplicationUuid(), memberrequest.getApplicationUuid(), "",
									"", memberrequest.getTenantId(),
									memberrequest.getApplicationStatus() == null ? ""
											: memberrequest.getApplicationStatus().toString(),
									memberrequest.getApplicationStatus() == null ? ""
											: memberrequest.getApplicationStatus().toString() },
							shgMemberRowMapper);

				}
			}
			return smid = jdbcTemplate.query(NULMQueryBuilder.GET_SHG_MEMBER_QUERY,
					new Object[] { memberrequest.getApplicationUuid(), memberrequest.getApplicationUuid(),
							userId.toString(), userId.toString(), memberrequest.getTenantId(),
							memberrequest.getApplicationStatus() == null ? ""
									: memberrequest.getApplicationStatus().toString(),
							memberrequest.getApplicationStatus() == null ? ""
									: memberrequest.getApplicationStatus().toString() },
					shgMemberRowMapper);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(CommonConstants.ROLE, e.getMessage());
		}

	}
}
