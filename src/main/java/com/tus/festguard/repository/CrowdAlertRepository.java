package com.tus.festguard.repository;

import com.tus.festguard.enums.AlertStatus;
import com.tus.festguard.model.CrowdAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrowdAlertRepository extends JpaRepository<CrowdAlert, Long> {

    List<CrowdAlert> findByStatus(AlertStatus status);

    long countByStatus(AlertStatus status);

    boolean existsByAreaIdAndStatus(Long areaId, AlertStatus status);
}
