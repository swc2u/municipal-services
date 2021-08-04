package org.egov.cpt.util;

import org.egov.cpt.service.pdf.ApplicationCacheManager;
import org.egov.cpt.service.pdf.ReportFormat;
import org.egov.cpt.service.pdf.ReportOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportViewerUtil {

    @Autowired
    private ApplicationCacheManager applicationCacheManager;


    public static String getContentType(ReportFormat fileFormat) {
        return ReportUtil.contentType(fileFormat);
    }

    public String addReportToTempCache(ReportOutput reportOutput) {
        String reportId = UUID.randomUUID().toString();
        applicationCacheManager.put(reportId, reportOutput);
        return reportId;
    }

    public ReportOutput getReportOutputFormCache(String reportOutputCacheKey) {
        return applicationCacheManager.get(reportOutputCacheKey, ReportOutput.class);
    }

    public void removeReportOutputFromCache(String reportOutputCacheKey) {
        applicationCacheManager.remove(reportOutputCacheKey);
    }
}
