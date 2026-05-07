package com.tus.festguard.service;



import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.CrowdAlertRepository;
import com.tus.festguard.repository.CrowdReportRepository;
import com.tus.festguard.repository.FestivalAreaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalAreaService {

    private final FestivalAreaRepository festivalAreaRepository;
    private final CrowdReportRepository crowdReportRepository;
    private final CrowdAlertRepository crowdAlertRepository;

    public FestivalArea create(FestivalArea festivalArea) {
        return festivalAreaRepository.save(festivalArea);
    }

    public List<FestivalArea> getAll() {
        return festivalAreaRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        if (!festivalAreaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area not found: " + id);
        }

        crowdAlertRepository.deleteByAreaId(id);
        crowdReportRepository.deleteByAreaId(id);
        festivalAreaRepository.deleteById(id);
    }
}
