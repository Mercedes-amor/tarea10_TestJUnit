package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;


import com.example.app.services.EmpleadoService;
@SpringBootApplication
public class Main {

	@Autowired
	EmpleadoService empleadoService;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		System.out.println("Tarea 9 API REST");
	}

	@Bean
	CommandLineRunner initData(EmpleadoService empleadoService) {
		return args -> {
			empleadoService.add(
					new Empleado(null, "pepe", "pepe@gmail.com", 18000f, true, Genero.MASCULINO));
			empleadoService.add(
					new Empleado(null, "ana", "ana@gmail.com", 20000f, true, Genero.FEMENINO));
			empleadoService.add(
					new Empleado(null, "Mercedes", "Mercedesamor@gmail.com", 30000f, true, Genero.FEMENINO));
			empleadoService.add(
					new Empleado(null, "Indiana Jones", "laxsiempre@gmail.com", 35000f, false, Genero.OTROS));
		};
	}
}
