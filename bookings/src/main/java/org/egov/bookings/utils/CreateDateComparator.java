package org.egov.bookings.utils;

import java.util.Comparator;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.RoomsModel;
import org.egov.bookings.validator.BookingsFieldsValidator;

public class CreateDateComparator implements Comparator<Object> {  
	
	public int compare(Object o1, Object o2) {
		int number = 0;
		if(o1 instanceof BookingsModel && o2 instanceof BookingsModel) {
			BookingsModel bookingModel1 =(BookingsModel)o1;  
			BookingsModel bookingModel2 =(BookingsModel)o2;  
			if (!BookingsFieldsValidator.isNullOrEmpty(bookingModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(bookingModel2.getLastModifiedDate())) {
				number = bookingModel1.getLastModifiedDate().compareTo(bookingModel2.getLastModifiedDate());  
			}
		}
		if(o1 instanceof RoomsModel && o2 instanceof RoomsModel) {
			RoomsModel roomsModel1 =(RoomsModel)o1;  
			RoomsModel roomsModel2 =(RoomsModel)o2;  
			if (!BookingsFieldsValidator.isNullOrEmpty(roomsModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(roomsModel2.getLastModifiedDate())) {
				number = roomsModel1.getLastModifiedDate().compareTo(roomsModel2.getLastModifiedDate());  
			}
		}
		return number;
	}  
}
