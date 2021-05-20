package org.egov.tl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.DocumentContext;

import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.ServiceRequestRepository;
import org.egov.tl.util.CTLConstants;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

/**
 * Invoke PDF service to generate PDFs which will be subsequently used as email attachments.
 * @author rajiv
 *
 */
@Slf4j
@Service
public class TLPDFGenerationService {

	@Value("${egov.filestore.host}")
    private String fileStoreHost;

    @Value("${egov.filestore.path}")
    private String fileStorePath;

    private ServiceRequestRepository serviceRequestRepository;

    private TLConfiguration config;
    
    private RestTemplate restTemplate;

    @Autowired
    public TLPDFGenerationService(ServiceRequestRepository serviceRequestRepository, TLConfiguration config, RestTemplate restTemplate) {
    	this.serviceRequestRepository = serviceRequestRepository;
    	this.config = config;
    	this.restTemplate = restTemplate;
	}
    
	public String generatePaymentReceiptPDF(DocumentContext documentContext, TradeLicense license) throws Exception {
		log.info("Generating Payment receipt for license "+license.getApplicationNumber());
		String tenantId = license.getTenantId();
		StringBuilder urlBuilder = new StringBuilder(config.getPdfServiceHost()).append(
				config.getPdfServiceCreateEndpoint()
					.replace(CTLConstants.CTL_RECEIPT_PARAM_KEY, CTLConstants.CTL_RECEIPT_PARAM_VALUE)
					.replace(CTLConstants.CTL_RECEIPT_PARAM_TENANT_KEY, tenantId)
		);
		
		//Transform the tax head codes that start with TL_ for localisation.
        JSONArray lineItems = documentContext.read("$.Payment.paymentDetails[?(@.businessService=='"+license.getBusinessService()+"')].bill.billDetails[0].billAccountDetails[?(@.order >= 0)]");
        
        for (int i = 0; i < lineItems.size(); i++) {
        	Map<String, Object> map = (Map<String, Object>) lineItems.get(i);
        	String taxHeadCode = (String) map.get("taxHeadCode");
        	if (taxHeadCode.endsWith("_FEE")) {
        		taxHeadCode = "TL_FEE";
        	} else if (taxHeadCode.endsWith("_PENALTY")) {
        		taxHeadCode = "TL_TIME_PENALTY";
        	} else if (taxHeadCode.endsWith("_TAX")) {
        		taxHeadCode = "TL_TAX";
        	} else if (taxHeadCode.endsWith("_ROUNDOFF")) {
        		taxHeadCode = "TL_ROUNDOFF";
        	} else if (taxHeadCode.endsWith("_CHARGES")) {
        		taxHeadCode = "TL_CHARGES";
        	}
        	map.put("taxHeadCode", taxHeadCode);
        }
		Map<String, Object> pdfCreateRequestBody = new HashMap<>();
		
		pdfCreateRequestBody.put("RequestInfo", documentContext.read("$.RequestInfo"));
		pdfCreateRequestBody.put("Licenses", new TradeLicense[] { license });
		pdfCreateRequestBody.put("Payments", new Object[] { documentContext.read("$.Payment")});
		pdfCreateRequestBody.put(CTLConstants.CTL_RECEIPT_GENERATED_BY_KEY, CTLConstants.CTL_RECEIPT_GENERATED_BY_VALUE);
		LinkedHashMap<String, Object> response = this.callPDFService(pdfCreateRequestBody, urlBuilder);

		//Fetch the url from file store id.
		@SuppressWarnings("unchecked")
		String fileStoreId = (String) ((ArrayList<Object>) response.get("filestoreIds")).get(0);
		log.info("Generated filestore id " + fileStoreId);
		Map<String, String> fileStoreUrlMap = this.getUrlByFileStoreId(tenantId, (List<String>) response.get("filestoreIds"));
		log.info("Retrieved filestore url", fileStoreUrlMap.get(fileStoreId));

		return fileStoreUrlMap.get(fileStoreId);
	}
	
	/**
     * Returns UserDetailResponse by calling user service with given uri and object
     * @param userRequest Request object for user service
     * @param uri The address of the endpoint
     * @return Response from user service as parsed as userDetailResponse
     */
    private LinkedHashMap<String, Object> callPDFService(Object userRequest, StringBuilder uri) {
        try{
            @SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>)serviceRequestRepository.fetchResult(uri, userRequest);
            return responseMap;
        }
        catch(IllegalArgumentException  e)
        {
            throw new CustomException("IllegalArgumentException","ObjectMapper not able to convertValue in userCall");
        }
    }
    
    /**
     * Get FileStoreUrls By passing FileStore Id's
     *
     * @param tenantId
     * @param fileStoreId
     * @return
     * @throws Exception
     */
    public Map<String, String> getUrlByFileStoreId(String tenantId, List<String> fileStoreIds) throws Exception {
        Map<String, String> fileStoreUrls = null;

        String idLIst = fileStoreIds.toString().substring(1, fileStoreIds.toString().length() - 1).replace(", ", ",");
        log.info("idLIst: " + idLIst);
        String Url = fileStoreHost + fileStorePath + "?tenantId=" + tenantId + "&fileStoreIds=" + idLIst;

        try {
            fileStoreUrls = this.restTemplate.getForObject(Url, Map.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getResponseBodyAsString());
        }
        log.info("filrStoreUrls " + fileStoreUrls);
        if (null != fileStoreUrls && !fileStoreUrls.isEmpty())
            return fileStoreUrls;
        return null;
    }

}
