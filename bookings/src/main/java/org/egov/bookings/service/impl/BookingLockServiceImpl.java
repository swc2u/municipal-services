package org.egov.bookings.service.impl;

import javax.transaction.Transactional;

import org.egov.bookings.repository.BookingLockRepository;
import org.egov.bookings.service.BookingLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookingLockServiceImpl implements BookingLockService {

	@Autowired
	private BookingLockRepository bookingLockRepository;

	@Override
	public long deleteLockDates(String bkApplicationNumber) {
		try {
			return bookingLockRepository.deleteByApplicationNumber(bkApplicationNumber);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
