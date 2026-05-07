package com.tus.festguard.repository;

import com.tus.festguard.model.CrowdReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrowdReportRepository extends JpaRepository<CrowdReport, Long> {
    List<CrowdReport> findTop10ByOrderBySubmittedAtDesc();
}
