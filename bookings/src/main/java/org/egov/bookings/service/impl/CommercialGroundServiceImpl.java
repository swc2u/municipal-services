package org.egov.bookings.service.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.map.HashedMap;
import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.AvailabilityResponse;
import org.egov.bookings.contract.CommercialGrndAvailabiltyLockRequest;
import org.egov.bookings.contract.CommercialGroundAvailabiltySearchCriteria;
import org.egov.bookings.contract.CommercialGroundFeeSearchCriteria;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.producer.BookingsProducer;
import org.egov.bookings.repository.BookingsRepository;
import org.egov.bookings.repository.CommercialGrndAvailabilityRepository;
import org.egov.bookings.repository.CommercialGroundRepository;
import org.egov.bookings.repository.CommonRepository;
import org.egov.bookings.repository.ParkCommunityHallV1MasterRepository;
import org.egov.bookings.service.CommercialGroundService;
import org.egov.bookings.utils.BookingsConstants;
import org.egov.bookings.utils.BookingsUtils;
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

	@Autowired
	private BookingsProducer bookingsProducer;
	
	@Autowired
	private CommercialGrndAvailabilityRepository commercialGrndAvailabilityRepo;
	
	@Autowired
	private BookingsUtils bookingsUtils;
	
	@Autowired
	private ParkCommunityHallV1MasterRepository parkCommunityHallV1MasterRepository; 
	
	@Autowired
	private BookingsRepository bookingsRepo; 
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
		Set<CommercialGrndAvailabilityModel> availabilityLockModelList = commercialGrndAvailabilityRepo
				.findByBookingVenueAndIsLocked(commercialGroundAvailabiltySearchCriteria.getBookingVenue(), date1);
		Set<BookingsModel> bookingsModel = commonRepository.findAllBookedVenuesFromNow(
				commercialGroundAvailabiltySearchCriteria.getBookingVenue(),
				commercialGroundAvailabiltySearchCriteria.getBookingType(), date1, BookingsConstants.APPLY);
		for (BookingsModel bkModel : bookingsModel) {
			bookedDates.add(AvailabilityResponse.builder().fromDate(bkModel.getBkFromDate())
					.toDate(bkModel.getBkToDate()).build());
		}
		if (!BookingsFieldsValidator.isNullOrEmpty(availabilityLockModelList)) {
			for (CommercialGrndAvailabilityModel availabilityLockModel : availabilityLockModelList) {
				if (availabilityLockModel.isLocked()) {
					bookedDates.add(AvailabilityResponse.builder().fromDate(availabilityLockModel.getFromDate())
							.toDate(availabilityLockModel.getToDate()).build());

				}
			}
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
	public List<CommercialGrndAvailabilityModel> saveCommercialAvailabilityLockDates(
			CommercialGrndAvailabiltyLockRequest commercialGrndAvailabiltyLockRequest) {
		 
			
			Set<BookingsModel>	bookingModelSet = new LinkedHashSet<>();
			Set<BookingsModel>	bookingModelSet1 = new LinkedHashSet<>();
			LocalDate date = LocalDate.now();
			Date date1 = Date.valueOf(date);
			for (CommercialGrndAvailabilityModel availabiltyModel : commercialGrndAvailabiltyLockRequest
					.getCommercialGrndAvailabilityLock()) {
					bookingModelSet1=bookingsRepo.findByBkBookingVenueAndBkSectorAndBkToDateGreaterThanEqualAndBkPaymentStatus(availabiltyModel.getBookingVenue(),
							availabiltyModel.getSector(),date1,BookingsConstants.PAYMENT_SUCCESS_STATUS);
					if(!BookingsFieldsValidator.isNullOrEmpty(bookingModelSet1))
					bookingModelSet.addAll(bookingModelSet1);
			}
			Set<LocalDate> fetchBookedDates = enrichmentService.enrichBookedDates(bookingModelSet);
			for(CommercialGrndAvailabilityModel dateHolding : commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock()) {
				for(LocalDate localDate : fetchBookedDates) {
					if(localDate.compareTo(dateHolding.getFromDate().toLocalDate()) == 0) {
						return null;
					}
				}
			}
			for(CommercialGrndAvailabilityModel commercialGrndAvailabilityModel : commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock()) {
				DateFormat formatter = bookingsUtils.getSimpleDateFormat();
				commercialGrndAvailabilityModel.setCreatedDate(formatter.format(new java.util.Date()));
				commercialGrndAvailabilityModel.setLastModifiedDate(formatter.format(new java.util.Date()));	
			}
			bookingsProducer.push(config.getSaveCommercialGrndLockedDates(), commercialGrndAvailabiltyLockRequest);
			//commGrndAvail = commercialGrndAvailabilityRepository.save(commercialGrndAvailabilityModel);
			return commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock();
	
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
		enrichmentService.enrichLockDates(bookingsRequest);
		// Date date = commercialGroundAvailabiltySearchCriteria.getDate();
		LocalDate date = LocalDate.now();
		Date date1 = Date.valueOf(date);
		SortedSet<Date> bookedDates = new TreeSet<>();
		LocalDate sixMonthsFromNow = date.plusMonths(6);
		Date currentDate = Date.valueOf(date);
		Date sixMonthsFromNowSql = Date.valueOf(sixMonthsFromNow);
		try {
			lock.lock();
			Set<LocalDate> toBeBooked = enrichmentService.extractAllDatesBetweenTwoDates(bookingsRequest);
			if (config.isCommercialLock()) {
				Set<BookingsModel> bookingsModelSet = commonRepository.findAllBookedVenuesFromNow(
						bookingsRequest.getBookingsModel().getBkBookingVenue(),
						bookingsRequest.getBookingsModel().getBkBookingType(), date1, BookingsConstants.APPLY);

				Set<LocalDate> fetchBookedDates = enrichmentService.enrichBookedDates(bookingsModelSet);
				List<CommercialGrndAvailabilityModel> lockList = commercialGrndAvailabilityRepo
						.findLockedDatesFromNowTo6Months(currentDate, sixMonthsFromNowSql);
				for (LocalDate toBeBooked1 : toBeBooked) {
					for (LocalDate fetchBookedDates1 : fetchBookedDates) {
						if (toBeBooked1.equals(fetchBookedDates1)) {
							bookedDates.add(Date.valueOf(toBeBooked1));
						}
					}
				}
				for (LocalDate toBeBooked1 : toBeBooked) {
					for (CommercialGrndAvailabilityModel commGrndAvailModel : lockList) {
						if (BookingsConstants.VENUE_TYPE_COMMERCIAL.equals(commGrndAvailModel.getVenueType())
								&& commGrndAvailModel.isLocked() && bookingsRequest.getBookingsModel()
										.getBkBookingVenue().equals(commGrndAvailModel.getBookingVenue())) {
							if (toBeBooked1.equals(commGrndAvailModel.getFromDate().toLocalDate()))
								bookedDates.add(commGrndAvailModel.getFromDate());
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

	
	
	
	@Override
	public List<CommercialGrndAvailabilityModel> updateCommercialAvailabilityLockDates(
			CommercialGrndAvailabiltyLockRequest commercialGrndAvailabiltyLockRequest) {
		try {
			for(CommercialGrndAvailabilityModel commercialGrndAvailabilityModel : commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock()) {
				commercialGrndAvailabilityRepo.delete(commercialGrndAvailabilityModel.getId());
				//DateFormat formatter = bookingsUtils.getSimpleDateFormat();
				//commercialGrndAvailabilityModel.setLastModifiedDate(formatter.format(new java.util.Date()));	
			}
			//bookingsProducer.push(config.getUpdateCommercialGrndLockedDates(), commercialGrndAvailabiltyLockRequest);
			//commGrndAvail = commercialGrndAvailabilityRepository.save(commercialGrndAvailabilityModel);
			return commercialGrndAvailabiltyLockRequest.getCommercialGrndAvailabilityLock();
		} catch (Exception e) {
			throw new CustomException("DATABASE__PERSISTER_ERROR", e.getLocalizedMessage());
		}
	}

	@Override
	public List<CommercialGrndAvailabilityModel> fetchLockedDates() {
		List<CommercialGrndAvailabilityModel> lockList = null;
		LocalDate date = LocalDate.now();
		LocalDate sixMonthsFromNow = date.plusMonths(6);
		Date currentDate = Date.valueOf(date);
		Date sixMonthsFromNowSql = Date.valueOf(sixMonthsFromNow);
		lockList = commercialGrndAvailabilityRepo.findLockedDatesFromNowTo6Months(currentDate, sixMonthsFromNowSql);
		List<CommercialGrndAvailabilityModel> dupliLockList = new ArrayList<>();
		// iteration on locl 
		lockList.stream().forEach(commGrndAvailabilitty -> {
			CommercialGrndAvailabilityModel model = new CommercialGrndAvailabilityModel();
			model.setFromDate(commGrndAvailabilitty.getFromDate());
			model.setBookingVenue(commGrndAvailabilitty.getBookingVenue());
			model.setCreatedDate(commGrndAvailabilitty.getCreatedDate());
			model.setId(commGrndAvailabilitty.getId());
			model.setLastModifiedDate(commGrndAvailabilitty.getLastModifiedDate());
			model.setToDate(commGrndAvailabilitty.getToDate());
			model.setVenueType(commGrndAvailabilitty.getVenueType());
			model.setLocked(commGrndAvailabilitty.isLocked());
			model.setReasonForHold(commGrndAvailabilitty.getReasonForHold());
			model.setSector(commGrndAvailabilitty.getSector());
			if (BookingsConstants.VENUE_TYPE_COMMUNITY_CENTER.equals(commGrndAvailabilitty.getVenueType())
					|| BookingsConstants.VENUE_TYPE_PARKS.equals(commGrndAvailabilitty.getVenueType())) {
			ParkCommunityHallV1MasterModel parkCommunityHallV1MasterModel = parkCommunityHallV1MasterRepository
					.findById(commGrndAvailabilitty.getBookingVenue());
			if (!BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel)) {
				if (!BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel.getName()))
					model.setBookingVenue(parkCommunityHallV1MasterModel.getName());
			}}
			
			dupliLockList.add(model);
		});
		//CommercialGrndAvailabilityModel l1 = new CommercialGrndAvailabilityModel();
		/*for (CommercialGrndAvailabilityModel l1 : dupliLockList) {
			if (BookingsConstants.VENUE_TYPE_COMMUNITY_CENTER.equals(l1.getVenueType())
					|| BookingsConstants.VENUE_TYPE_PARKS.equals(l1.getVenueType())) {
				ParkCommunityHallV1MasterModel parkCommunityHallV1MasterModel = parkCommunityHallV1MasterRepository
						.findById(l1.getBookingVenue());
				if (!BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel)) {
					if (!BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel.getName()))
						l1.setBookingVenue(parkCommunityHallV1MasterModel.getName());
				}
			}
		}*/

		if (BookingsFieldsValidator.isNullOrEmpty(dupliLockList)) {
			return dupliLockList;
		}
		List<CommercialGrndAvailabilityModel> sortedLockList = dupliLockList.stream()
				.sorted((l1, l2) -> -(l1.getLastModifiedDate().compareTo(l2.getLastModifiedDate())))
				.collect(Collectors.toList());
		return sortedLockList;
	}
}
