package com.sks.demo.app.gastos;

import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.repository.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	@Bean
	CommandLineRunner init (ProjectRepository projectRepository) {
		return args -> {
			Project project = new Project();
			project.setName("Proyecto prueba 1");
			project.setDescription("Descripción del proyecto prueba 1");
			project.setBudget(new BigDecimal("10000.00"));
			project.setStartDate(LocalDate.now());
			project.setEndDate(LocalDate.now().plusWeeks(1));

			projectRepository.save(project);

			projectRepository.findAll().forEach( proj ->
					System.out.println("Proyecto: " + proj.getName() + ", Presupuesto: " + proj.getBudget()));
		};
	}*/

}
