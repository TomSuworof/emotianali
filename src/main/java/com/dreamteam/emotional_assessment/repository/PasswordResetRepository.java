package com.dreamteam.emotional_assessment.repository;

import com.dreamteam.emotional_assessment.entity.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, String> {

    Optional<PasswordResetRequest> findById(String id);
}
