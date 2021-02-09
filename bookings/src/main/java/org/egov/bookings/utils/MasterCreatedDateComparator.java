package org.egov.bookings.utils;

import java.util.Comparator;

import org.egov.bookings.model.CommercialGroundFeeModel;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.model.OsbmFeeModel;
import org.egov.bookings.model.OsujmFeeModel;
import org.egov.bookings.model.ParkCommunityHallV1MasterModel;
import org.egov.bookings.validator.BookingsFieldsValidator;

public class MasterCreatedDateComparator implements Comparator<Object> {
	
	public int compare(Object o1, Object o2) {  
		int number = 0;
		if(o1 instanceof OsbmApproverModel && o2 instanceof OsbmApproverModel) {
			OsbmApproverModel osbmApproverModel1 =(OsbmApproverModel)o1;  
			OsbmApproverModel osbmApproverModel2 =(OsbmApproverModel)o2;
			if (!BookingsFieldsValidator.isNullOrEmpty(osbmApproverModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(osbmApproverModel2.getLastModifiedDate())) {
				number = osbmApproverModel1.getLastModifiedDate().compareTo(osbmApproverModel2.getLastModifiedDate()); 
			}
		}
		else if(o1 instanceof OsbmFeeModel && o2 instanceof OsbmFeeModel) {
			OsbmFeeModel osbmFeeModel1 =(OsbmFeeModel)o1;  
			OsbmFeeModel osbmFeeModel2 =(OsbmFeeModel)o2; 
			if (!BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(osbmFeeModel2.getLastModifiedDate())) {
				number = osbmFeeModel1.getLastModifiedDate().compareTo(osbmFeeModel2.getLastModifiedDate()); 
			}
		}
		else if(o1 instanceof OsujmFeeModel && o2 instanceof OsujmFeeModel) {
			OsujmFeeModel osujmFeeModel1 =(OsujmFeeModel)o1;  
			OsujmFeeModel osujmFeeModel2 =(OsujmFeeModel)o2;  
			if (!BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(osujmFeeModel2.getLastModifiedDate())) {
				number = osujmFeeModel1.getLastModifiedDate().compareTo(osujmFeeModel2.getLastModifiedDate()); 
			}
		}
		else if(o1 instanceof CommercialGroundFeeModel && o2 instanceof CommercialGroundFeeModel) {
			CommercialGroundFeeModel commercialGroundFeeModel1 =(CommercialGroundFeeModel)o1;  
			CommercialGroundFeeModel commercialGroundFeeModel2 =(CommercialGroundFeeModel)o2;  
			if (!BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(commercialGroundFeeModel2.getLastModifiedDate())) {
				number = commercialGroundFeeModel1.getLastModifiedDate().compareTo(commercialGroundFeeModel2.getLastModifiedDate()); 
			}
		}
		else if(o1 instanceof ParkCommunityHallV1MasterModel && o2 instanceof ParkCommunityHallV1MasterModel) {
			ParkCommunityHallV1MasterModel parkCommunityHallV1MasterModel1 =(ParkCommunityHallV1MasterModel)o1;  
			ParkCommunityHallV1MasterModel parkCommunityHallV1MasterModel2 =(ParkCommunityHallV1MasterModel)o2; 
			if (!BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel1.getLastModifiedDate()) && !BookingsFieldsValidator.isNullOrEmpty(parkCommunityHallV1MasterModel2.getLastModifiedDate())) {
				number = parkCommunityHallV1MasterModel1.getLastModifiedDate().compareTo(parkCommunityHallV1MasterModel2.getLastModifiedDate()); 
			}
		}
		return number;
	} 
}
