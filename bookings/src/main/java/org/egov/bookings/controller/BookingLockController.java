package org.egov.bookings.controller;

import org.egov.bookings.common.model.ResponseModel;
import org.egov.bookings.contract.BookingLockRequest;
import org.egov.bookings.service.BookingLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
public class BookingLockController {

	@Autowired
	private BookingLockService bookingLockService;

	@PostMapping("/delete/lock/_dates")
	private ResponseEntity<?> saveCardDetails(@RequestBody BookingLockRequest bookingLockRequest) {

		long Long = bookingLockService
				.deleteLockDates(bookingLockRequest.getBookingLockModel().get(0).getApplicationNumber());
		ResponseModel rs = new ResponseModel();
		if (Long == 0) {
			rs.setStatus("400");
			rs.setMessage("Failure while Deleting details");
			rs.setData("Failure");
		} else {
			rs.setStatus("200");
			rs.setMessage("Booking Lock Released successfully");
			rs.setData("Success");
		}
		return ResponseEntity.ok(rs);
	}

}
