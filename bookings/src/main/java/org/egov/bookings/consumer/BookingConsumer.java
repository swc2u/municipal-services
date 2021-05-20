package org.egov.bookings.consumer;

import java.util.HashMap;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.contract.RefundTransactionRequest;
import org.egov.bookings.service.BookingsService;
import org.egov.bookings.service.notification.BookingNotificationService;
import org.egov.bookings.service.notification.NLUJMBookingNotificationService;
import org.egov.bookings.service.notification.RoomBookingNotificationService;
import org.egov.bookings.validator.BookingsFieldsValidator;
import org.egov.bookings.web.models.BookingsRequest;
import org.egov.bookings.web.models.NewLocationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookingConsumer {

	/** The notification service. */
	private BookingNotificationService notificationService;

	/** The utils. */
	private BookingsConfiguration utils;

	/** The nlujm booking notification service. */
	private NLUJMBookingNotificationService nlujmBookingNotificationService;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/** The bookings service. */
	private BookingsService bookingsService;
	
	/** The room booking notification service. */
	private RoomBookingNotificationService roomBookingNotificationService;
	
	/**
	 * Instantiates a new booking consumer.
	 *
	 * @param notificationService the notification service
	 * @param utils the utils
	 * @param nlujmBookingNotificationService the nlujm booking notification service
	 * @param roomBookingNotificationService the room booking notification service
	 * @param bookingsService the bookings service
	 */
	@Autowired
	public BookingConsumer(BookingNotificationService notificationService, BookingsConfiguration utils,
			NLUJMBookingNotificationService nlujmBookingNotificationService, RoomBookingNotificationService roomBookingNotificationService,
			BookingsService bookingsService) {
		this.notificationService = notificationService;
		this.utils = utils;
		this.nlujmBookingNotificationService = nlujmBookingNotificationService;
		this.roomBookingNotificationService = roomBookingNotificationService;
		this.bookingsService = bookingsService;
	}

	/**
	 * Listen.
	 *
	 * @param record the record
	 * @param topic  the topic
	 */
	@KafkaListener(topics = { "${kafka.topics.save.booking.sms.notification.service}",
			"${kafka.topics.update.sms.notification.service}", "${kafka.topics.save.nlujm.sms.notification.service}",
			"${kafka.topics.update.nlujm.sms.notification.service}", "${kafka.topics.refund.status}",
			"${kafka.topics.save.room.booking.sms.notification.service}", "${kafka.topics.update.room.booking.sms.notification.service}"})
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();

		if (utils.getSaveBookingSMSTopic().equals(topic) || utils.getUpdateBookingSMSTopic().equals(topic)) {
			BookingsRequest bookingsRequest = new BookingsRequest();
			try {
				log.info("Consuming record: " + record);
				bookingsRequest = mapper.convertValue(record, BookingsRequest.class);
			} catch (final Exception e) {
				log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			log.info("Booking Received: " + bookingsRequest.getBookingsModel().getBkApplicationNumber());
			notificationService.process(bookingsRequest);
		} else if (utils.getSaveNLUJMBookingSMSTopic().equals(topic) || utils.getUpdateNLUJMBookingSMSTopic().equals(topic)) {
			NewLocationRequest newLocationRequest = new NewLocationRequest();
			try {
				log.info("Consuming record: " + record);
				newLocationRequest = mapper.convertValue(record, NewLocationRequest.class);
			} catch (final Exception e) {
				log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			log.info("TradeLicense Received: " + newLocationRequest.getNewLocationModel().getApplicationNumber());
			nlujmBookingNotificationService.process(newLocationRequest);
		}
		else if (utils.getSaveRoomBookingSMSTopic().equals(topic) || utils.getUpdateRoomBookingSMSTopic().equals(topic)) {
			BookingsRequest bookingsRequest = new BookingsRequest();
			try {
				log.info("Consuming record: " + record);
				bookingsRequest = mapper.convertValue(record, BookingsRequest.class);
			} catch (final Exception e) {
				log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
			}
			log.info("Booking Received: " + bookingsRequest.getBookingsModel().getRoomsModel().get(0).getRoomApplicationNumber());
			roomBookingNotificationService.process(bookingsRequest);
		}
		
		try {
			if (utils.getPaymentRefundStatus().equals(topic)) {
				RefundTransactionRequest refundTransactionRequest = objectMapper.convertValue(record, RefundTransactionRequest.class);
				if(!BookingsFieldsValidator.isNullOrEmpty(refundTransactionRequest)) {
					bookingsService.persistRefundStatus(refundTransactionRequest);
					log.info("Consuming Refund Status Record: "+refundTransactionRequest);
				}
			}
		} catch (Exception e) {
			log.error("Error while listening to value: " + record + " on topic: " + topic + ": " + e);
		}
	}

}
