package com.dreamteam.emotianali.repository;

import com.dreamteam.emotianali.entity.Tone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Tone, Long> {
}
