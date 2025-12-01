package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.model.UserEntity;
import com.sks.demo.app.gastos.service.ProjectService;
import com.sks.demo.app.gastos.service.UserEntityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrchestrator {

    private final UserEntityService userEntityService;
    private final ProjectService projectService;

    public UserOrchestrator(UserEntityService userEntityService, ProjectService projectService) {
        this.userEntityService = userEntityService;
        this.projectService = projectService;
    }


    public void deleteUser(Long userId){
        // 1- validate if user exist
        userEntityService.findById(userId);
        // 2- validate no user root
        userEntityService.isUserRoot(userId);
        // Validate if user has projects
        List<Project> projects = projectService.findByUserId(userId);
        if (!projects.isEmpty()){
            UserEntity userEntity = userEntityService.findByUsername("root");
            projects.forEach(
                    project -> project.setUser(userEntity)
            );
        }
        userEntityService.deleteUser(userId);
    }
}
