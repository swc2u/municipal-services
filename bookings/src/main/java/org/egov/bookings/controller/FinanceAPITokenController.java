package org.egov.bookings.controller;

import javax.validation.Valid;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.config.FinanceConfiguration;
import org.egov.bookings.contract.RequestInfoWrapper;
import org.egov.bookings.finance.contract.FinanceAuthResponse;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.utils.BookingsUtils;
import org.egov.bookings.utils.FinanceUtils;
import org.egov.bookings.web.models.BookingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class FinanceAPITokenController {
	
	@Autowired
	private FinanceUtils bookingsUtils;
	
	
	@PostMapping("/generate/_token")
	private ResponseEntity<?> getAccessToken(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper) {
		
		FinanceAuthResponse financeAuthResponce = bookingsUtils.getAccessToken();
		ResponseModel rs = new ResponseModel();
		if (financeAuthResponce == null) {
			rs.setStatus("400");
			rs.setMessage("Failure while getting finance token");
			rs.setData(financeAuthResponce);
		} else {
			rs.setStatus("200");
			rs.setMessage("Token Generated");
			rs.setData(financeAuthResponce);
		}
		return ResponseEntity.ok(rs);
	}
	
}
