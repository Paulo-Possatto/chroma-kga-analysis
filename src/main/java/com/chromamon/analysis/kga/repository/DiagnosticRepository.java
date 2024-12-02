package com.chromamon.analysis.kga.repository;

import com.chromamon.analysis.kga.model.DiagnosticData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticRepository extends JpaRepository<DiagnosticData, Long> {
}