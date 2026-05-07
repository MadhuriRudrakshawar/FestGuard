package com.tus.festguard.service;


import com.tus.festguard.enums.AlertStatus;
import com.tus.festguard.model.CrowdAlert;
import com.tus.festguard.repository.CrowdAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrowdAlertService {

    private final CrowdAlertRepository crowdAlertRepository;

    public List<CrowdAlert> getActive() {
        return crowdAlertRepository.findByStatus(AlertStatus.ACTIVE);
    }

    public CrowdAlert resolve(Long id) {
        CrowdAlert alert = crowdAlertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found: " + id));
        alert.setStatus(AlertStatus.RESOLVED);
        return crowdAlertRepository.save(alert);
    }
}
