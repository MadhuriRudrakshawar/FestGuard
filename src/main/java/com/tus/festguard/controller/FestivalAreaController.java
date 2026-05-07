package com.tus.festguard.controller;

import com.tus.festguard.model.FestivalArea;
import com.tus.festguard.service.FestivalAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/festival-areas")
@RequiredArgsConstructor
public class FestivalAreaController {

    private final FestivalAreaService festivalAreaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FestivalArea create(@Valid @RequestBody FestivalArea festivalArea) {
        return festivalAreaService.create(festivalArea);
    }

    @GetMapping
    public List<FestivalArea> getAll() {
        return festivalAreaService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        festivalAreaService.delete(id);
    }
}
