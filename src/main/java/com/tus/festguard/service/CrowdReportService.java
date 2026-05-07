package com.tus.festguard.service;

import com.tus.festguard.enums.AlertStatus;
import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdAlert;
import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrowdReportService {

    private final CrowdReportRepository crowdReportRepository;
    private final CrowdAlertRepository crowdAlertRepository;
    private final FestivalAreaRepository festivalAreaRepository;

    public CrowdReport submit(Long areaId, CrowdReport report) {
        FestivalArea area = festivalAreaRepository.findById(areaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Area not found: " + areaId));

        report.setArea(area);
        report.setSubmittedAt(LocalDateTime.now());
        CrowdReport saved = crowdReportRepository.save(report);

        if (CrowdLevel.FULL.equals(report.getCrowdLevel())
                && !crowdAlertRepository.existsByAreaIdAndStatus(areaId, AlertStatus.ACTIVE)) {
            CrowdAlert alert = new CrowdAlert();
            alert.setArea(area);
            alert.setMessage("Area '" + area.getName() + "' is FULL");
            alert.setStatus(AlertStatus.ACTIVE);
            alert.setCreatedAt(LocalDateTime.now());
            crowdAlertRepository.save(alert);
        }

        return saved;
    }

    public List<CrowdReport> getRecent() {
        return crowdReportRepository.findTop10ByOrderBySubmittedAtDesc();
    }
}
