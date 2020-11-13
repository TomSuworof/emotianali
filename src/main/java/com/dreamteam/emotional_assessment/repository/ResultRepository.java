package com.dreamteam.emotional_assessment.repository;

import com.dreamteam.emotional_assessment.entity.Tone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Tone, Long> {
}
