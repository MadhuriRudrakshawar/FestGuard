package com.tus.festguard.service;

import com.tus.festguard.dto.DashboardSummaryDTO;
import com.tus.festguard.enums.AlertStatus;
import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FestivalAreaRepository festivalAreaRepository;
    private final CrowdReportRepository crowdReportRepository;
    private final CrowdAlertRepository crowdAlertRepository;

    public DashboardSummaryDTO getSummary() {
        return new DashboardSummaryDTO(
                festivalAreaRepository.count(),
                crowdReportRepository.count(),
                crowdAlertRepository.countByStatus(AlertStatus.ACTIVE),
                crowdReportRepository.countDistinctAreasByCrowdLevel(CrowdLevel.FULL)
        );
    }
}
