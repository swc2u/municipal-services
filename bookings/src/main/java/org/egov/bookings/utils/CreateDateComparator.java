package org.egov.bookings.utils;

import java.util.Comparator;

import org.egov.bookings.model.BookingsModel;

public class CreateDateComparator implements Comparator<Object> {  
	
	public int compare(Object o1, Object o2) {  
		BookingsModel bookingModel1 =(BookingsModel)o1;  
		BookingsModel bookingModel2 =(BookingsModel)o2;  
		return bookingModel1.getBkApplicationNumber().compareTo(bookingModel2.getBkApplicationNumber());  
	}  

}
