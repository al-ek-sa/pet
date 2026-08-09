package org.example.authservice.bd.repository;

import org.example.authservice.bd.entity.User;
import org.example.authservice.dto.LoginName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT password, user_name FROM users WHERE login = :login", nativeQuery = true)
    Optional<LoginName> findByLoginNativePassword(@Param("login") String login);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE login = :login)", nativeQuery = true)
    boolean existsByLogin(@Param("login") String login);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE user_name = :userName)", nativeQuery = true)
    boolean existsByUserName(@Param("userName") String userName);

    @Modifying
    @Query(value = "UPDATE users SET password = :password WHERE login = :login", nativeQuery = true)
    int updatePassword(@Param("login") String login, @Param("password") String password);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE login = :login and email = :email)", nativeQuery = true)
    boolean existsByLoginAndEmail(@Param("login") String login, @Param("email") String email);
}
