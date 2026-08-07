package org.example.authservice.bd.repository;

import org.example.authservice.bd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<User, UUID> {
    @Modifying
    @Query(value = "UPDATE users SET password = :password WHERE login = :login", nativeQuery = true)
    int updatePassword(@Param("login") String login, @Param("password") String password);
}
