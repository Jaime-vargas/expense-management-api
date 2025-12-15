package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.Mapper.Mapper;
import com.sks.demo.app.gastos.dto.ProjectDTO;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.model.UserEntity;
import com.sks.demo.app.gastos.service.ExpenseService;
import com.sks.demo.app.gastos.service.ProjectService;
import com.sks.demo.app.gastos.service.UserEntityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProjectOrchestrator {

    private final ProjectService projectService;
    private final ExpenseService expenseService;
    private final UserEntityService userEntityService;
    private final ExpenseOrchestrator expenseOrchestrator;
    private final Mapper mapper;

    // --- CONSTRUCTOR ---
    public ProjectOrchestrator(ProjectService projectService, ExpenseService expenseService, ExpenseOrchestrator expenseOrchestrator, UserEntityService userEntityService, Mapper mapper) {
        this.projectService = projectService;
        this.expenseService = expenseService;
        this.userEntityService = userEntityService;
        this.expenseOrchestrator = expenseOrchestrator;
        this.mapper = mapper;
    }

    // --- METHODS ---
    public ProjectDTO saveProject(ProjectDTO projectDTO){
        // VALIDATIONS
        projectService.validateDates(projectDTO);
        UserEntity userEntity = userEntityService.findByUsername(projectDTO.getUsername());

        Project project = mapper.dtoToEntity(projectDTO, userEntity);
        project = projectService.saveProject(project);
        return mapper.entityToDto(project);
    }

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO){
        // VALIDATIONS
        projectService.findById(projectId);
        projectService.validateDates(projectDTO);
        UserEntity userEntity = userEntityService.findByUsername(projectDTO.getUsername());

        Project project = mapper.dtoToEntity(projectDTO, userEntity);
        project.setId(projectId);
        project = projectService.saveProject(project);
        return mapper.entityToDto(project);
    }

    @Transactional
    public void deleteProjectAndItsExpenses(Long projectId){
        // VALIDATIONS
        projectService.findById(projectId);

        expenseService.getExpensesByProjectId(projectId).forEach(
                expenseDTO -> expenseOrchestrator.deleteExpenseAndItsFiles(projectId, expenseDTO.getId()));
        projectService.deleteProject(projectId);
    }
}
