package org.egov.bookings.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingsConstants.
 */
@Component
public class BookingsConstants {

	/**
	 * Instantiates a new bookings constants.
	 */
	private BookingsConstants() {
	}

	/** The Constant SERV_REQ_ID_NAME. */
	public static final String SERV_REQ_ID_NAME = "pgr.servicerequestid";

	/** The Constant SERV_REQ_ID_FORMAT. */
	public static final String SERV_REQ_ID_FORMAT = "[cy:dd]/[cy:MM]/[cy:yyyy]/[SEQ_EG_PGR_SERVICEREQUESTID]";

	/** The Constant TEMPLATE_COMPLAINT_EMAIL. */
	// Notification
	public static final String TEMPLATE_COMPLAINT_EMAIL = "./src/main/resources/email-templates/velocityEmailNotifSample.vm";

	/** The Constant SEARCHER_PGR_MOD_NAME. */
	public static final String SEARCHER_PGR_MOD_NAME = "rainmaker-pgr-V2";

	/** The Constant SEARCHER_SRSEARCH_DEF_NAME. */
	public static final String SEARCHER_SRSEARCH_DEF_NAME = "serviceSearchWithDetails";

	/** The Constant SEARCHER_PLAINSEARCH_DEF_NAME. */
	public static final String SEARCHER_PLAINSEARCH_DEF_NAME = "plainSearch";

	/** The Constant SEARCHER_COUNT_DEF_NAME. */
	public static final String SEARCHER_COUNT_DEF_NAME = "count";

	/** The Constant PG_JSONPATH_COUNT. */
	public static final String PG_JSONPATH_COUNT = "$.count[0].count";

	/** The Constant SEARCHER_SRID_ASSIGNEDTO_DEF_NAME. */
	public static final String SEARCHER_SRID_ASSIGNEDTO_DEF_NAME = "getServiceRequestsOnAssignedTo";

	/** The Constant SRID_ASSIGNEDTO_JSONPATH. */
	public static final String SRID_ASSIGNEDTO_JSONPATH = "$.servicesRequestIds.*.businesskey";

	/** The Constant MDMS_PGR_MOD_NAME. */
	public static final String MDMS_PGR_MOD_NAME = "RAINMAKER-PGR";

	/** The Constant MDMS_SERVICETYPE_MASTER_NAME. */
	public static final String MDMS_SERVICETYPE_MASTER_NAME = "ServiceDefs";

	/** The Constant MDMS_COMMON_MASTERS_MODULE_NAME. */
	public static final String MDMS_COMMON_MASTERS_MODULE_NAME = "common-masters";

	/** The Constant MDMS_DEPT_MASTERS_MASTER_NAME. */
	public static final String MDMS_DEPT_MASTERS_MASTER_NAME = "Department";

	/** The Constant MDMS_DESIGNATION_MASTERS_MASTER_NAME. */
	public static final String MDMS_DESIGNATION_MASTERS_MASTER_NAME = "Designation";

	/** The Constant LOCALIZATION_MODULE_NAME. */
	public static final String LOCALIZATION_MODULE_NAME = "rainmaker-pgr";

	/** The Constant LOCALIZATION_CODE_SUBMIT_CITIZEN. */
	public static final String LOCALIZATION_CODE_SUBMIT_CITIZEN = "pgr.sms.notification.submit.citizen";

	/** The Constant LOCALIZATION_CODE_REOPEN_CITIZEN. */
	public static final String LOCALIZATION_CODE_REOPEN_CITIZEN = "pgr.sms.notification.reopen.citizen";

	/** The Constant LOCALIZATION_CODE_REOPEN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_REOPEN_EMPLOYEE = "pgr.sms.notification.reopen.employee";

	/** The Constant LOCALIZATION_CODE_ASSIGN_CITIZEN. */
	public static final String LOCALIZATION_CODE_ASSIGN_CITIZEN = "pgr.sms.notification.assign.citizen";


	/** The Constant LOCALIZATION_CODE_ASSIGN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_ASSIGN_EMPLOYEE = "pgr.sms.notification.assign.employee";

	/** The Constant LOCALIZATION_CODE_REASSIGN_CITIZEN. */
	public static final String LOCALIZATION_CODE_REASSIGN_CITIZEN = "pgr.sms.notification.reassign.citizen";

	/** The Constant LOCALIZATION_CODE_REASSIGN_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_REASSIGN_EMPLOYEE = "pgr.sms.notification.reassign.employee";

	/** The Constant LOCALIZATION_CODE_REJECT_CITIZEN. */
	public static final String LOCALIZATION_CODE_REJECT_CITIZEN = "pgr.sms.notification.reject.citizen";

	/** The Constant LOCALIZATION_CODE_RESOLVE_CITIZEN. */
	public static final String LOCALIZATION_CODE_RESOLVE_CITIZEN = "pgr.sms.notification.resolve.citizen";

	/** The Constant LOCALIZATION_CODE_CLOSE_EMPLOYEE. */
	public static final String LOCALIZATION_CODE_CLOSE_EMPLOYEE = "pgr.sms.notification.close.employee";

	/** The Constant LOCALIZATION_CODE_COMMENT. */
	public static final String LOCALIZATION_CODE_COMMENT = "pgr.sms.notification.comment";

	/** The Constant LOCALIZATION_CODE_DEFAULT. */
	public static final String LOCALIZATION_CODE_DEFAULT = "pgr.sms.notification.default";

	/** The Constant LOCALIZATION_CODE_COMMENT_DEFAULT. */
	public static final String LOCALIZATION_CODE_COMMENT_DEFAULT = "pgr.sms.notification.comment.default";

	/** The Constant LOCALIZATION_COMP_CATEGORY_PREFIX. */
	public static final String LOCALIZATION_COMP_CATEGORY_PREFIX = "pgr.complaint.category.";

	/** The Constant SERVICE_CODES. */
	public static final String SERVICE_CODES = "serviceCode";

	/** The Constant JSONPATH_SERVICEDEFS. */
	public static final String JSONPATH_SERVICEDEFS = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs";

	/** The Constant JSONPATH_SERVICE_CODES. */
	public static final String JSONPATH_SERVICE_CODES = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.serviceCode";

	/** The Constant JSONPATH_SLA. */
	public static final String JSONPATH_SLA = "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.slaHours";

	/** The Constant JSONPATH_DEPARTMENTS. */
	public static final String JSONPATH_DEPARTMENTS = "$.MdmsRes.common-masters.Department";

	/** The Constant JSONPATH_DESIGNATIONS. */
	public static final String JSONPATH_DESIGNATIONS = "$.MdmsRes.common-masters.Designation";

	/** The Constant SERVICE_NAME. */
	public static final String SERVICE_NAME = "serviceName";

	/** The Constant DEFAULT_COMPLAINT_TYPE. */
	public static final String DEFAULT_COMPLAINT_TYPE = "resolution";

	/** The Constant EMPLOYEE_DEPTCODES_JSONPATH. */
	public static final String EMPLOYEE_DEPTCODES_JSONPATH = "$.Employees[0].assignments.*.department";

	/** The Constant EMPLOYEE_DEPTCODE_JSONPATH. */
	public static final String EMPLOYEE_DEPTCODE_JSONPATH = "$.Employees.[0].assignments.[?(@.isCurrentAssignment == true)].department";

	/** The Constant EMPLOYEE_DESGCODE_JSONPATH. */
	public static final String EMPLOYEE_DESGCODE_JSONPATH = "$.Employees.[0].assignments.[?(@.isCurrentAssignment == true)].designation";

	/** The Constant EMPLOYEE_NAME_JSONPATH. */
	public static final String EMPLOYEE_NAME_JSONPATH = "$.Employees[0].user.name";

	/** The Constant EMPLOYEE_PHNO_JSONPATH. */
	public static final String EMPLOYEE_PHNO_JSONPATH = "$.Employees[0].user.mobileNumber";

	/** The Constant EMPLOYEE_TENANTID_JSONPATH. */
	public static final String EMPLOYEE_TENANTID_JSONPATH = "$.Employees[0].tenantId";

	/** The Constant EMPLOYEE_BASE_JSONPATH. */
	public static final String EMPLOYEE_BASE_JSONPATH = "$.Employees";

	/** The Constant DEPARTMENTNAME_EMPLOYEE_JSONPATH. */
	public static final String DEPARTMENTNAME_EMPLOYEE_JSONPATH = "$.Department[0].name";

	/** The Constant LOCALIZATION_CODES_JSONPATH. */
	public static final String LOCALIZATION_CODES_JSONPATH = "$.messages.*.code";

	/** The Constant LOCALIZATION_MSGS_JSONPATH. */
	public static final String LOCALIZATION_MSGS_JSONPATH = "$.messages.*.message";

	/** The Constant LOCATION__BOUNDARY_NAMES_JSONPATH. */
	public static final String LOCATION__BOUNDARY_NAMES_JSONPATH = "$.TenantBoundary.*.boundary.*.name";

	/** The Constant LOCATION__BOUNDARY_CODES_JSONPATH. */
	public static final String LOCATION__BOUNDARY_CODES_JSONPATH = "$.TenantBoundary.*.boundary.*.code";

	/** The Constant LOCATION__BOUNDARY_HIERARCHYTYPE_ADMIN. */
	public static final String LOCATION__BOUNDARY_HIERARCHYTYPE_ADMIN = "ADMIN";

	/** The Constant LOCATION__BOUNDARY_BOUNDARYTYPE_LOCALITY. */
	public static final String LOCATION__BOUNDARY_BOUNDARYTYPE_LOCALITY = "Locality";

	/** The Constant SEARCHER_RESPONSE_TEXT. */
	public static final String SEARCHER_RESPONSE_TEXT = "Searcher response : ";

	/** The Constant SMS_NOTIFICATION_STATUS_KEY. */
	public static final String SMS_NOTIFICATION_STATUS_KEY = "<status>";

	/** The Constant SMS_NOTIFICATION_COMPLAINT_TYPE_KEY. */
	public static final String SMS_NOTIFICATION_COMPLAINT_TYPE_KEY = "<complaint_type>";

	/** The Constant SMS_NOTIFICATION_DATE_KEY. */
	public static final String SMS_NOTIFICATION_DATE_KEY = "<date>";

	/** The Constant SMS_NOTIFICATION_ID_KEY. */
	public static final String SMS_NOTIFICATION_ID_KEY = "<id>";

	/** The Constant SMS_NOTIFICATION_EMP_NAME_KEY. */
	public static final String SMS_NOTIFICATION_EMP_NAME_KEY = "<emp_name>";

	/** The Constant SMS_NOTIFICATION_USER_NAME_KEY. */
	public static final String SMS_NOTIFICATION_USER_NAME_KEY = "<emp_name>";

	/** The Constant SMS_NOTIFICATION_EMP_DEPT_KEY. */
	public static final String SMS_NOTIFICATION_EMP_DEPT_KEY = "<emp_department>";

	/** The Constant SMS_NOTIFICATION_EMP_DESIGNATION_KEY. */
	public static final String SMS_NOTIFICATION_EMP_DESIGNATION_KEY = "<emp_designation>";

	/** The Constant SMS_NOTIFICATION_COMMENT_KEY. */
	public static final String SMS_NOTIFICATION_COMMENT_KEY = "<comment>";

	/** The Constant SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY. */
	public static final String SMS_NOTIFICATION_REASON_FOR_REOPEN_KEY = "<reason>";

	/** The Constant SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY. */
	public static final String SMS_NOTIFICATION_ADDITIONAL_COMMENT_KEY = "<additional_comments>";

	/** The Constant SMS_NOTIFICATION_APP_LINK_KEY. */
	public static final String SMS_NOTIFICATION_APP_LINK_KEY = "<app_link>";

	/** The Constant SMS_NOTIFICATION_APP_DOWNLOAD_LINK_KEY. */
	public static final String SMS_NOTIFICATION_APP_DOWNLOAD_LINK_KEY = "<download_link>";

	/** The Constant SMS_NOTIFICATION_AO_DESIGNATION. */
	public static final String SMS_NOTIFICATION_AO_DESIGNATION = "<ao_designation>";

	/** The Constant SMS_NOTIFICATION_ULB_NAME. */
	public static final String SMS_NOTIFICATION_ULB_NAME = "<ulb>";

	/** The Constant SMS_NOTIFICATION_SLA_NAME. */
	public static final String SMS_NOTIFICATION_SLA_NAME = "<sla>";

	/** The Constant SMS_NOTIFICATION_RATING_KEY. */
	public static final String SMS_NOTIFICATION_RATING_KEY = "<rating>";

	/** The Constant USREVENTS_EVENT_TYPE. */
	public static final String USREVENTS_EVENT_TYPE = "SYSTEMGENERATED";

	/** The Constant USREVENTS_EVENT_NAME. */
	public static final String USREVENTS_EVENT_NAME = "Public Grievance Redressal";

	/** The Constant USREVENTS_EVENT_POSTEDBY. */
	public static final String USREVENTS_EVENT_POSTEDBY = "SYSTEM-PGR";

	/* search on roles constant */

	/** The Constant ROLE_EMPLOYEE. */
	public static final String ROLE_EMPLOYEE = "EMPLOYEE";

	/** The Constant ROLE_CITIZEN. */
	public static final String ROLE_CITIZEN = "CITIZEN";

	/** The Constant ROLE_GRO. */
	public static final String ROLE_GRO = "GRO";

	/** The Constant ROLE_DGRO. */
	public static final String ROLE_DGRO = "DGRO";

	/** The Constant ROLE_CSR. */
	public static final String ROLE_CSR = "CSR";

	/** The Constant ROLE_NAME_CITIZEN. */
	public static final String ROLE_NAME_CITIZEN = "Citizen";

	/** The Constant ROLE_NAME_EMPLOYEE. */
	public static final String ROLE_NAME_EMPLOYEE = "Employee";

	/** The Constant ROLE_NAME_GRO. */
	public static final String ROLE_NAME_GRO = "Grievance Routing Officer";

	/** The Constant ROLE_NAME_DGRO. */
	public static final String ROLE_NAME_DGRO = "Department Grievance Routing Officer";

	/** The Constant ROLE_NAME_CSR. */
	public static final String ROLE_NAME_CSR = "Customer Support Representative";

	/** The status notif key map. */
	private static Map<String, String> statusNotifKeyMap = prepareStatusNotifKeyMap();

	/** The action notif key map. */
	private static Map<String, String> actionNotifKeyMap = prepareActionNotifKeyMap();

	/** The status role localization key map. */
	private static Map<String, String> statusRoleLocalizationKeyMap = prepareStatusRoleLocalizationKeyMap();

	/** The action role localization key map. */
	private static Map<String, String> actionRoleLocalizationKeyMap = prepareActionRoleLocalizationKeyMap();

	/**
	 * Prepare status notif key map.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareStatusNotifKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.STATUS_OPENED, "submitted");
		map.put(WorkFlowConfigs.STATUS_ASSIGNED, "assigned");
		map.put(WorkFlowConfigs.STATUS_REJECTED, "rejected");
		map.put(WorkFlowConfigs.STATUS_RESOLVED, "resolved");

		return map;
	}

	/**
	 * Prepare action notif key map.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareActionNotifKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.ACTION_REOPEN, "reopened");
		map.put(WorkFlowConfigs.ACTION_REASSIGN, "reassigned");

		return map;
	}

	/**
	 * Mapping between which messages are to be sent to which actor and on what
	 * status.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareStatusRoleLocalizationKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.STATUS_OPENED + "|" + BookingsConstants.ROLE_CITIZEN, LOCALIZATION_CODE_SUBMIT_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_ASSIGNED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_ASSIGN_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_ASSIGNED + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_ASSIGN_EMPLOYEE);
		map.put(WorkFlowConfigs.STATUS_REJECTED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_REJECT_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_RESOLVED + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_RESOLVE_CITIZEN);
		map.put(WorkFlowConfigs.STATUS_CLOSED + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_CLOSE_EMPLOYEE);

		return map;
	}

	/**
	 * Mapping between which messages are to be sent to which actor and on what
	 * action.
	 *
	 * @return the map
	 */
	private static Map<String, String> prepareActionRoleLocalizationKeyMap() {

		Map<String, String> map = new HashMap<>();
		map.put(WorkFlowConfigs.ACTION_REOPEN + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_REOPEN_EMPLOYEE);
		map.put(WorkFlowConfigs.ACTION_REOPEN + "|" + BookingsConstants.ROLE_CITIZEN, LOCALIZATION_CODE_REOPEN_CITIZEN);
		map.put(WorkFlowConfigs.ACTION_REASSIGN + "|" + BookingsConstants.ROLE_CITIZEN,
				LOCALIZATION_CODE_REASSIGN_CITIZEN);
		map.put(WorkFlowConfigs.ACTION_REASSIGN + "|" + BookingsConstants.ROLE_EMPLOYEE,
				LOCALIZATION_CODE_REASSIGN_EMPLOYEE);

		return map;
	}

	/**
	 * Gets the status notif key map.
	 *
	 * @return the status notif key map
	 */
	public static Map<String, String> getStatusNotifKeyMap() {
		return statusNotifKeyMap;
	}

	/**
	 * Gets the action notif key map.
	 *
	 * @return the action notif key map
	 */
	public static Map<String, String> getActionNotifKeyMap() {
		return actionNotifKeyMap;
	}

	/**
	 * Gets the status role localization key map.
	 *
	 * @return the status role localization key map
	 */
	public static Map<String, String> getStatusRoleLocalizationKeyMap() {
		return statusRoleLocalizationKeyMap;
	}

	/**
	 * Gets the action role localization key map.
	 *
	 * @return the action role localization key map
	 */
	public static Map<String, String> getActionRoleLocalizationKeyMap() {
		return actionRoleLocalizationKeyMap;
	}

	/** The Constant TENANT_ID. */
	public static final String TENANT_ID = "tenantId";

	/** The Constant ACTION. */
	public static final String ACTION = "action";
	
	/** The Constant ACTION_INITIATE. */
	public static final String ACTION_INITIATE = "INITIATE";

	/** The Constant BUSINESS_SERVICE. */
	public static final String BUSINESS_SERVICE = "businessservice";

	/** The Constant ROLE_TENANT_ID. */
	public static final String ROLE_TENANT_ID = "role_tenantId";

	/** The Constant UUID. */
	public static final String UUID = "uuid";

	/** The Constant APPLICATION_NUMBER. */
	public static final String APPLICATION_NUMBER = "applicationNumber";

	/** The Constant BOOKING_VENUE. */
	public static final String BOOKING_VENUE = "bookingVenue";

	/** The Constant BOOKING_TYPE. */
	public static final String BOOKING_TYPE = "bookingType";

	/** The Constant DATE. */
	public static final String DATE = "date";

	/** The Constant APPLY. */
	public static final String APPLY = "APPLY";
	
	/** The Constant APPROVE. */
	public static final String APPROVE = "APPROVE";

	/** The Constant PAY. */
	public static final String PAY = "PAY";

	/** The Constant BUSINESS_SERVICE_OSBM. */
	public static final String BUSINESS_SERVICE_OSBM = "OSBM";

	/** The Constant BUSINESS_SERVICE_BWT. */
	public static final String BUSINESS_SERVICE_BWT = "BWT";

	/** The Constant BUSINESS_SERVICE_GFCP. */
	public static final String BUSINESS_SERVICE_GFCP = "GFCP";

	/** The Constant BUSINESS_SERVICE_OSUJM. */
	public static final String BUSINESS_SERVICE_OSUJM = "OSUJM";

	/** The Constant BUSINESS_SERVICE_NLUJM. */
	public static final String BUSINESS_SERVICE_NLUJM = "NLUJM";
	
	/** The Constant BUSINESS_SERVICE_PACC. */
	public static final String BUSINESS_SERVICE_PACC = "PACC";
	
	/** The Constant BUSINESS_SERVICE_BKROOM. */
	public static final String BUSINESS_SERVICE_BKROOM = "BKROOM";
	
	/** The Constant BUSINESS_SERVICE_ROOM_FOR_COMMUNITY. */
	public static final String BUSINESS_SERVICE_ROOM_FOR_COMMUNITY = "BKROOM";

	/** The Constant EMPLOYEE. */
	public static final String TYPE = "type";

	/** The Constant ROLES. */
	public static final String ROLES = "roles";

	/** The Constant CITIZEN. */
	public static final String CITIZEN = "CITIZEN";

	/** The Constant EMPLOYEE. */
	public static final String EMPLOYEE = "EMPLOYEE";

	/** The Constant OSBM_APPROVER. */
	public static final String OSBM_APPROVER = "BK_OSBM_APPROVER";

	/** The Constant MCC_HELPDESK_USER. */
	public static final String MCC_HELPDESK_USER = "BK_MCC_HELPDESK_USER";

	/** The Constant COMMERCIAL_GROUND_VIEWER. */
	public static final String COMMERCIAL_GROUND_VIEWER = "BK_COMMERCIAL_GROUND_VIEWER";

	/** The Constant GROUND_FOR_COMMERCIAL_PURPOSE. */
	public static final String GROUND_FOR_COMMERCIAL_PURPOSE = "GROUND_FOR_COMMERCIAL_PURPOSE";

	/** The Constant AREA. */
	public static final String AREA = "area";

	/** The Constant SECTOR. */
	public static final String SECTOR = "sector";

	/** The Constant BK_SECTOR. */
	public static final String BK_SECTOR = "bkSector";

	/** The Constant MCC_APPROVER. */
	public static final String MCC_APPROVER = "BK_MCC_APPROVER";

	/** The Constant ACTION_SPECIAL_APPLY. */
	public static final String ACTION_DELIVER = "DELIVER";

	/** The Constant ACTION_FAILURE_APPLY. */
	public static final String ACTION_FAILURE_APPLY = "FAILUREAPPLY";

	/** The Constant OSD_APPROVER. */
	public static final String OSD_APPROVER = "BK_OSD_APPROVER";

	/** The Constant ADMIN_APPROVER. */
	public static final String ADMIN_APPROVER = "BK_ADMIN_APPROVER";
	
	/** The Constant NLOSUJM. */
	public static final String NLOSUJM = "New Location Open Spaces Under Jurisdiction of MCC";

	/** The Constant STATE. */
	public static final String STATE = "state";
	
	/** The Constant APPROVER. */
	public static final String APPROVER = "approver";
	
	/** The Constant PARKS_AND_COMMUNITY_VIEWER. */
	public static final String PARKS_AND_COMMUNITY_VIEWER = "BK_PARKS_AND_COMMUNITY_VIEWER";
	
	
	
	// ACTION_STATUS combinations for notification
	/** The Constant ACTION_STATUS_INITIATED. */
	//OSBM,OSUJM
    public static final String ACTION_STATUS_INITIATED = "INITIATE_INITIATED";
    
    /** The Constant ACTION_STATUS_PENDINGAPPROVAL. */
    public static final String ACTION_STATUS_PENDINGAPPROVAL = "APPLY_PENDINGAPPROVAL";
    
    /** The Constant ACTION_STATUS_PENDINGPAYMENT. */
    public static final String ACTION_STATUS_PENDINGPAYMENT = "APPROVE_PENDINGPAYMENT";
    
    /** The Constant ACTION_STATUS_REJECTED. */
    public static final String ACTION_STATUS_REJECTED  = "REJECT_REJECTED";
    
    /** The Constant ACTION_STATUS_APPROVED. */
    public static final String ACTION_STATUS_APPROVED = "PAY_APPROVED";
    
    /** The Constant ACTION_STATUS_SPECIALAPPLY_DELIVERED. */
    //BWT
    public static final String ACTION_STATUS_SPECIALAPPLY_DELIVERED = "SPECIALAPPLY_PENDINGASSIGNMENTDRIVER";
    
    /** The Constant ACTION_STATUS_PAIDAPPLY_PENDINGASSIGNMENTDRIVER. */
    public static final String ACTION_STATUS_PAIDAPPLY_PENDINGASSIGNMENTDRIVER = "PAIDAPPLY_PENDINGASSIGNMENTDRIVER";
    
    /** The Constant ACTION_STATUS_FAILUREAPPLY_PENDINGASSIGNMENTDRIVER. */
    public static final String ACTION_STATUS_FAILUREAPPLY_PENDINGASSIGNMENTDRIVER = "FAILUREAPPLY_PENDINGASSIGNMENTDRIVER";
    
    /** The Constant ACTION_STATUS_PENDINGUPDATE. */
    public static final String ACTION_STATUS_PENDINGUPDATE = "ASSIGNDRIVER_PENDINGUPDATE";
    
    /** The Constant ACTION_STATUS_DELIVERED. */
    public static final String ACTION_STATUS_DELIVERED = "DELIVER_DELIVERED";
    
    /** The Constant ACTION_STATUS_NOTDELIVERED. */
    public static final String ACTION_STATUS_NOTDELIVERED = "NOTDELIVER_NOTDELIVERED";
    
    /** The Constant ACTION_STATUS_APPLIED. */
    //GFCP
    public static final String ACTION_STATUS_APPLIED  = "APPLY_APPLIED";
    
    /** The Constant ACTION_STATUS_PENDINGAPPROVALOSD. */
    //NLUJM
    public static final String ACTION_STATUS_PENDINGAPPROVALOSD = "APPROVE_PENDINGAPPROVALOSD";
    
    /** The Constant ACTION_STATUS_PENDINGPUBLISH. */
    public static final String ACTION_STATUS_PENDINGPUBLISH = "APPROVEOSD_PENDINGPUBLISH";
    
    /** The Constant ACTION_STATUS_PUBLISH. */
    public static final String ACTION_STATUS_PUBLISHED = "PUBLISH_PUBLISHED";
	//
	
	/** The Constant NOTIFICATION_INITIATED. */
	public static final String NOTIFICATION_INITIATED = "bk.en.counter.initiate";
	
	/** The Constant NOTIFICATION_ROOM_INITIATED. */
	public static final String NOTIFICATION_ROOM_INITIATED = "bk.en.counter.room.initiate";
	
	/** The Constant NOTIFICATION_PENDINGAPPROVAL. */
	public static final String NOTIFICATION_PENDINGAPPROVAL = "bk.en.counter.apply";
	
	/** The Constant NOTIFICATION_PENDINGPAYMENT. */
	public static final String NOTIFICATION_PENDINGPAYMENT = "bk.en.counter.approved";
	
	/** The Constant NOTIFICATION_REJECTED. */
	public static final String NOTIFICATION_REJECTED = "bk.en.counter.rejected";
	
	/** The Constant NOTIFICATION_APPROVED. */
	public static final String NOTIFICATION_APPROVED = "bk.en.counter.pay";
	
	/** The Constant NOTIFICATION_PENDINGASSIGNMENTDRIVER. */
	public static final String NOTIFICATION_PENDINGASSIGNMENTDRIVER = "bk.en.counter.paidapply";
	
	/** The Constant NOTIFICATION_PENDINGUPDATE. */
	public static final String NOTIFICATION_PENDINGUPDATE = "bk.en.counter.assigndriver";
	
	/** The Constant NOTIFICATION_DELIVERED. */
	public static final String NOTIFICATION_DELIVERED = "BK_WF_BWT_DELIVERED";
	
	/** The Constant NOTIFICATION_NOTDELIVERED. */
	public static final String NOTIFICATION_NOTDELIVERED = "BK_WF_BWT_NOTDELIVERED";
	
	/** The Constant NOTIFICATION_APPLIED. */
	public static final String NOTIFICATION_APPLIED = "bk.en.counter.apply";
	
	/** The Constant NOTIFICATION_ROOM_APPLIED. */
	public static final String NOTIFICATION_ROOM_APPLIED = "bk.en.counter.room.apply";
	
	/** The Constant NOTIFICATION_PACC_ROOM_APPLIED. */
	public static final String NOTIFICATION_PACC_ROOM_APPLIED = "bk.en.counter.pacc.room.apply";
    
    /** The Constant NOTIFICATION_OBJECT_MODIFIED. */
    public static final String NOTIFICATION_OBJECT_MODIFIED = "tl.en.edit.object.modified";
    
    /** The Constant DEFAULT_OBJECT_MODIFIED_MSG. */
    public static final String DEFAULT_OBJECT_MODIFIED_MSG = "Dear <1>,Your Trade License with application number <APPLICATION_NUMBER> was modified.";
    
    /** The Constant EMAIL_SUBJECT. */
    public static  final String EMAIL_SUBJECT = "Chandigarh mSeva Booking Application Status";
    
    /** The Constant BILL_AMOUNT_JSONPATH. */
    public static final String BILL_AMOUNT_JSONPATH = "$.billResponse.Bill[0].totalAmount";
    
    /** The Constant TRADE_LICENSE_MODULE_CODE. */
    public static final String TRADE_LICENSE_MODULE_CODE = "BK";
    
    /** The Constant NOTIFICATION_LOCALE. */
    public static final String NOTIFICATION_LOCALE = "en_IN";
    
    /** The Constant MODULE. */
    public static final String MODULE = "rainmaker-services";
    
    /** The Constant INITIATED. */
    public static final String INITIATED = "INITIATED";
    
    /** The Constant PARKS. */
    public static final String PARKS = "Parks";
    
    /** The Constant COMMUNITY_CENTER. */
    public static final String COMMUNITY_CENTER = "Community Center";
    
    /** The Constant DEO. */
    public static final String DEO = "BK_DEO";
    
    /** The Constant CLERK. */
    public static final String CLERK = "BK_CLERK";
    
    /** The Constant SENIOR_ASSISTANT. */
    public static final String SENIOR_ASSISTANT = "BK_SENIOR_ASSISTANT";
    
    /** The Constant AUDIT_DEPARTMENT. */
    public static final String AUDIT_DEPARTMENT = "BK_AUDIT_DEPARTMENT";
    
    /** The Constant CHIEF_ACCOUNT_OFFICER. */
    public static final String CHIEF_ACCOUNT_OFFICER = "BK_CHIEF_ACCOUNT_OFFICER";
    
    /** The Constant PAYMENT_PROCESSING_AUTHORITY. */
    public static final String PAYMENT_PROCESSING_AUTHORITY = "BK_PAYMENT_PROCESSING_AUTHORITY";
    
    /** The Constant E_SAMPARK_CENTER. */
    public static final String E_SAMPARK_CENTER = "BK_E-SAMPARK-CENTER";
    
    /** The Constant MCC_USER. */
    public static final String MCC_USER = "BK_MCC_USER";
    
    /** The Constant NLUJM_BOOKING_TYPE. */
    public static final String NLUJM_BOOKING_TYPE = "New Location Open Spaces Under Jurisdiction of MCC";
    
    /** The Constant NOTIFICATION_UPDATE. */
    public static final String NOTIFICATION_UPDATE = "bk.en.counter.update";
    
    /** The Constant BK_WF_NLUJM. */
    public static final String BK_WF_NLUJM = "BK_WF_NLUJM_";
    
    /** The Constant RPT_BK_WF. */
    public static final String RPT_BK_WF = "RPT_BK_WF_";
    
    /** The Constant DATE_FORMAT. */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /** The Constant TIME_ZONE. */
    public static final String TIME_ZONE = "Asia/Kolkata";
    
    /** The Constant MDMS_MODULE_NAME. */
    public static final String MDMS_MODULE_NAME = "ACCESSCONTROL-ROLES";
    
    /** The Constant MDMS_FILE_NAME. */
    public static final String MDMS_FILE_NAME = "roles";

	/** The Constant PAYMENT_SUCCESS_STATUS. */
	public static final String PAYMENT_SUCCESS_STATUS = "SUCCESS";
	
	/** The Constant PACC_TAXHEAD_CODE_3. */
	public static final String PACC_TAXHEAD_CODE_3 = "PARK_LOCATION_AND_VENUE_CHANGE_AMOUNT";
	
	/** The Constant PACC_TAXHEAD_CODE_3. */
	public static final String PACC_TAXHEAD_CODE_COMM_LOC_CHANGE = "COMMUNITY_LOCATION_AND_VENUE_CHANGE_AMOUNT";
	
	/** The Constant DELIVER. */
	public static final String DELIVER = "DELIVER";
	
	/** The Constant PACC_RE_INITIATED_ACTION. */
	public static final String PACC_RE_INITIATED_ACTION = "RE_INITIATE";

	/** The Constant BK_WATER_TANKER_DELIVER_ACTION_KEY. */
	public static final String BK_WATER_TANKER_DELIVER_ACTION_KEY = "BK_WATER_TANKER_DELIVER_ACTION";

	/** The Constant BOOKING_MDMS_MODULE_NAME. */
	public static final String BOOKING_MDMS_MODULE_NAME = "Booking";
	
	/** The Constant PACC_TAXHEAD_CODE_LUXURY_TAX. */
	public static final String PACC_TAXHEAD_CODE_LUXURY_TAX = "LUXURY_TAX";
	
	/** The Constant PACC_TAXHEAD_CODE_REFUNDABLE_SECURITY_AMOUNT. */
	public static final String PACC_TAXHEAD_CODE_REFUNDABLE_SECURITY_AMOUNT = "REFUNDABLE_SECURITY";
	
	/** The Constant PACC_TAXHEAD_CODE_FACILITATION_CHARGE. */
	public static final String PACC_TAXHEAD_CODE_FACILITATION_CHARGE = "FACILITATION_CHARGE";
	
	/** The Constant PACC_TAXHEAD_CODE_PACC_TAX. */
	public static final String PACC_TAXHEAD_CODE_PACC_TAX = "PACC_TAX";
	
	/** The Constant PACC_TAXHEAD_CODE_PACC. */
	public static final String PACC_TAXHEAD_CODE_PACC = "PACC";
	
	/** The Constant OFFLINE_APPLY. */
	public static final String OFFLINE_APPLY = "OFFLINE_APPLY";

	/** The Constant BOOKING_MDMS_FILE_NAME. */
	public static final String BOOKING_MDMS_FILE_NAME = "Booking_Config";
	
	/** The Constant MAIL_NOTIFICATION_INITIATED. */
	public static final String MAIL_NOTIFICATION_INITIATED = "bk.m.counter.initiate";
	
	/** The Constant MAIL_NOTIFICATION_ROOM_INITIATED. */
	public static final String MAIL_NOTIFICATION_ROOM_INITIATED = "bk.m.counter.room.initiate";
	
	/** The Constant MAIL_NOTIFICATION_PENDINGAPPROVAL. */
	public static final String MAIL_NOTIFICATION_PENDINGAPPROVAL = "bk.m.counter.apply";
	
	/** The Constant MAIL_NOTIFICATION_PENDINGPAYMENT. */
	public static final String MAIL_NOTIFICATION_PENDINGPAYMENT = "bk.m.counter.approved";
	
	/** The Constant MAIL_NOTIFICATION_REJECTED. */
	public static final String MAIL_NOTIFICATION_REJECTED = "bk.m.counter.rejected";
	
	/** The Constant MAIL_NOTIFICATION_APPROVED. */
	public static final String MAIL_NOTIFICATION_APPROVED = "bk.m.counter.pay";
	
	/** The Constant MAIL_NOTIFICATION_PENDINGASSIGNMENTDRIVER. */
	public static final String MAIL_NOTIFICATION_PENDINGASSIGNMENTDRIVER = "bk.m.counter.paidapply";
	
	/** The Constant MAIL_NOTIFICATION_PENDINGUPDATE. */
	public static final String MAIL_NOTIFICATION_PENDINGUPDATE = "bk.m.counter.assigndriver";
	
	/** The Constant MAIL_NOTIFICATION_APPLIED. */
	public static final String MAIL_NOTIFICATION_APPLIED = "bk.m.counter.apply";
	
	/** The Constant MAIL_NOTIFICATION_ROOM_APPLIED. */
	public static final String MAIL_NOTIFICATION_ROOM_APPLIED = "bk.m.counter.room.apply";
	
	/** The Constant MAIL_NOTIFICATION_PACC_ROOM_APPLIED. */
	public static final String MAIL_NOTIFICATION_PACC_ROOM_APPLIED = "bk.m.counter.pacc.room.apply";
	
	/** The Constant MAIL_NOTIFICATION_UPDATE. */
	public static final String MAIL_NOTIFICATION_UPDATE = "bk.m.counter.update";
	
	/** The Constant NOTIFICATION_DRIVER. */
	public static final String NOTIFICATION_DRIVER = "bk.en.counter.driver";
	
	/** The Constant MIME_TYPE. */
	public static final String MIME_TYPE = "application/pdf";
	
	/** The Constant PAYMENT_RECEIPT_NAME. */
	public static final String PAYMENT_RECEIPT_NAME = "PaymentReceipt.pdf";
	
	/** The Constant PERMISSION_LETTER_NAME. */
	public static final String PERMISSION_LETTER_NAME = "PermissionLetter.pdf";
	
	/** The Constant PAYMENT_RECEIPT. */
	public static final String PAYMENT_RECEIPT = "paymentReceipt";
	
	/** The Constant PERMISSION_LETTER. */
	public static final String PERMISSION_LETTER = "permissionLetter";
	
	/** The Constant HOUSE_NO. */
	public static final String HOUSE_NO = "House No.";
	
	/** The Constant DAYS_FIFTEEN. */
	public static final int DAYS_FIFTEEN = 15;
    
	/** The Constant BOOKING_ROLES. */
	public static final String BOOKING_ROLES = "BookingRoles";
	
	/** The Constant AND. */
	public static final String AND = "and";
	
	/** The Constant HOURS. */
	public static final int HOURS = 18;
	
	/** The Constant MINUTES. */
	public static final int MINUTES = 29;
	
	/** The Constant SECONDS. */
	public static final int SECONDS = 59;
	
	/** The Constant PARKS_AND_COMMUNITY_CENTER. */
	public static final String PARKS_AND_COMMUNITY_CENTER = "Parks and Community Center";
	
	/** The Constant PACC_ACTION_CANCEL. */
	public static final String PACC_ACTION_CANCEL = "CANCEL";
	
	/** The Constant SUPERVISOR. */
	public static final String SUPERVISOR = "BK_SUPERVISOR";
	
	/** The Constant OSD. */
	public static final String OSD = "BK_OSD";
	
	/** The Constant ACTION_STATUS_RE_INITIATED. */
	public static final String ACTION_STATUS_RE_INITIATED  = "RE_INITIATE_RE_INITIATED";
	
	/** The Constant ACTION_STATUS_CANCEL. */
	public static final String ACTION_STATUS_CANCEL  = "CANCEL_PENDING_FOR_APPROVAL_CLEARK_DEO";
	
	/** The Constant ACTION_STATUS_SECURITY_REFUND. */
	public static final String ACTION_STATUS_SECURITY_REFUND  = "SECURITY_REFUND_PENDING_FOR_APPROVAL_SUPERVISOR";
	
	/** The Constant ACTION_STATUS_MODIFY. */
	public static final String ACTION_STATUS_MODIFY  = "MODIFY_MODIFIED";
	
	/** The Constant ACTION_STATUS_APPROVE_CLERK_DEO. */
	public static final String ACTION_STATUS_APPROVE_CLERK_DEO  = "APPROVE_CLERK_DEO_PENDING_FOR_APPROVAL_SENIOR_ASSISTANT";
	
	/** The Constant ACTION_STATUS_APPROVE_SENIOR_ASSISTANT. */
	public static final String ACTION_STATUS_APPROVE_SENIOR_ASSISTANT  = "APPROVE_SENIOR_ASSISTANT_PENDING_FOR_APPROVAL_AUDIT_DEPARTMENT";
	
	/** The Constant ACTION_STATUS_APPROVE_AUDIT_DEPARTMENT. */
	public static final String ACTION_STATUS_APPROVE_AUDIT_DEPARTMENT  = "APPROVE_AUDIT_DEPARTMENT_PENDING_FOR_APPROVAL_CAO";
	
	/** The Constant ACTION_STATUS_APPROVE_CHIEF_ACCOUNT_OFFICER. */
	public static final String ACTION_STATUS_APPROVE_CHIEF_ACCOUNT_OFFICER  = "APPROVE_CHIEF_ACCOUNT_OFFICER_PENDING_FOR_DISBURSEMENT";
	
	/** The Constant ACTION_STATUS_PAY. */
	public static final String ACTION_STATUS_PAY  = "PAY_PAYMENT_DISBURSED";
	
	/** The Constant ACTION_STATUS_OFFLINE_INITIATE. */
	public static final String ACTION_STATUS_OFFLINE_INITIATE  = "OFFLINE_INITIATE_OFFLINE_INITIATED";
	
	/** The Constant ACTION_STATUS_OFFLINE_APPLY. */
	public static final String ACTION_STATUS_OFFLINE_APPLY  = "OFFLINE_APPLY_OFFLINE_APPLIED";
	
	/** The Constant ACTION_STATUS_OFFLINE_RE_INITIATE. */
	public static final String ACTION_STATUS_OFFLINE_RE_INITIATE  = "OFFLINE_RE_INITIATE_OFFLINE_RE_INITIATED";
	
	/** The Constant ACTION_STATUS_OFFLINE_CANCEL. */
	public static final String ACTION_STATUS_OFFLINE_CANCEL  = "OFFLINE_CANCEL_PENDING_FOR_DISBURSEMENT";
	
	/** The Constant ACTION_STATUS_OFFLINE_SECURITY_REFUND. */
	public static final String ACTION_STATUS_OFFLINE_SECURITY_REFUND  = "OFFLINE_SECURITY_REFUND_PENDING_FOR_APPROVAL_SUPERVISOR";
	
	/** The Constant ACTION_STATUS_OFFLINE_MODIFY. */
	public static final String ACTION_STATUS_OFFLINE_MODIFY  = "OFFLINE_MODIFY_OFFLINE_MODIFIED";
	
	/** The Constant ACTION_STATUS_APPROVE_SUPERVISOR. */
	public static final String ACTION_STATUS_APPROVE_SUPERVISOR  = "APPROVE_SUPERVISOR_PENDING_FOR_APPROVAL_OSD";
	
	/** The Constant ACTION_STATUS_APPROVE_OSD. */
	public static final String ACTION_STATUS_APPROVE_OSD  = "APPROVE_OSD_PENDING_FOR_APPROVAL_AUDIT_DEPARTMENT";
	
	/** The Constant NOTIFICATION_RE_INITIATED. */
	public static final String NOTIFICATION_RE_INITIATED = "bk.en.counter.re_initiate";
	
	/** The Constant NOTIFICATION_PACC_RE_INITIATED. */
	public static final String NOTIFICATION_PACC_RE_INITIATED = "bk.en.counter.pacc.re_initiate";
	
	/** The Constant NOTIFICATION_CANCEL. */
	public static final String NOTIFICATION_CANCEL = "bk.en.counter.cancel";
	
	/** The Constant NOTIFICATION_PACC_ROOM_CANCEL. */
	public static final String NOTIFICATION_PACC_ROOM_CANCEL = "bk.en.counter.pacc.room.cancel";
	
	/** The Constant NOTIFICATION_SECURITY_REFUND. */
	public static final String NOTIFICATION_SECURITY_REFUND = "bk.en.counter.security_refund";
	
	/** The Constant NOTIFICATION_MODIFY. */
	public static final String NOTIFICATION_MODIFY = "bk.en.counter.modify";
	
	/** The Constant NOTIFICATION_DISBURSED. */
	public static final String NOTIFICATION_DISBURSED = "bk.en.counter.disbursed";
	
	/** The Constant ACTION_STATUS_PACC_REJECTED. */
	public static final String ACTION_STATUS_PACC_REJECTED = "PACC_REJECT_REJECTED";
	
	/** The Constant NOTIFICATION_PACC_REJECTED. */
	public static final String NOTIFICATION_PACC_REJECTED = "bk.en.counter.pacc_reject";
	
	/** The Constant MAIL_NOTIFICATION_RE_INITIATED. */
	public static final String MAIL_NOTIFICATION_RE_INITIATED = "bk.m.counter.re_initiate";
	
	/** The Constant MAIL_NOTIFICATION_PACC_RE_INITIATED. */
	public static final String MAIL_NOTIFICATION_PACC_RE_INITIATED = "bk.m.counter.pacc.room.re_initiate";
	
	/** The Constant MAIL_NOTIFICATION_CANCEL. */
	public static final String MAIL_NOTIFICATION_CANCEL = "bk.m.counter.cancel";
	
	/** The Constant MAIL_NOTIFICATION_PACC_ROOM_CANCEL. */
	public static final String MAIL_NOTIFICATION_PACC_ROOM_CANCEL = "bk.m.counter.pacc.room.cancel";
	
	/** The Constant MAIL_NOTIFICATION_SECURITY_REFUND. */
	public static final String MAIL_NOTIFICATION_SECURITY_REFUND = "bk.m.counter.security_refund";
	
	/** The Constant MAIL_NOTIFICATION_MODIFY. */
	public static final String MAIL_NOTIFICATION_MODIFY = "bk.m.counter.modify";
	
	/** The Constant MAIL_NOTIFICATION_DISBURSED. */
	public static final String MAIL_NOTIFICATION_DISBURSED = "bk.m.counter.disbursed";
	
	/** The Constant MAIL_NOTIFICATION_PACC_REJECTED. */
	public static final String MAIL_NOTIFICATION_PACC_REJECTED = "bk.m.counter.pacc_reject";
	
	/** The Constant NOTIFICATION_APPROVER_MSG. */
	public static final String NOTIFICATION_APPROVER_MSG = "bk.en.counter.approver_msg";
	
	/** The Constant MAIL_NOTIFICATION_APPROVER_MSG. */
	public static final String MAIL_NOTIFICATION_APPROVER_MSG = "bk.m.counter.approver_msg";
	
	/** The Constant PACC_ACTION_MODIFY. */
	public static final String PACC_ACTION_MODIFY = "MODIFY";
	
	/** The Constant EMPLOYEE_TENANT_ID. */
	public static final String EMPLOYEE_TENANT_ID = "ch.chandigarh";
	
	
	public static final boolean ISLOCKED = true;
	
	public static final String AC = "AC";
	
	public static final String NON_AC = "NON-AC";
	
	public static final String TOTAL_AC_ROOMS = "totalAcRooms";
	
	public static final String TOTAL_NON_AC_ROOMS = "totalNonAcRooms";
	
	public static final String BOOKED_AC_ROOMS = "bookedAcRooms";
	
	public static final String BOOKED_NON_AC_ROOMS = "bookedNonAcRooms";
	
	public static final String AVAILABLE_AC_ROOMS = "availableAcRooms";
	
	public static final String AVAILABLE_NON_AC_ROOMS = "availableNonAcRooms";
	
	public static final String ROOM_TAXHEAD_CODE_FACILITATION_CHARGE = "ROOM_FACILITATION_CHARGE";
	
	public static final String SIX_MONTHS_FROM_NOW_SQL = "sixMonthsFromNowSql";
	
	public static final String VENUE_TYPE_COMMUNITY_CENTER = "Community Center";
	
	public static final String VENUE_TYPE_PARKS = "Parks";
	
	public static final String VENUE_TYPE_COMMERCIAL = "Commercial Ground";
	
	public static final String CANCEL = "CANCEL";
	
	public static final String PACC_ACTION_OFFLINE_MODIFY = "OFFLINE_MODIFY";
	
	public static final String PACC_ACTION_APPROVE_CLERK_DEO = "APPROVE_CLERK_DEO";
	
	public static final int TEN_MINUTES = 10;
}