package com.tus.festguard.service;



import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.repository.FestivalAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalAreaService {

    private final FestivalAreaRepository festivalAreaRepository;

    public FestivalArea create(FestivalArea festivalArea) {
        return festivalAreaRepository.save(festivalArea);
    }

    public List<FestivalArea> getAll() {
        return festivalAreaRepository.findAll();
    }
}
