package org.egov.bookings.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.bookings.config.BookingsConfiguration;
import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.repository.impl.ServiceRequestRepository;
import org.egov.bookings.utils.BookingsCalculatorConstants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MDMSService {



	@Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private BookingsConfiguration config;
   

    /**
     * Creates MDMS request
     * @param requestInfo The RequestInfo of the calculationRequest
     * @param tenantId The tenantId of the tradeLicense
     * @return MDMSCriteria Request
     */
    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {

        // master details for TL module
        List<MasterDetail> fyMasterDetails = new ArrayList<>();
        // filter to only get code field from master data

        final String filterCodeForUom = "$.[?(@.active==true)]";

        fyMasterDetails.add(MasterDetail.builder().name(BookingsCalculatorConstants.MDMS_FINANCIALYEAR).filter(filterCodeForUom).build());

        ModuleDetail fyModuleDtls = ModuleDetail.builder().masterDetails(fyMasterDetails)
                .moduleName(BookingsCalculatorConstants.MDMS_EGF_MASTER).build();

        List<MasterDetail> tlMasterDetails = new ArrayList<>();
/*        tlMasterDetails.add(MasterDetail.builder().name(TLCalculatorConstants.MDMS_CALCULATIONTYPE)
                .filter(filterCodeForUom).build());
        ModuleDetail tlModuleDtls = ModuleDetail.builder().masterDetails(tlMasterDetails)
                .moduleName(TLCalculatorConstants.MDMS_TRADELICENSE).build();*/

        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(fyModuleDtls);
       // moduleDetails.add(tlModuleDtls);

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId)
                .build();

        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }


    
    public Map<String,Long> getTaxPeriods(RequestInfo requestInfo,BookingsModel bookingsModel,Object mdmsData){
        Map<String,Long> taxPeriods = new HashMap<>();
        try {
        	 String jsonPath = BookingsCalculatorConstants.MDMS_FINACIALYEAR_PATH.replace("{}",bookingsModel.getFinancialYear());
             List<Map<String,Object>> jsonOutput =  JsonPath.read(mdmsData, jsonPath);
             Map<String,Object> financialYearProperties = jsonOutput.get(0);
             Object startDate = financialYearProperties.get(BookingsCalculatorConstants.MDMS_STARTDATE);
             Object endDate = financialYearProperties.get(BookingsCalculatorConstants.MDMS_ENDDATE);
             taxPeriods.put(BookingsCalculatorConstants.MDMS_STARTDATE,(Long) startDate);
             taxPeriods.put(BookingsCalculatorConstants.MDMS_ENDDATE,(Long) endDate);

        } catch (Exception e) {
            log.error("Error while fetvhing MDMS data", e);
            throw new CustomException("INVALID FINANCIALYEAR", "No data found for the financialYear: "+bookingsModel.getFinancialYear());
        }
        return taxPeriods;
    }




    public Object mDMSCall(RequestInfo requestInfo,String tenantId){
        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,tenantId);
        StringBuilder url = getMdmsSearchUrl();
        Object result = serviceRequestRepository.fetchResult(url , mdmsCriteriaReq);
        return result;
    }



    /**
     * Creates and returns the url for mdms search endpoint
     *
     * @return MDMS Search URL
     */
    private StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }



}
