package com.tus.festguard.repository;

import com.tus.festguard.model.CrowdAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrowdAlertRepository extends JpaRepository<CrowdAlert, Long> {
}
