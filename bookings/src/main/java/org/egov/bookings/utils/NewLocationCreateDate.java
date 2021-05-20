package org.egov.bookings.utils;

import java.util.Comparator;

import org.egov.bookings.model.OsujmNewLocationModel;

public class NewLocationCreateDate implements Comparator<Object> {
	
	public int compare(Object o1, Object o2) {  
		OsujmNewLocationModel osujmNewLocationModel1 =(OsujmNewLocationModel)o1;  
		OsujmNewLocationModel osujmNewLocationModel2 =(OsujmNewLocationModel)o2;  
		return osujmNewLocationModel1.getApplicationNumber().compareTo(osujmNewLocationModel2.getApplicationNumber());  
	}  
}
