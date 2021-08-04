package org.egov.cpt.util;

import com.google.common.collect.ImmutableMap;

import org.egov.cpt.service.pdf.ReportFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ReportUtil {
	public static final String REPORT_CONFIG_FILE = "/config/reports.properties";

    public static final ImmutableMap<ReportFormat, String> CONTENT_TYPES = ImmutableMap.<ReportFormat, String>builder()
            .put(ReportFormat.PDF, "application/pdf")
            .put(ReportFormat.XLS, "application/vnd.ms-excel")
            .put(ReportFormat.RTF, "application/rtf")
            .put(ReportFormat.HTM, "text/html")
            .put(ReportFormat.TXT, "text/plain")
            .put(ReportFormat.CSV, "text/plain").build();
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

    private ReportUtil() {
        // Only static api's
    }

    public static String contentType(ReportFormat reportFormat) {
        return CONTENT_TYPES.get(reportFormat);
    }

 
    public static Properties loadReportConfig() {
        Properties reportProps = null;
        try {
            InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(REPORT_CONFIG_FILE);
            if (configStream != null) {
                reportProps = new Properties();
                reportProps.load(configStream);
            }
            return reportProps;
        } catch (IOException e) {
            LOGGER.warn("Exception while loading report configuration file [{}]", REPORT_CONFIG_FILE, e);
            return null;
        }
    }
}
