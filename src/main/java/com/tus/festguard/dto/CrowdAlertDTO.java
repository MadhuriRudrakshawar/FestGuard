package com.tus.festguard.dto;

import com.tus.festguard.enums.AlertStatus;
import java.time.LocalDateTime;

public class CrowdAlertDTO {
    private Long areaId;
    private String message;
    private AlertStatus status;
    private LocalDateTime createdAt;

    public CrowdAlertDTO() {}

    public CrowdAlertDTO(Long areaId, String message, AlertStatus status, LocalDateTime createdAt) {
        this.areaId = areaId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public AlertStatus getStatus() { return status; }
    public void setStatus(AlertStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
