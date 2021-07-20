package org.egov.cpt.service.pdf;

public enum ReportDisposition {
    INLINE, ATTACHMENT;

    public String toString() {
        return this.name().toLowerCase();
    }
}
