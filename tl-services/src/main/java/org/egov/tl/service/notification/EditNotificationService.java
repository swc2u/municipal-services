package org.egov.tl.service.notification;

import static org.egov.tl.util.CTLConstants.businessService_BOOK_SHOP;
import static org.egov.tl.util.CTLConstants.businessService_DHOBI_GHAT;
import static org.egov.tl.util.CTLConstants.businessService_REHRI_DL;
import static org.egov.tl.util.CTLConstants.businessService_REHRI_RC;
import static org.egov.tl.util.TLConstants.businessService_TL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.util.NotificationUtil;
import org.egov.tl.web.models.Difference;
import org.egov.tl.web.models.SMSRequest;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditNotificationService {


    private NotificationUtil util;

    @Autowired
    private TLConfiguration config;

    @Autowired
    public EditNotificationService(NotificationUtil util) {
        this.util = util;
    }

    public void sendEditNotification(TradeLicenseRequest request, Map<String, Difference> diffMap) {
        List<SMSRequest> smsRequests = enrichSMSRequest(request, diffMap);
        String businessService = request.getLicenses().isEmpty()?null:request.getLicenses().get(0).getBusinessService();
        if (businessService == null)
            businessService = businessService_TL;
        switch(businessService)
        {
	        case businessService_REHRI_RC:
			case businessService_REHRI_DL:
			case businessService_DHOBI_GHAT:
			case businessService_BOOK_SHOP:
            case businessService_TL:
                util.sendSMS(smsRequests,config.getIsTLSMSEnabled());
                break;

//            case businessService_BPA:
//                util.sendSMS(smsRequests,config.getIsBPASMSEnabled());
//                break;
        }
    }

    /**
     * Creates smsRequest for edits done in update
     * @param request The update Request
     * @param diffMap The map of id to Difference for each license
     * @return The smsRequest
     */
    private List<SMSRequest> enrichSMSRequest(TradeLicenseRequest request, Map<String, Difference> diffMap) {
        List<SMSRequest> smsRequests = new LinkedList<>();
        String tenantId = request.getLicenses().get(0).getTenantId();
        String localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
        for (TradeLicense license : request.getLicenses()) {
            Difference diff = diffMap.get(license.getId());
            String message = util.getCustomizedMsg(diff, license, localizationMessages);
            if (StringUtils.isEmpty(message)) continue;

            Map<String, String> mobileNumberToOwner = new HashMap<>();

            license.getTradeLicenseDetail().getOwners().forEach(owner -> {
                if (owner.getMobileNumber() != null)
                    mobileNumberToOwner.put(owner.getMobileNumber(), owner.getName());
            });
            smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
        }
        return smsRequests;
    }

}
