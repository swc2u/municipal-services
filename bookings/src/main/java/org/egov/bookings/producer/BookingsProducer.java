package org.egov.bookings.producer;

import org.egov.bookings.contract.EmailRequest;
import org.egov.tracer.kafka.CustomKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class BookingsProducer.
 */
@Service

/** The Constant log. */
@Slf4j
public class BookingsProducer {

	/** The kafka template. */
	@Autowired
	private CustomKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * Push.
	 *
	 * @param topic the topic
	 * @param value the value
	 */
	public void push(String topic, Object value) {
		log.info("Value: " + value.toString());
		log.info("Topic: " + topic);
		kafkaTemplate.send(topic, value);

	}

	/**
	 * Push email.
	 *
	 * @param emailTopic the email topic
	 * @param request    the request
	 */
	public void pushEmail(String emailTopic, EmailRequest request) {
		kafkaTemplate.send(emailTopic, request);
	}

	/**
	 * Push email.
	 *
	 * @param emailTopic   the email topic
	 * @param emailAddress the email address
	 * @param message      the message
	 * @param subject      the subject
	 * @param isHTML       the is HTML
	 */
	public void pushEmail(String emailTopic, String emailAddress, String message, String subject, Boolean isHTML) {
		// kafkaTemplate.send(topic, value);
		final EmailRequest emailMessage = EmailRequest.builder().body(message).subject(subject).email(emailAddress)
				.isHTML(isHTML).build();
		kafkaTemplate.send(emailTopic, emailMessage);
	}
}
