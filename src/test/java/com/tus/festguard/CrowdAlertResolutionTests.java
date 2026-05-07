package com.tus.festguard;

import com.tus.festguard.enums.AlertStatus;
import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdAlert;
import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import com.tus.festguard.service.CrowdAlertService;
import com.tus.festguard.service.CrowdReportService;
import com.tus.festguard.service.FestivalAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CrowdAlertResolutionTests {

    @Autowired
    private FestivalAreaService festivalAreaService;

    @Autowired
    private CrowdReportService crowdReportService;

    @Autowired
    private CrowdAlertService crowdAlertService;

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
    void resolvesAlertAndRemovesItFromActiveList() {
        FestivalArea area = festivalAreaService.create(new FestivalArea(null, "Main Stage", "Primary stage", "STAGE"));
        CrowdReport report = new CrowdReport();
        report.setCrowdLevel(CrowdLevel.FULL);
        crowdReportService.submit(area.getId(), report);
        CrowdAlert alert = crowdAlertService.getActive().get(0);

        CrowdAlert resolved = crowdAlertService.resolve(alert.getId());

        assertThat(resolved.getStatus()).isEqualTo(AlertStatus.RESOLVED);
        assertThat(crowdAlertService.getActive()).isEmpty();
    }
}
