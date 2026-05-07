package com.tus.festguard.dto;

public class DashboardSummaryDTO {
    private long totalAreas;
    private long totalReports;
    private long activeAlerts;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(long totalAreas, long totalReports, long activeAlerts) {
        this.totalAreas = totalAreas;
        this.totalReports = totalReports;
        this.activeAlerts = activeAlerts;
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
}
