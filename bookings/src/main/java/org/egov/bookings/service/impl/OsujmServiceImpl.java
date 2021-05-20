package org.egov.bookings.service.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.JurisdictionAvailabilityRequest;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.OsujmFeeRepository;
import org.egov.bookings.service.OsujmService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class OsujmServiceImpl.
 */
@Service
@Transactional
public class OsujmServiceImpl implements OsujmService {

	/** The osujm fee repository. */
	@Autowired
	private OsujmFeeRepository osujmFeeRepository;

	/** The common repository. */
	@Autowired
	private CommonRepository commonRepository;
	
	@Autowired
	private EnrichmentService enrichmentService;
	
	@Autowired
	BookingsConfiguration config;
	
	private Lock lock = new ReentrantLock();
	
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsujmService#findJurisdictionFee(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public OsujmFeeModel findJurisdictionFee(BookingsRequest bookingsRequest) {
		List<OsujmFeeModel> osujmFeeModelList = null;
		OsujmFeeModel osujmFeeModel1 = null;
		try {
			LocalDate currentDate =LocalDate.now();
			Long area = Long.valueOf(bookingsRequest.getBookingsModel().getBkAreaRequired());
			String sector = bookingsRequest.getBookingsModel().getBkSector();
			if(!sector.equals("SECTOR-17") && !sector.equals("SECTOR-22"))
				sector = "OTHER";
			osujmFeeModelList = osujmFeeRepository.findJurisdictionFee(area,sector);
			
			for(OsujmFeeModel osujmFeeModel : osujmFeeModelList) {
				if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel.getFromDate())) {
					throw new IllegalArgumentException("There is no from date for this open space under jurisdiction criteria in database");
				}
				String pattern = "yyyy-MM-dd";
				DateFormat df = new SimpleDateFormat(pattern);
				String fromDateInString = df.format(osujmFeeModel.getFromDate());
				LocalDate fromDate = LocalDate.parse(fromDateInString);
				if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel.getToDate()) && currentDate.isAfter(fromDate) || currentDate.isEqual(fromDate)) {
					osujmFeeModel1 = osujmFeeModel;
				}
				if (!BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel.getToDate())
						&& (fromDate.isEqual(currentDate) || fromDate.isBefore(currentDate))
						&& (currentDate.isBefore(LocalDate.parse(df.format(osujmFeeModel.getToDate()))))) {
					osujmFeeModel1 = osujmFeeModel;
					break;
				}
			}
			
			if(BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel1)) {
				throw new IllegalArgumentException("There is not any amount for open space under jurisdiction criteria in database");
			}
			
		}
		catch (Exception e) {
			throw new CustomException("INVALID_REQUEST",e.getLocalizedMessage());
		}
		
		return osujmFeeModel1;
	}

	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsujmService#searchJurisdictionAvailability(org.egov.bookings.contract.JurisdictionAvailabilityRequest)
	 */
	@Override
	public Set<AvailabilityResponse> searchJurisdictionAvailability(
			JurisdictionAvailabilityRequest jurisdictionAvailabilityRequest) {

        LocalDate date = LocalDate.now();
        Date date1 = Date.valueOf(date);
        Set<AvailabilityResponse> bookedDates = new HashSet<>();
        
        Set<BookingsModel> bookingsModel = commonRepository.searchJurisdictionAvailability(
        		jurisdictionAvailabilityRequest.getBookingVenue(),
        		jurisdictionAvailabilityRequest.getBookingType(),
        		jurisdictionAvailabilityRequest.getBkSector(),
				date1,BookingsConstants.PAYMENT_SUCCESS_STATUS);
		for(BookingsModel bkModel : bookingsModel) {
			bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate()).toDate(bkModel.getBkToDate()).build());
		}
		
		return bookedDates;

		
	}

	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsujmService#fetchBookedDates(org.egov.bookings.web.models.BookingsRequest)
	 */
	@Override
	public Set<Date> fetchBookedDates(BookingsRequest bookingsRequest) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		SortedSet<Date> bookedDates = new TreeSet<>();

		lock.lock();
		try {
			if (config.isJurisdictionLock()) {
				config.setJurisdictionLock(false);
				Set<BookingsModel> bookingsModel = commonRepository.searchJurisdictionAvailability(
						bookingsRequest.getBookingsModel().getBkBookingVenue(),
						bookingsRequest.getBookingsModel().getBkBookingType(),
						bookingsRequest.getBookingsModel().getBkSector(), date1, BookingsConstants.PAYMENT_SUCCESS_STATUS);
				List<LocalDate> fetchBookedDates = enrichmentService.enrichBookedDates(bookingsModel);
				List<LocalDate> toBeBooked = enrichmentService.extractAllDatesBetweenTwoDates(bookingsRequest);
				for (LocalDate toBeBooked1 : toBeBooked) {

					for (LocalDate fetchBookedDates1 : fetchBookedDates) {
						if (toBeBooked1.equals(fetchBookedDates1)) {
							bookedDates.add(Date.valueOf(toBeBooked1));
						}
					}

				}
			} else {
				lock.unlock();
				throw new CustomException("OTHER_PAYMENT_IN_PROCESS", "Please try after few seconds");
			}
			lock.unlock();

		} catch (Exception e) {
			lock.unlock();
			config.setJurisdictionLock(true);
			throw new CustomException("OTHER_PAYMENT_IN_PROCESS", "Please try after few seconds");
		}
		return bookedDates;

	}
	
}
