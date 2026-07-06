package com.anurag.hft.repository;

import com.anurag.hft.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
