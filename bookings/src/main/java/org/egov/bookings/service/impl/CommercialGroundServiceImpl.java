package org.egov.bookings.service.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.apache.commons.collections.map.HashedMap;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommercialGrndAvailabilityRepository;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class CommercialGroundFeeServiceImpl.
 */
@Service
@Transactional
public class CommercialGroundServiceImpl implements CommercialGroundService {

	/** The commercial ground fee repository. */
	@Autowired
	private CommercialGroundRepository commercialGroundRepository;

	/** The common repository. */
	@Autowired
	CommonRepository commonRepository;

	/** The bookings repository. */
	@Autowired
	private BookingsRepository bookingsRepository;

	/** The commercial grnd availability repository. */
	@Autowired
	private CommercialGrndAvailabilityRepository commercialGrndAvailabilityRepository;

	/** The enrichment service. */
	@Autowired
	private EnrichmentService enrichmentService;

	@Autowired
	private BookingsConfiguration config;

	private Lock lock = new ReentrantLock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.CommercialGroundFeeService#
	 * searchCommercialGroundFee(org.egov.bookings.contract.
	 * CommercialGroundFeeSearchCriteria)
	 */
	@Override
	public CommercialGroundFeeModel searchCommercialGroundFee(
			CommercialGroundFeeSearchCriteria commercialGroundFeeSearchCriteria) {
		List<CommercialGroundFeeModel> commercialGroundFeeModelList = null;
		CommercialGroundFeeModel commercialGroundFeeModel = null;
		try {
			LocalDate currentDate = LocalDate.now();
			commercialGroundFeeModelList = commercialGroundRepository.findByBookingVenueAndCategory(
					commercialGroundFeeSearchCriteria.getBookingVenue(),
					commercialGroundFeeSearchCriteria.getCategory());
			if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModelList)) {
				throw new CustomException("DATA_NOT_FOUND","There is not any amount for this commercial ground criteria in database");
			}
			for(CommercialGroundFeeModel commercialGroundFeeModel1 : commercialGroundFeeModelList) {
				
				if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel1.getFromDate())) {
					throw new CustomException("DATA_NOT_FOUND","There is no from date for this commercial ground criteria in database");
				}
				
				String pattern = "yyyy-MM-dd";
				DateFormat df = new SimpleDateFormat(pattern);
				String fromDateInString = df.format(commercialGroundFeeModel1.getFromDate());
				LocalDate fromDate = LocalDate.parse(fromDateInString);
				//LocalDate toDate = LocalDate.parse(toDateInString);
				if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel1.getToDate()) && currentDate.isAfter(fromDate) || currentDate.isEqual(fromDate)) {
					//toDateInString = df.format(osbmFeeModel1.getToDate());
					commercialGroundFeeModel = commercialGroundFeeModel1;
				}
				if (!BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel1.getToDate())
						&& (fromDate.isEqual(currentDate) || fromDate.isBefore(currentDate))
						&& (currentDate.isBefore(LocalDate.parse(df.format(commercialGroundFeeModel1.getToDate()))))) {
					commercialGroundFeeModel = commercialGroundFeeModel1;
					break;
				}
			}
			
			if(BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel)) {
				throw new CustomException("DATA_NOT_FOUND","There is not any amount for this commercial ground criteria in database");
			}
			
		} catch (Exception e) {
			throw new CustomException("DATABASE_FETCH_ERROR", e.getLocalizedMessage());
		}
		return commercialGroundFeeModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.bookings.service.CommercialGroundService#
	 * searchCommercialGroundAvailabilty(org.egov.bookings.contract.
	 * CommercialGroundAvailabiltySearchCriteria)
	 */
	@Override
	public Set<AvailabilityResponse> searchCommercialGroundAvailabilty(
			CommercialGroundAvailabiltySearchCriteria commercialGroundAvailabiltySearchCriteria) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		Set<AvailabilityResponse> bookedDates = new HashSet<>();
		Set<BookingsModel> bookingsModel = commonRepository.findAllBookedVenuesFromNow(
				commercialGroundAvailabiltySearchCriteria.getBookingVenue(),
				commercialGroundAvailabiltySearchCriteria.getBookingType(), date1, BookingsConstants.APPLY);
		for (BookingsModel bkModel : bookingsModel) {
			bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate())
					.toDate(bkModel.getBkToDate()).build());
		}

		return bookedDates;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.CommercialGroundService#lockCommercialAvailability(
	 * org.egov.bookings.model.CommercialGrndAvailabilityModel)
	 */
	@Override
	public CommercialGrndAvailabilityModel lockCommercialAvailability(
			CommercialGrndAvailabilityModel commercialGrndAvailabilityModel) {
		CommercialGrndAvailabilityModel commGrndAvail = null;
		try {
			commGrndAvail = commercialGrndAvailabilityRepository.save(commercialGrndAvailabilityModel);
			return commGrndAvail;
		} catch (Exception e) {
			throw new CustomException("DATABASE_ERROR", e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.bookings.service.CommercialGroundService#fetchBookedDates(org.egov.
	 * bookings.web.models.BookingsRequest)
	 */
	@Override
	public Set<Date> fetchBookedDates(BookingsRequest bookingsRequest) {

		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		SortedSet<Date> bookedDates = new TreeSet<>();
		
		try {
			List<LocalDate> toBeBooked = enrichmentService.extractAllDatesBetweenTwoDates(bookingsRequest);
			lock.lock();
			if (config.isCommercialLock()) {
				Set<BookingsModel> bookingsModelSet = commonRepository.findAllBookedVenuesFromNow(
						bookingsRequest.getBookingsModel().getBkBookingVenue(),
						bookingsRequest.getBookingsModel().getBkBookingType(), date1, BookingsConstants.APPLY);

				List<LocalDate> fetchBookedDates = enrichmentService.enrichBookedDates(bookingsModelSet);
				
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
			config.setCommercialLock(true);
			throw new CustomException("OTHER_PAYMENT_IN_PROCESS", "Please try after few seconds");
		}
		return bookedDates;

	}

}
