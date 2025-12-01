package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.dto.ProjectDTO;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.model.UserEntity;
import com.sks.demo.app.gastos.service.ExpenseService;
import com.sks.demo.app.gastos.service.ProjectService;
import com.sks.demo.app.gastos.service.UserEntityService;
import org.springframework.stereotype.Service;

@Service
public class ProjectOrchestrator {

    private final ProjectService projectService;
    private final ExpenseService expenseService;
    private final UserEntityService userEntityService;
    private final ExpenseOrchestrator expenseOrchestrator;

    public ProjectOrchestrator(ProjectService projectService, ExpenseService expenseService, ExpenseOrchestrator expenseOrchestrator, UserEntityService userEntityService) {
        this.projectService = projectService;
        this.expenseService = expenseService;
        this.userEntityService = userEntityService;
        this.expenseOrchestrator = expenseOrchestrator;
    }

    // DELETE PROJECTS AND ITS EXPENSES
    public void deleteProjectAndItsExpenses(Long projectId){
        projectService.findById(projectId);
        expenseService.getExpensesByProjectId(projectId).forEach(
                expenseDTO -> expenseOrchestrator.deleteExpenseAndItsFiles(projectId, expenseDTO.getId()));
        projectService.deleteProject(projectId);
    }

    public ProjectDTO addProject(ProjectDTO projectDTO){
        projectService.validateDates(projectDTO);
        projectDTO.setId(null);

        Project project = dtoToEntity(projectDTO);
        project = projectService.saveProject(project);
        return projectService.entityToDto(project);
    }

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO){

        projectService.findById(projectId);
        projectService.validateDates(projectDTO);
        projectDTO.setId(projectId);

        Project project = dtoToEntity(projectDTO);
        project = projectService.saveProject(project);

        return projectService.entityToDto(project);
    }

    public Project dtoToEntity(ProjectDTO projectDTO){
        UserEntity userEntity =
                userEntityService.findByUsername(projectDTO.getUsername());
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setUser(userEntity);
        return project;
    }


}
