package org.egov.cpt.service.pdf;

public enum ReportFormat {
    PDF, XLS, RTF, HTM, TXT, CSV;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
