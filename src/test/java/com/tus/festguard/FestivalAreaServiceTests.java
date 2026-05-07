package com.tus.festguard;

import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import com.tus.festguard.service.FestivalAreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FestivalAreaServiceTests {

    @Autowired
    private FestivalAreaService festivalAreaService;

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
    void createsAndListsFestivalAreas() {
        FestivalArea area = new FestivalArea();
        area.setName("Main Stage");
        area.setDescription("Primary performance area");
        area.setAreaType("STAGE");

        FestivalArea saved = festivalAreaService.create(area);

        List<FestivalArea> areas = festivalAreaService.getAll();
        assertThat(saved.getId()).isNotNull();
        assertThat(areas)
                .hasSize(1)
                .extracting(FestivalArea::getName)
                .containsExactly("Main Stage");
    }
}
