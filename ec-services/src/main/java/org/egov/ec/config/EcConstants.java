package org.egov.ec.config;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class EcConstants {
	
	


	public static final String MDM_TEMPLATE_AUCTION_NOTIFICATION = "AuctionNotificationTemplate";
	public static final String MDMS_TEMPLATE_PATH = "$.MdmsRes.egec";
	public static final String ROLE_STORE_MANAGER="challanSM";
	public static final String MDM_MODULE = "egov-echallan";

	public static final String NOTIFICATION_LOCALE = "en_IN";

	//success constants
	
	public static final String STATUS_SUCCESSFULL = "Successful";
	
	public static final String STATUS_SUCCESS = "Success";

	
	
	// error constants

	public static final String INVALID_TENANT_ID_MDMS_KEY = "INVALID TENANTID";

	public static final String INVALID_TENANT_ID_MDMS_MSG = "No data found for this tenentID";

	public static final String INVALID_FINE_DATA = "Fine Data does not exist";

	public static final String FAILED_IDGEN_CHALLANID = "ChallanID Generation Failed";

	// EC actions


	public static final String ACTION_APPROVE = "APPROVE";

	public static final String ACTION_REJECT = "REJECT";

	public static final String STATUS_APPROVED = "APPROVED";

	public static final String STATUS_REJECTED = "REJECTED";

	public static final String STATUS_PAID = "PAID";

	public static final String STATUS_PENDING = "PENDING";
	
	public static final String STATUS_CLOSED = "CLOSED";
	
	public static final String STATUS_FAILED = "FAILED";
	
	public static final String STATUS_AUCTION_PENDING = "PENDING FOR AUCTION";
	
	//workflows

	public static final String WORKFLOW_AUCTION = "AUCTION WORKFLOW";
	
	public static final String WORKFLOW_CHALLAN = "CHALLAN WORKFLOW";
	
	public static final String WORKFLOW_PAYMENT = "PAYMENT WORKFLOW";

	public static final String WORKFLOW_FINE = "FINE MASTER APPROVAL";
	

	public static final String WORKFLOW_MODULE = "ECHALLAN";
	
}
