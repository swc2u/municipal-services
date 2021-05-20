package org.egov.bookings.utils;

import java.math.BigDecimal;

public class BookingsCalculatorConstants {

	public static final String OSBM_TAXHEAD_CODE_1 = "PARKING_LOTS_MANUAL_OPEN_SPACE_BOOKING_BRANCH";

	public static final String OSBM_TAXHEAD_CODE_2 = "CGST_UTGST_MANUAL_OPEN_SPACE_BOOKING_BRANCH";
	
	public static final String OSBM_TAXHEAD_CODE_3 = "CLEANING_CHRGS_MANUAL_OPEN_SPACE_BOOKING_BRANCH";
	
	public static final String OSBM_TAXHEAD_CODE_4 = "SECURITY_MANUAL_OPEN_SPACE_BOOKING_BRANCH";
	
	public static final String OSBM_TAXHEAD_CODE_5 = "FACILITATION_CHRGS_MANUAL_OPEN_SPACE_BOOKING_BRANCH";
	

	public static final String MDMS_ROUNDOFF_TAXHEAD_OSBM = "OSBM_ROUND_OFF";
	
	public static final String BWT_TAXHEAD_CODE_1 = "WATER_TANKAR_CHARGES_BOOKING_BRANCH";

	public static final String BWT_TAXHEAD_CODE_2 = "BWT_TAX";

	
	
	public static final String GFCP_TAX_CODE_1 = "PARKING_LOTS_COMMERCIAL_GROUND_BOOKING_BRANCH";

	public static final String GFCP_TAX_CODE_2 = "CGST_UTGST_COMMERCIAL_GROUND_BOOKING_BRANCH";
	
	public static final String GFCP_TAX_CODE_3 = "SECURITY_COMMERCIAL_GROUND_BOOKING_BRANCH";
	
	public static final String GFCP_TAX_CODE_4 = "FACILITATION_CHRGS_COMMERCIAL_GROUND_BOOKING_BRANCH";
	
	public static final String GFCP_TAX_CODE_5 = "CLEANING_CHRGS_COMMERCIAL_GROUND_BOOKING_BRANCH";
	

	public static final String MDMS_ROUNDOFF_TAXHEAD_GFCP = "GFCP_ROUND_OFF";
	
	
	public static final String OSUJM_TAX_CODE_1 = "PARKING_LOTS_GROUND_OPEN_SPACES_BOOKING_BRANCH";

	public static final String OSUJM_TAX_CODE_2 = "CGST_UTGST_GROUND_OPEN_SPACES_BOOKING_BRANCH";
	
	public static final String OSUJM_TAX_CODE_3 = "SECURITY_GROUND_OPEN_SPACES_BOOKING_BRANCH";
	
	public static final String OSUJM_TAX_CODE_4 = "FACILITATION_CHRGS_GROUND_OPEN_SPACES_BOOKING_BRANCH";
	
	public static final String OSUJM_TAX_CODE_5 = "CLEANING_CHRGS_GROUND_OPEN_SPACES_BOOKING_BRANCH";
	
	
	public static final String PACC_TAX_CODE_1 = "RENT_COMMUNITY_CENTRES_JHANJ_GHAR_BOOKING_BRANCH";

	public static final String PACC_TAX_CODE_2 = "CGST_UTGST_COMMUNITY_CENTRES_JHANJ_GHAR_BOOKING_BRANCH";
	
	public static final String PACC_TAX_CODE_3 = "CLEANING_CHRGS_COMMUNITY_CENTRES_JHANJ_GHAR_BOOKING_BRANCH";
	
	public static final String PACC_TAX_CODE_4 = "SECURITY_CHRGS_COMMUNITY_CENTRES_JHANJ_GHAR_BOOKING_BRANCH";
	
	public static final String PACC_TAX_CODE_5 = "FACILITATION_CHRGS_COMMUNITY_CENTRES_JHANJ_GHAR_BOOKING_BRANCH";
	
	
	public static final String MDMS_ROUNDOFF_TAXHEAD_PACC = "PACC_ROUND_OFF";

	public static final String MDMS_ROUNDOFF_TAXHEAD_OSUJM = "OSUJM_ROUND_OFF";
	
	public static final String MDMS_FINANCIALYEAR  = "FinancialYear";
	
	public static final String MDMS_EGF_MASTER = "egf-master";
	
	public static final String MDMS_FINACIALYEAR_PATH = "$.MdmsRes.egf-master.FinancialYear[?(@.code==\"{}\")]";

    public static final String MDMS_STARTDATE  = "startingDate";

    public static final String MDMS_ENDDATE  = "endingDate";
    
    
    public static final String ROOM_FOR_COMMUNITY_TAX_CODE_1 = "BKROOM";

	public static final String ROOM_FOR_COMMUNITY_TAX_CODE_2 = "BKROOM_TAX";

	public static final String MDMS_ROUNDOFF_TAXHEAD_ROOM = "BKROOM_ROUND_OFF";
	
	public static final BigDecimal UGST_AND_CGST_TAX = new BigDecimal(18);
	
	public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	
	public static final BigDecimal FACILATION_CHARGE = new BigDecimal(100);
	
}
