package com.sks.demo.app.gastos.controller;

import com.sks.demo.app.gastos.dto.ProjectDTO;
import com.sks.demo.app.gastos.service.orchestrators.ProjectOrchestrator;
import com.sks.demo.app.gastos.service.ProjectService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectOrchestrator projectOrchestrator;

    // --- CONSTRUCTOR ---
    public ProjectController(ProjectService projectService, ProjectOrchestrator projectOrchestrator) {
        this.projectService = projectService;
        this.projectOrchestrator = projectOrchestrator;
    }

    // --- METHODS ---
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectDTO>>getProjects() {
        List<ProjectDTO> projectDTOList = projectService.getProjects();
        return ResponseEntity.status(HttpStatus.OK).body(projectDTOList);
    }

    @GetMapping("{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId) {
        ProjectDTO projectDTO = projectService.getProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(projectDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> saveProject(@Valid @RequestBody ProjectDTO projectDTO) {
        projectDTO = projectOrchestrator.saveProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
    }


    @PutMapping("{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectDTO projectDTO) {
        projectDTO =  projectOrchestrator.updateProject(projectId, projectDTO);
        return ResponseEntity.status(HttpStatus.OK).body(projectDTO);
    }


    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectOrchestrator.deleteProjectAndItsExpenses(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Project with id " + projectId + " has been deleted"));
    }


}
