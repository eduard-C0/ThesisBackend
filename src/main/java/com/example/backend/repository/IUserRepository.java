package com.example.backend.repository;

import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);

    @Query("UPDATE User u SET u.enabled = true WHERE u.id = ?1")
    @Modifying
    public void enable(Integer id);
}
