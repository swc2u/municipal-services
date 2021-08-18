package org.egov.cpt.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import static java.lang.String.format;

public class ReportOutput implements Serializable {
	public static final String CONTENT_DISPOSITION_ATTACH = "attachment;filename=%s";
    public static final String CONTENT_DISPOSITION_INLINE = "inline;filename=%s";
    public static final Character Y = 'Y';
    public static final Character N = 'N';
    public static final String NA = "N/A";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String UNDERSCORE = "_";
    public static final String HYPHEN = "-";
    public static final String WHITESPACE = " ";
    public static final String SLASH = "/";
    public static final String COLON = ":";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String UNKNOWN = "Unknown";
    
    private static final long serialVersionUID = -2559611205589631905L;
    private byte[] reportOutputData;
    private ReportFormat reportFormat;
    private String reportName = "report";
    private ReportDisposition reportDisposition = ReportDisposition.INLINE;

    public ReportOutput() {
        //default constructor
    }

    public ReportOutput(byte[] reportOutputData, ReportRequest reportInput) {
        this.reportOutputData = reportOutputData;
        this.reportFormat = reportInput.getReportFormat();
    }

    public byte[] getReportOutputData() {
        return this.reportOutputData;
    }

    public void setReportOutputData(byte[] reportOutputData) {
        this.reportOutputData = reportOutputData;
    }

    public ReportFormat getReportFormat() {
        return this.reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setReportDisposition(ReportDisposition reportDisposition) {
        this.reportDisposition = reportDisposition;
    }

    public InputStream asInputStream() {
        return new ByteArrayInputStream(reportOutputData);
    }

    public String reportDisposition() {
        return new StringBuilder()
                .append(format(this.reportDisposition == ReportDisposition.INLINE ?
                        CONTENT_DISPOSITION_INLINE : CONTENT_DISPOSITION_ATTACH, reportName))
                .append(DOT).append(reportFormat)
                .toString();
    }
}
