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

    public ProjectController(ProjectService projectService, ProjectOrchestrator projectOrchestrator) {
        this.projectService = projectService;
        this.projectOrchestrator = projectOrchestrator;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectDTO>>getProjects() {
        List<ProjectDTO> projects = projectService.getProjects();
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }


    @GetMapping("{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ProjectDTO>getProjects(@PathVariable Long projectId) {
        ProjectDTO projectDTO = projectService.getProject(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(projectDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        projectDTO = projectOrchestrator.addProject(projectDTO);
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
