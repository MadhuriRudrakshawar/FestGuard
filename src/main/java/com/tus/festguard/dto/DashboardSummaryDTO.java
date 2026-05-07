package com.tus.festguard.dto;

public class DashboardSummaryDTO {
    private long totalAreas;
    private long totalReports;
    private long activeAlerts;
    private long fullAreas;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(long totalAreas, long totalReports, long activeAlerts, long fullAreas) {
        this.totalAreas = totalAreas;
        this.totalReports = totalReports;
        this.activeAlerts = activeAlerts;
        this.fullAreas = fullAreas;
    }

    public long getTotalAreas() {
        return totalAreas;
    }

    public void setTotalAreas(long totalAreas) {
        this.totalAreas = totalAreas;
    }

    public long getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(long totalReports) {
        this.totalReports = totalReports;
    }

    public long getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(long activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public long getFullAreas() {
        return fullAreas;
    }

    public void setFullAreas(long fullAreas) {
        this.fullAreas = fullAreas;
    }
}
