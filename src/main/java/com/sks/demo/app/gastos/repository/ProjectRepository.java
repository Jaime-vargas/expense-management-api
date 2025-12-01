package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    void deleteById(@NonNull Long id);

    @NonNull
    Optional<Project> findById(@NonNull Long id);

    Optional<List<Project>> findByUser_Id(Long userId);


}

