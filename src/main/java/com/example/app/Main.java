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
		System.out.println("Tarea 7 Acceso a datos, Ej01");
	}

	@Bean
	CommandLineRunner initData(EmpleadoService empleadoService) {
		return args -> {
			empleadoService.add(
					new Empleado(null, "pepe", "pepe@gmail.com", 25000f, true, Genero.MASCULINO));
			empleadoService.add(
					new Empleado(null, "ana", "ana@gmail.com", 28000f, true, Genero.FEMENINO));
			empleadoService.add(
					new Empleado(null, "Mercedes", "Mercedesamor@gmail.com", 30000f, true, Genero.FEMENINO));
			empleadoService.add(
					new Empleado(null, "Indiana Jones", "laxsiempre@gmail.com", 128000f, false, Genero.OTROS));
		};
	}
}
