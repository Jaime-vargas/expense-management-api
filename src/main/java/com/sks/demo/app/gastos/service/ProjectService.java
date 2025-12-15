package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.Mapper.Mapper;
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

    private final Mapper mapper;
    private final  ProjectRepository projectRepository;

    // --- CONSTRUCTOR ---
    public ProjectService(Mapper mapper, ProjectRepository projectRepository){
        this.mapper = mapper;
        this.projectRepository = projectRepository;
    }

    // --- METHODS ---
    public List<ProjectDTO> getProjects(){
        return projectRepository.findAll().stream().map(mapper::entityToDto).toList();
    }

    public ProjectDTO getProject(Long id){
       Project project = findById(id);
       return mapper.entityToDto(project);
    }

    @Transactional
    public Project saveProject(Project project){
        project = projectRepository.save(project);
        return project;
    }

    @Transactional
    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }

    // --- VALIDATIONS ---
    public Project findById(Long id){
        return projectRepository.findById(id).orElseThrow(
               () -> new HandleException(400, "Bad Request", "Project with id: " + id + " does not exist")
       );
    }

    public List<Project> findByUserId (Long userId){
        return projectRepository.findByUser_Id(userId);
    }

    public void validateDates(ProjectDTO projectDTO){
        if(projectDTO.getEndDate().isBefore(projectDTO.getStartDate())){
            throw new HandleException(400, "Bad Request", "End date cannot be before start date");
        }
    }
}
