package org.egov.cpt.service.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.egov.cpt.util.ReportUtil;

public class ReportRequest {
	public static final String REPORT_CONFIG_FILE = "/config/reports.properties";
	 private static final Properties REPORT_CONFIG = ReportUtil.loadReportConfig();
    private ReportDataSourceType reportDataSourceType;
    private String reportTemplate;
    private ReportFormat reportFormat;
    private Object reportInputData;
    private Map<String, Object> reportParams;
    private boolean printDialogOnOpenReport = false;

    
    public ReportRequest(String reportTemplate, Object reportInputData) {
        this(reportTemplate, reportInputData, new HashMap<>());
    }

    public ReportRequest(String reportTemplate, Object reportInputData, Map<String, Object> reportParams) {
        initialize(reportTemplate, reportParams);
        this.reportInputData = reportInputData;
        this.reportDataSourceType = ReportDataSourceType.JAVABEAN;
    }

    public ReportRequest(String reportTemplate, Map<String, Object> reportParams, ReportDataSourceType dataSourceType) {
        initialize(reportTemplate, reportParams);
        this.reportDataSourceType = dataSourceType;
    }

    public ReportRequest(String reportTemplate, Object[] reportInputData, Map<String, Object> reportParams) {
        this(reportTemplate, (Object) reportInputData, reportParams);
    }

    public ReportRequest(String reportTemplate, Collection reportInputData, Map<String, Object> reportParams) {
        this(reportTemplate, (Object) reportInputData, reportParams);
    }

    private void initialize(String reportTemplate, Map<String, Object> reportParams) {
        this.reportTemplate = reportTemplate;
        this.reportParams = reportParams;
        if (REPORT_CONFIG == null) {
            this.reportFormat = ReportFormat.PDF;
        } else {
            this.reportFormat = ReportFormat.valueOf(REPORT_CONFIG.getProperty(this.reportTemplate, ReportFormat.PDF.name()));
        }
    }
    public String getReportTemplate() {
        return this.reportTemplate;
    }

    public ReportFormat getReportFormat() {
        return this.reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    public Map<String, Object> getReportParams() {
        return this.reportParams;
    }

    public void setReportParams(Map<String, Object> reportParams) {
        this.reportParams = reportParams;
    }

    public Object getReportInputData() {
        return this.reportInputData;
    }

    public ReportDataSourceType getReportDataSourceType() {
        return this.reportDataSourceType;
    }

    public boolean isPrintDialogOnOpenReport() {
        return this.printDialogOnOpenReport;
    }

    public void setPrintDialogOnOpenReport(boolean printDialogOnOpenReport) {
        this.printDialogOnOpenReport = printDialogOnOpenReport;
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
            return null;
        }
    }
}
