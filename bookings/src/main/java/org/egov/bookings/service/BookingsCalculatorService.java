package org.egov.bookings.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.egov.bookings.contract.TaxHeadMasterFields;
import org.egov.bookings.models.demand.Demand;
import org.egov.bookings.models.demand.TaxHeadEstimate;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.common.contract.request.RequestInfo;


// TODO: Auto-generated Javadoc
/**
 * The Interface BookingsCalculatorService.
 */
public interface BookingsCalculatorService {

	/**
	 * Search demand.
	 *
	 * @param tenantId the tenant id
	 * @param consumerCodes the consumer codes
	 * @param requestInfo the request info
	 * @param businessService the business service
	 * @return the list
	 */
	public List<Demand> searchDemand(String tenantId, Set<String> consumerCodes, RequestInfo requestInfo,
			String businessService);
	
	/**
	 * Gets the tax head estimate.
	 *
	 * @param bookingsRequest the bookings request
	 * @param taxHeadCode1 the tax head code 1
	 * @param taxHeadCode2 the tax head code 2
	 * @return the tax head estimate
	 */
	public List<TaxHeadEstimate> getTaxHeadEstimate(BookingsRequest bookingsRequest, String taxHeadCode1,
			String taxHeadCode2);

	/**
	 * Gets the jurisdication amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the jurisdication amount
	 */
	public BigDecimal getJurisdicationAmount(BookingsRequest bookingsRequest);
	
	/**
	 * Gets the tax head master data.
	 *
	 * @param bookingsRequest the bookings request
	 * @param bussinessService the bussiness service
	 * @return the tax head master data
	 */
	public List<TaxHeadMasterFields> getTaxHeadMasterData(BookingsRequest bookingsRequest, String bussinessService);
	
	/**
	 * Gets the osbm amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the osbm amount
	 */
	public BigDecimal getOsbmAmount(BookingsRequest bookingsRequest);
	
	/**
	 * Gets the commercial amount.
	 *
	 * @param bookingsRequest the bookings request
	 * @return the commercial amount
	 */
	public BigDecimal getCommercialAmount(BookingsRequest bookingsRequest);
}
