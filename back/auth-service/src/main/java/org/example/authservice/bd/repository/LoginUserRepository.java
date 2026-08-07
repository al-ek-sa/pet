package org.example.authservice.bd.repository;

import jakarta.annotation.Nullable;
import org.example.authservice.bd.entity.User;
import org.example.authservice.dto.LoginName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginUserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT password, user_name FROM users WHERE login = :login", nativeQuery = true)
    Optional<LoginName> findByLoginNativePassword(@Param("login") String login);
}
