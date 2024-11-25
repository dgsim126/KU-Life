package com.donggeun.kulife.repository;

import com.donggeun.kulife.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}