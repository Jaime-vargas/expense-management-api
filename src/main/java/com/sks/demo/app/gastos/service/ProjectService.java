package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.dto.ProjectDTO;
import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final  ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    // Transactional methods

    public List<ProjectDTO> getProjects(){
        List<ProjectDTO> projectsDTO = new ArrayList<>();
                projectRepository.findAll().forEach(project -> {
                    projectsDTO.add(entityToDto(project));
                });
        return projectsDTO;
    }

    public ProjectDTO getProject(Long id){
        Project project = findById(id);
        return entityToDto(project);
    }

    @Transactional
    public Project saveProject(Project project){
        project = projectRepository.save(project);
        return project;
    }

    @Transactional
    public Project updateProject(Project project){
        project = projectRepository.save(project);
        return project;
    }

    @Transactional
    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }

    // No transactional methods

    // Replantear validación con metodos para validar si existen elementos
    public Project findById(Long id){
        return projectRepository.findById(id).orElseThrow(
               () -> new HandleException(400, "Bad Request", "Project with id: " + id + " does not exist")
       );
    }

    public List<Project> findByUserId (Long userId){
        return projectRepository.findByUser_Id(userId).get();
    }

    public void validateDates(ProjectDTO projectDTO){
        if(projectDTO.getEndDate().isBefore(projectDTO.getStartDate())){
            throw new HandleException(400, "Bad Request", "End date cannot be before start date");
        }
    }
  // Reviewing
/*
    public Project dtoToEntity(ProjectDTO projectDTO){
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setUser(null);
        return project;
    }
*/
    public ProjectDTO entityToDto(Project project){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());

        projectDTO.setBudget(project.getBudget());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setUsername(project.getUser().getUsername());
        return projectDTO;
    }
}
