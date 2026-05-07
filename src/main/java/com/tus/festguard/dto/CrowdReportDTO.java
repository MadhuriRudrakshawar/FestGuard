package com.tus.festguard.dto;

import com.tus.festguard.enums.CrowdLevel;
import java.time.LocalDateTime;

public class CrowdReportDTO {

    private Long areaId;
    private CrowdLevel crowdLevel;
    private String note;
    private LocalDateTime submittedAt;

    public CrowdReportDTO() {}

    public CrowdReportDTO(Long areaId, CrowdLevel crowdLevel, String note, LocalDateTime submittedAt) {
        this.areaId = areaId;
        this.crowdLevel = crowdLevel;
        this.note = note;
        this.submittedAt = submittedAt;
    }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public CrowdLevel getCrowdLevel() { return crowdLevel; }
    public void setCrowdLevel(CrowdLevel crowdLevel) { this.crowdLevel = crowdLevel; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
