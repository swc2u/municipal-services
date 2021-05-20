package org.egov.bookings.service;


import java.util.List;
import java.util.Set;

import org.egov.bookings.contract.BillResponse;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.GenerateBillCriteria;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;

public interface DemandService {

	public void createDemand(BookingsRequest bookingsRequest);

	public void updateDemand(BookingsRequest bookingsRequest);
	
	public BillResponse generateBill(RequestInfo requestInfo,GenerateBillCriteria billCriteria);
	
	public List<Demand> searchDemand(String tenantId, Set<String> consumerCodes, RequestInfo requestInfo,
			String businessService);
	
	public List<Demand> updateDemandsForPacc(BookingsRequest bookingsRequest);

	public void createDemandForRoom(BookingsRequest bookingsRequest);

	public void updateDemandForRoom(BookingsRequest bookingsRequest);

}
