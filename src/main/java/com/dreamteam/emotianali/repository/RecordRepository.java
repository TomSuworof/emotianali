package com.dreamteam.emotianali.repository;

import com.dreamteam.emotianali.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
