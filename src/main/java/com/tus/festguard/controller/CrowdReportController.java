package com.tus.festguard.controller;


import com.tus.festguard.model.CrowdReport;
import com.tus.festguard.service.CrowdReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crowd-reports")
@RequiredArgsConstructor
public class CrowdReportController {

    private final CrowdReportService crowdReportService;

    @PostMapping("/area/{areaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CrowdReport submit(@PathVariable Long areaId, @Valid @RequestBody CrowdReport report) {
        return crowdReportService.submit(areaId, report);
    }

    @GetMapping("/recent")
    public List<CrowdReport> getRecent() {
        return crowdReportService.getRecent();
    }
}
