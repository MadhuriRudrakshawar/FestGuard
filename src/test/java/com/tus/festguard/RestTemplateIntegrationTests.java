package com.tus.festguard;

import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdAlert;
import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class RestTemplateIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FestivalAreaRepository festivalAreaRepository;

    @Autowired
    private CrowdReportRepository crowdReportRepository;

    @Autowired
    private CrowdAlertRepository crowdAlertRepository;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void cleanDatabase() {
        crowdAlertRepository.deleteAll();
        crowdReportRepository.deleteAll();
        festivalAreaRepository.deleteAll();
    }

    @Test
    void submitCrowdReport_returns201AndSavedReport() {
        FestivalArea area = festivalAreaRepository.save(new FestivalArea(null, "Main Stage", "Primary stage", "STAGE"));

        CrowdReport request = new CrowdReport();
        request.setCrowdLevel(CrowdLevel.MEDIUM);
        request.setNote("Getting busy");

        ResponseEntity<CrowdReport> response = restTemplate.postForEntity(
                url("/api/crowd-reports/area/" + area.getId()), request, CrowdReport.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getSubmittedAt()).isNotNull();
    }

    @Test
    void submitCrowdReport_unknownArea_returns404() {
        CrowdReport request = new CrowdReport();
        request.setCrowdLevel(CrowdLevel.LOW);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/api/crowd-reports/area/99999"), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getRecentReports_returnsSubmittedReports() {
        FestivalArea area = festivalAreaRepository.save(new FestivalArea(null, "Food Village", "Food area", "FOOD"));

        CrowdReport request = new CrowdReport();
        request.setCrowdLevel(CrowdLevel.LOW);
        request.setNote("Quiet");
        restTemplate.postForEntity(url("/api/crowd-reports/area/" + area.getId()), request, CrowdReport.class);

        ResponseEntity<CrowdReport[]> response = restTemplate.getForEntity(
                url("/api/crowd-reports/recent"), CrowdReport[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void submitFullReport_createsActiveAlert() {
        FestivalArea area = festivalAreaRepository.save(new FestivalArea(null, "Acoustic Tent", "Small tent", "STAGE"));

        CrowdReport request = new CrowdReport();
        request.setCrowdLevel(CrowdLevel.FULL);
        request.setNote("At capacity");
        restTemplate.postForEntity(url("/api/crowd-reports/area/" + area.getId()), request, CrowdReport.class);

        ResponseEntity<CrowdAlert[]> response = restTemplate.getForEntity(
                url("/api/crowd-alerts/active"), CrowdAlert[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void resolveAlert_returns200AndResolvedStatus() {
        FestivalArea area = festivalAreaRepository.save(new FestivalArea(null, "Craft Beer Bar", "Bar area", "BAR"));

        CrowdReport request = new CrowdReport();
        request.setCrowdLevel(CrowdLevel.FULL);
        restTemplate.postForEntity(url("/api/crowd-reports/area/" + area.getId()), request, CrowdReport.class);

        CrowdAlert[] alerts = restTemplate.getForObject(url("/api/crowd-alerts/active"), CrowdAlert[].class);
        assertThat(alerts).isNotNull().hasSize(1);

        ResponseEntity<CrowdAlert> response = restTemplate.exchange(
                url("/api/crowd-alerts/" + alerts[0].getId() + "/resolve"),
                org.springframework.http.HttpMethod.PATCH,
                null,
                CrowdAlert.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus().name()).isEqualTo("RESOLVED");

        CrowdAlert[] remaining = restTemplate.getForObject(url("/api/crowd-alerts/active"), CrowdAlert[].class);
        assertThat(remaining).isEmpty();
    }
}
