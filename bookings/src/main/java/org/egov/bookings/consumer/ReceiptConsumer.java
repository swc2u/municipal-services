package org.egov.bookings.consumer;

import java.util.HashMap;

import org.egov.bookings.service.PaymentUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiptConsumer {
	@Autowired
	private PaymentUpdateService paymentUpdateService;
	
	@KafkaListener(topics = "egov.collection.payment-create")
    public void listenPayments(final HashMap<String, Object> record) {
		System.out.println("I am able to listen!!!");
        paymentUpdateService.process(record);
    }
}
