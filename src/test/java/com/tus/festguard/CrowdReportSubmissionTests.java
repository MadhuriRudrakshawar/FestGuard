package com.tus.festguard;

import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import com.tus.festguard.service.CrowdReportService;
import com.tus.festguard.service.FestivalAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CrowdReportSubmissionTests {

    @Autowired
    private FestivalAreaService festivalAreaService;

    @Autowired
    private CrowdReportService crowdReportService;

    @Autowired
    private FestivalAreaRepository festivalAreaRepository;

    @Autowired
    private CrowdReportRepository crowdReportRepository;

    @Autowired
    private CrowdAlertRepository crowdAlertRepository;

    @BeforeEach
    void cleanDatabase() {
        crowdAlertRepository.deleteAll();
        crowdReportRepository.deleteAll();
        festivalAreaRepository.deleteAll();
    }

    @Test
    void submitsValidCrowdReportForExistingArea() {
        FestivalArea area = festivalAreaService.create(new FestivalArea(null, "Food Court", "Food vendors", "FOOD"));
        CrowdReport report = new CrowdReport();
        report.setCrowdLevel(CrowdLevel.MEDIUM);
        report.setNote("Queue building");

        CrowdReport saved = crowdReportService.submit(area.getId(), report);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getArea().getId()).isEqualTo(area.getId());
        assertThat(saved.getSubmittedAt()).isNotNull();
        assertThat(crowdReportService.getRecent()).hasSize(1);
    }
}
