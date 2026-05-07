package com.tus.festguard;

import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import com.tus.festguard.service.CrowdReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MissingAreaReportTests {

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
    void rejectsCrowdReportForMissingArea() {
        CrowdReport report = new CrowdReport();
        report.setCrowdLevel(CrowdLevel.LOW);

        assertThatThrownBy(() -> crowdReportService.submit(99999L, report))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }
}
