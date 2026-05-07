package com.tus.festguard.repository;

import com.tus.festguard.enums.CrowdLevel;
import com.tus.festguard.model.CrowdReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CrowdReportRepository extends JpaRepository<CrowdReport, Long> {
    List<CrowdReport> findTop10ByOrderBySubmittedAtDesc();

    @Query("select count(distinct report.area.id) from CrowdReport report where report.crowdLevel = :crowdLevel")
    long countDistinctAreasByCrowdLevel(CrowdLevel crowdLevel);

    void deleteByAreaId(Long areaId);
}
