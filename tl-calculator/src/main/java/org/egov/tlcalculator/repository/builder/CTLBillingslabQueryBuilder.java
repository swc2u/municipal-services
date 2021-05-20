package org.egov.tlcalculator.repository.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tlcalculator.web.models.CTLBillingSlabSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CTLBillingslabQueryBuilder {
	
	/**
	 * Builds search query for searching billing slabs from db
	 * @param criteria
	 * @param preparedStmtList
	 * @return
	 */
	public String getSearchQuery(CTLBillingSlabSearchCriteria criteria, List<Object> preparedStmtList) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * from eg_ctl_billingslab ");
		addWhereClause(queryBuilder, criteria, preparedStmtList);
		return queryBuilder.toString();
	}
	
	/**
	 * Builds the where clause for the search query. 
	 * @param queryBuilder
	 * @param billingSlabSearcCriteria
	 * @param preparedStmtList
	 */
	public void addWhereClause(StringBuilder queryBuilder, CTLBillingSlabSearchCriteria billingSlabSearcCriteria, List<Object> preparedStmtList) {
		queryBuilder.append(" WHERE tenantid = ?");
		preparedStmtList.add(billingSlabSearcCriteria.getTenantId());
		List<String> ids = billingSlabSearcCriteria.getIds();
		
		if (!CollectionUtils.isEmpty(ids)) {
			queryBuilder.append(" AND id IN ( ");
			setValuesForList(queryBuilder, preparedStmtList, ids);
			queryBuilder.append(")");
		}

		if (!StringUtils.isEmpty(billingSlabSearcCriteria.getBusinessService())) {
			queryBuilder.append(" AND businessservice = ?");
			preparedStmtList.add(billingSlabSearcCriteria.getBusinessService());
		}

		if (!StringUtils.isEmpty(billingSlabSearcCriteria.getFeeType())) {
			queryBuilder.append(" AND  feetype = ?");
			preparedStmtList.add(billingSlabSearcCriteria.getFeeType());
		}

		if (!StringUtils.isEmpty(billingSlabSearcCriteria.getApplicationType())) {
			queryBuilder.append(" AND applicationtype = ? ");
			preparedStmtList.add(billingSlabSearcCriteria.getApplicationType());
		}

		if (!StringUtils.isEmpty(billingSlabSearcCriteria.getUom())) {
			queryBuilder.append(" AND uom = ?");
			preparedStmtList.add(billingSlabSearcCriteria.getUom());
		}

		if (null != billingSlabSearcCriteria.getFrom()) {
			queryBuilder.append(" AND fromUom <= ?");
			preparedStmtList.add(billingSlabSearcCriteria.getFrom());
		}
		
		if (null != billingSlabSearcCriteria.getTo()) {
			queryBuilder.append(" AND toUom >= ?");
			preparedStmtList.add(billingSlabSearcCriteria.getTo());
		}

		if (null != billingSlabSearcCriteria.getUomValue()) {
			queryBuilder.append(" AND fromUom <= ?");
			preparedStmtList.add(billingSlabSearcCriteria.getUomValue());
		}

		if (null != billingSlabSearcCriteria.getUomValue()) {
			queryBuilder.append(" AND toUom > ?");
			preparedStmtList.add(billingSlabSearcCriteria.getUomValue());
		}
	}

	/**
	 * Sets prepared statement for values for a list
	 * 
	 * @param queryBuilder
	 * @param preparedStmtList
	 * @param ids
	 */
	private void setValuesForList(StringBuilder queryBuilder, List<Object> preparedStmtList, List<String> ids) {
		int len = ids.size();
		for (int i = 0; i < ids.size(); i++) {
			queryBuilder.append("?");
			if (i != len - 1)
				queryBuilder.append(", ");
			preparedStmtList.add(ids.get(i));
		}
	}

}
