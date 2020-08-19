package org.egov.nulm.repository.builder;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NULMQueryBuilder {
	public static final String GET_SEP_APPLICATION_QUERY = "SELECT  NA.application_uuid,  NA.remark, NA.application_id,  NA.nulm_application_id,  NA.application_status, \n" + 
			"        NA.name,  NA.gender,  NA.age,  NA.dob,  NA.adhar_no,  NA.mother_name,  NA.father_or_husband_name, \n" + 
			"        NA.occupation,  NA.address,  NA.contact,  NA.since_how_long_in_chandigarh,  NA.qualification, \n" + 
			"        NA.category,  NA.is_urban_poor,  NA.is_minority,  NA.is_handicapped,  NA.is_loan_from_bankinginstitute, \n" + 
			"        NA.is_repayment_made,  NA.bpl_no,  NA.minority,  NA.type_of_business_to_be_started, \n" + 
			"        NA.previous_experience,  NA.place_of_work,  NA.bank_details,  NA.no_of_family_members, \n" + 
			"        NA.project_cost,  NA.loan_amount,  NA.recommended_amount,  NA.recommended_by, \n" + 
			"        NA.representative_name,  NA.representative_address,  NA.tenant_id,NA.is_active,NA.created_by ,NA.created_time,NA.last_modified_by, NA.last_modified_time,\n" + 
			"        array_to_json(array_agg(json_build_object('documentType',ND.document_type,'filestoreId',ND.filestore_id,'documnetUuid',ND.document_uuid,'isActive',ND.is_active,\n" + 
			"        'tenantId',ND.tenant_id,'applicationUuid',ND.application_uuid) ))as document \n" + 
			"  FROM public.nulm_sep_application_detail NA inner  join nulm_sep_application_document ND on NA.application_uuid=ND.application_uuid and NA.tenant_id=ND.tenant_id\n" + 
			"  where NA.application_id=(case when ?  <>'' then ?  else NA.application_id end) and NA.created_by=(case when ?  <>'' then ?  else NA.created_by end) AND NA.tenant_id=? "
			+ "AND NA.application_status=(case when ?  <>'' then ?  else NA.application_status end) \n" + 
			"  AND NA.is_active='true' AND ND.is_active='true' AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE \n" + 
			" TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END \n" + 
			"AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE \n" + 
			" TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END AND  NA.application_status<>?  group by NA.application_uuid    ORDER BY created_time desc";
	
	 public static final String GET_SEP_DOCUMENT_QUERY="SELECT count(*) \n" + 
	 		"        FROM public.nulm_sep_application_document \n" + 
	 		"        WHERE application_uuid=? and tenant_id=? and filestore_id=? and document_type=? and is_active='true'; ";
	 
	 public static final String GET_SMID_APPLICATION_QUERY="select * FROM nulm_smid_application_detail where application_uuid=(case when ?  <>'' then ?  else application_uuid end) and created_by=(case when ?  <>'' then ?  else created_by end) \n" + 
	 		"AND tenant_id=? AND application_status=(case when ?  <>'' then ?  else application_status end) AND is_active='true' AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE \n" + 
	 		" TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END \n" + 
	 		"AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE \n" + 
	 		" TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END  AND application_status<>?  ORDER BY created_time desc";
	 
	 public static final String SHG_UUID_EXIST_QUERY="select count(*) from nulm_smid_shg_detail where shg_uuid=? and tenant_id=? and is_active='true'";
	 
	 public static final String GET_SHG_QUERY="select GP.shg_uuid,GP.shg_id,GP.name,GP.type,GP.groups_nominated_by,GP.formed_through,GP.status,GP.address,GP.contact_no,GP.date_of_formation,GP.account_no,GP.date_of_opening_account,GP.bank_name,\n" + 
	 		"GP.branch_name,GP.main_activity,GP.remark,GP.tenant_id,GP.is_active,GP.created_by ,GP.created_time ,GP.last_modified_by,GP.last_modified_time,\n" + 
	 		"array_to_json(array_agg(json_build_object('applicationUuid',MB.application_uuid,'shgUuid',MB.shg_uuid,'applicationId',MB.application_id,'nulmApplicationId',MB.nulm_application_id,'applicationStatus',MB.application_status,\n" + 
	 		"'name',MB.name,'positionLevel',MB.position_level,'gender',MB.gender,'dob',MB.dob,'dateOfOpeningAccount',MB.date_of_opening_account,'adharNo',MB.adhar_no,'motherName',MB.mother_name,\n" + 
	 		"'fatherOrHusbandName',MB.father_or_husband_name,'address',MB.address,'phoneNo',MB.phone_no,'mobileNo',MB.mobile_no,'qualification',MB.qualification,'emailId',MB.email_id,\n" + 
	 		"'isUrbanPoor',MB.is_urban_poor,'isMinority',MB.is_minority,'isPwd',MB.is_pwd,'isStreetVendor',MB.is_street_vendor,'isHomeless',MB.is_homeless,'isInsurance',MB.is_insurance,\n" + 
	 		"'bplNo',MB.bpl_no,'minority',MB.minority,'caste',MB.caste,'wardNo',MB.ward_no,'nameAsPerAdhar',MB.name_as_per_adhar,'adharAcknowledgementNo',\n" + 
	 		"MB.adhar_acknowledgement_no,'insuranceThrough',MB.insurance_through,'documentAttachemnt',MB.document_attachemnt,'accountNo',MB.account_no,\n" + 
	 		"'bankName',MB.bank_name,'branchName',MB.branch_name,'remark',MB.remark,'tenantId',MB.tenant_id,'isActive',MB.is_active,'createdBy',MB.created_by,'createdTime',MB.created_time\n" + 
	 		",'lastModifiedBy',MB.last_modified_by,'lastModifiedTime',MB.last_modified_time) ))as member  from nulm_smid_shg_detail GP LEFT JOIN  nulm_smid_shg_member_details MB on GP.shg_uuid=MB.shg_uuid AND GP.tenant_id=MB.tenant_id AND (GP.status IN ('APPROVED','AWAITINGFORAPPROVAL') AND mb.application_status NOT IN ('DRAFTED','CREATED') OR (GP.status NOT IN ('APPROVED','AWAITINGFORAPPROVAL')  AND COALESCE(mb.application_status,'')=COALESCE(mb.application_status,''))) \n" + 
	 		"  where GP.created_by=(case when :createdBy <>'' then :createdBy else GP.created_by end) and GP.tenant_id=:tenantId and GP.is_active='true'AND\n" + 
	 		"GP.status IN (:status) AND GP.shg_uuid=(case when :shgUuid <>'' then :shgUuid else GP.shg_uuid end)  \n" + 
	 		" GROUP BY GP.shg_uuid";
	 
	 
	 public static final String GET_SHG_MEMBER_QUERY= "SELECT MB.application_uuid, MB.shg_uuid, MB.application_id, MB.nulm_application_id,MB.application_status, MB.name, MB.position_level, MB.gender, MB.dob,\n" + 
	 		"MB.date_of_opening_account ,MB.adhar_no, MB.mother_name, MB.father_or_husband_name, MB.address, MB.phone_no,MB.mobile_no, MB.qualification, MB.email_id, MB.is_urban_poor, MB.is_minority,\n" + 
	 		"MB.is_pwd, MB.is_street_vendor, MB.is_homeless, MB.is_insurance, MB.bpl_no,MB.minority, MB.caste, MB.ward_no, MB.name_as_per_adhar, MB.adhar_acknowledgement_no,\n" + 
	 		"MB.insurance_through, document_attachemnt, MB.account_no, MB.bank_name,MB.branch_name, MB.remark, MB.tenant_id, MB.is_active, MB.created_by, MB.created_time,\n" + 
	 		"MB.last_modified_by, MB.last_modified_time ,\n" + 
	 		"array_to_json(array_agg(json_build_object('shgUuid',GP.shg_uuid,'shgId',GP.shg_id,'name',GP.name,'type',GP.type,'groupsNominatedBy',GP.groups_nominated_by,\n" + 
	 		"'formedThrough',GP.formed_through,'status',GP.status,'address',GP.address,'contactNo',GP.contact_no,'dateOfFormation',\n" + 
	 		"GP.date_of_formation,'accountNo',GP.account_no,'dateOfOpeningAccount',GP.date_of_opening_account,'bankName',GP.bank_name,'branchName',GP.branch_name,'mainActivity',GP.main_activity,'remark',GP.remark) ))as group \n" + 
	 		"FROM public.nulm_smid_shg_member_details MB Inner  join nulm_smid_shg_detail GP on GP.shg_uuid=MB.shg_uuid AND GP.tenant_id=MB.tenant_id\n" + 
	 		"WHERE application_uuid=(case when ?  <>'' then ?  else application_uuid end) and MB.created_by=(case when ?  <>'' then ?  else MB.created_by end)  \n" + 
	 		" AND MB.tenant_id=? AND MB.application_status=(case when ?  <>'' then ?  else MB.application_status end) AND MB.is_active='true'\n" + 
	 		" GROUP BY MB.application_uuid";
	 
	 public static final String GET_SHG_MEMBER_COUNT_QUERY="select count(*) from nulm_smid_shg_member_details where shg_uuid=? and tenant_id=? and is_active='true' and position_level='MEMBER'";
	 public static final String GET_ORGANIZATION_MOBILE_NO_QUERY="select count(*) from nulm_organization where tenant_id=? and is_active='true'and mobile_no=? ";
	 public static final String GET_ORGANIZATION_NAME_QUERY="select count(*) from nulm_organization where tenant_id=? and is_active='true'and organization_name=? ";
	 public static final String GET_ORGANIZATION_QUERY="SELECT organization_uuid, user_id, organization_name, address, email_id,representative_name, mobile_no, registration_no, tenant_id, is_active,created_by, created_time, last_modified_by, last_modified_time\n" + 
	 		" FROM public.nulm_organization where tenant_id=? and is_active='true' and organization_uuid=(case when ?  <>'' then ?  else organization_uuid end)\n" + 
	 		" and organization_name=(case when ?  <>'' then ?  else organization_name end)  and registration_no=(case when ?  <>'' then ?  else registration_no end)\n" + 
	 		" AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END\n" + 
	 		" AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') <= CASE WHEN ?<>'' THEN DATE(?) ELSE TO_DATE(TO_CHAR(TO_TIMESTAMP(created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END\n" + 
	 		"ORDER BY created_time desc";
    
	 public static final String GET_SUH_NAME_QUERY="select count(*) from public.nulm_suh_application_detail where name_of_shelter=? and tenant_id=? and is_active='true'";
	 
	 public static final String GET_SUH_QUERY="SELECT NA.*,array_to_json(array_agg(json_build_object('suhUuid',FM.suh_uuid,'facilityUuid',FM.facility_uuid,'isBedding',FM.is_bedding,'beddingRemark',FM.bedding_remark,\n" + 
		"'isWashingOfLinen',FM.is_washing_of_linen,'washingOfLinenRemark',FM.washing_of_linen_remark,'isCleaningOfPremises',FM.is_cleaning_of_premises,'cleaningOfPremiseRemark',FM.cleaning_of_premise_remark,\n" + 
		"'isRecreationFacilities',FM.is_recreation_facilities,'recreationFacilitiesRemark',FM.recreation_facilities_remark,'isDrinkingWater',FM.is_drinking_water,\n" + 
		"'drinkingWaterRemark',FM.drinking_water_remark,'isMeals',FM.is_meals,'mealsRemark',FM.meals_remark,'isLockerForInmates',FM.is_locker_for_inmates,'lockerForInmatesRemark',FM.locker_for_inmates_remark,\n" + 
		"'isFireSafetyMeasure',FM.is_fire_safety_measure,'fireSafetyMeasureRemark',FM.fire_safety_measure_remark,'isOfficeSetUp',FM.is_office_set_up,'officeSetUpRemark',FM.office_set_up_remark,\n" + 
		"'isFirstAidKitAndTrainingToStaff',FM.is_first_aid_kit_and_training_to_staff,'firstAidKitAndTrainingToStaffRemark',FM.first_aid_kit_and_training_to_staff_remark,\n" + 
		"'isDisplayOfEmergencyNumbers',FM.is_display_of_emergency_numbers,'displayOfEmergencyNumbers_remark',FM.display_of_emergency_numbers_remark,'isToilet',FM.is_toilet,\n" + 
		"'toiletRemark',FM.toilet_remark,'facilityPicture',FM.facility_picture,'tenant_id',FM.tenant_id,'is_active',FM.is_active,'created_by',FM.created_by,'created_time',FM.created_time\n" + 
		",'last_modified_by',FM.last_modified_by,'last_modified_time',FM.last_modified_time) ))as facilities,\n" + 
		"array_to_json(array_agg(json_build_object('suhUuid',RM.suh_uuid,'recordUuid',RM.record_uuid,'isAssetInventoryRegister',RM.is_asset_inventory_register,'assetInventoryRegisterRemark',RM.asset_inventory_register_remark,\n" + 
		"'isAcountRegister',RM.is_acount_register,'acountRegisterRemark',RM.acount_register_remark,'isAttendanceRegisterOfStaff',RM.is_attendance_register_of_staff,'attendanceRegisterOfStaffRemark',RM.attendance_register_of_staff_remark,\n" + 
		"'isShelterManagementCommitteeRegister',RM.is_shelter_management_committee_register,'shelterManagementCommitteeRegisterRemark',RM.shelter_management_committee_register_remark,\n" + 
		"'isPersonnelAndSalaryRegister',RM.is_personnel_and_salary_register,'personnelAndSalaryRegisterRemark',RM.personnel_and_salary_register_remark,\n" + 
		"'isHousekeepingAndMaintenanceRegister',RM.is_housekeeping_and_maintenance_register,'housekeepingAndMaintenanceRegisterRemark',RM.housekeeping_and_maintenance_register_remark,\n" + 
		"'isComplaintAndSuggestionRegister',RM.is_complaint_and_suggestion_register,'complaintAndSuggestionRegisterRemark',RM.complaint_and_suggestion_register_remark,\n" + 
		"'isVisitorRegister',RM.is_visitor_register,'visitorRegisterRemark',RM.visitor_register_remark,'isProfileRegister',RM.is_profile_register,'profileRegisterRemark',RM.profile_register_remark,\n" + 
		"'tenant_id',RM.tenant_id,'is_active',RM.is_active,'created_by',RM.created_by,'created_time',RM.created_time\n" + 
		",'last_modified_by',RM.last_modified_by,'last_modified_time',RM.last_modified_time) ))as record,array_to_json(array_agg(json_build_object('suhUuid',SM.suh_uuid,'staffUuid',SM.staff_uuid,'isManager',SM.is_manager,'managerRemark',SM.manager_remark,\n" + 
		"'isSecurityStaff',SM.is_security_staff,'securityStaffRemark',SM.security_staff_remark,'isCleaner',SM.is_cleaner,'cleanerRemark',SM.cleaner_remark,'tenant_id',SM.tenant_id,'is_active',SM.is_active,'created_by',SM.created_by,'created_time',SM.created_time\n" + 
		",'last_modified_by',SM.last_modified_by,'last_modified_time',SM.last_modified_time) ))as staff	FROM public.nulm_suh_application_detail NA left join nulm_suh_facilities_maintenance FM ON NA.suh_uuid=FM.suh_uuid and NA.tenant_id=FM.tenant_id\n" + 
		"INNER JOIN  public.nulm_suh_record_maintenance RM ON NA.suh_uuid=RM.suh_uuid and NA.tenant_id=RM.tenant_id INNER JOIN public.nulm_suh_staff_maintenance SM ON NA.suh_uuid=SM.suh_uuid and NA.tenant_id=SM.tenant_id\n" + 
		"where NA.suh_uuid=(case when :suhUuid  <>'' then :suhUuid   else NA.suh_uuid end) and NA.created_by=(case when :createdBy  <>'' then :createdBy  else NA.created_by end) AND NA.tenant_id=:tenantId  AND NA.application_status IN (:status) \n" + 
		"AND NA.is_active='true'  AND TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') >= CASE WHEN :fromDate<>'' THEN DATE(:fromDate) ELSE\n" + 
		"TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END AND  TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') <= CASE WHEN :toDate<>'' THEN DATE(:toDate) ELSE \n" + 
		" TO_DATE(TO_CHAR(TO_TIMESTAMP(NA.created_time / 1000), 'YYYY-MM-DD'),'YYYY-MM-DD') END GROUP BY NA.suh_uuid \n" + 
		"ORDER BY created_time desc";
	 
	 public static final String GET_SUH_LOG_QUERY="SELECT log_uuid, name_of_shelter, date, name, qualification, gender, age, address, adhar_no, reason_for_staying, tenant_id, is_active, created_by, created_time, last_modified_by, last_modified_time\n" + 
	 		"  FROM public.nulm_suh_occupancy_log where log_uuid=(case when ?  <>'' then ?   else log_uuid end) and created_by=(case when ?  <>'' then ?  else created_by end)  and tenant_id=? and is_active='true'";

	 public static final String  GET_SUH_LOG_DATE_QUERY="SELECT log_uuid,created_time FROM public.nulm_suh_occupancy_log where log_uuid=? and tenant_id=? and is_active='true';";
	 public static final String  GET_SUH_SHELTER_NAME_QUERY="SELECT NA.name_of_shelter,NA.tenant_id,NA.suh_uuid FROM public.nulm_suh_application_detail NA where NA.created_by=(case when :createdBy  <>'' then :createdBy  else NA.created_by end) AND NA.tenant_id=:tenantId  AND NA.application_status IN (:status) \n" + 
	 		"AND NA.is_active='true'  GROUP BY NA.suh_uuid ";
	 
	 
	 
	 public static final String GET_SUSV_DOCUMENT_QUERY="SELECT count(*)  FROM public.nulm_susv_application_document  WHERE application_uuid=? and tenant_id=? and filestore_id=? and document_type=? and is_active='true';";

}
