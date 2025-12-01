package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    @NonNull
    Optional<UserEntity> findById(@NonNull Long id);

    Optional<UserEntity> findByUsername(String username);
}
