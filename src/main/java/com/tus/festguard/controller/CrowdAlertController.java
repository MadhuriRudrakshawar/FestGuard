package com.tus.festguard.controller;


import com.tus.festguard.model.CrowdAlert;
import com.tus.festguard.service.CrowdAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crowd-alerts")
@RequiredArgsConstructor
public class CrowdAlertController {

    private final CrowdAlertService crowdAlertService;

    @GetMapping("/active")
    public List<CrowdAlert> getActive() {
        return crowdAlertService.getActive();
    }

    @PatchMapping("/{id}/resolve")
    public CrowdAlert resolve(@PathVariable Long id) {
        return crowdAlertService.resolve(id);
    }
}
