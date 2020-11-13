package com.dreamteam.emotional_assessment.repository;

import com.dreamteam.emotional_assessment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findUserByEmailEquals(String email);
}
