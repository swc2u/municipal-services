package org.egov.bookings.service;

import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.OsbmApproverModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsbmApproverService.
 */
public interface OsbmApproverService {

	/**
	 * Creates the osbm approver.
	 *
	 * @param osbmApproverRequest the osbm approver request
	 * @return the osbm approver model
	 */
	public OsbmApproverModel createOsbmApprover(OsbmApproverRequest osbmApproverRequest);

}
