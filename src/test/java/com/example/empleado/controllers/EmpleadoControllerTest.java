package com.example.empleado.controllers;

//hasSize, instanceOf, put...
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//get, post, put...
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//jsonPath, status..
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.Main;
import com.example.app.controllers.EmpleadoController;
import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.services.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = Main.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)

public class EmpleadoControllerTest {

    @InjectMocks // clase a testar con servicio dependiente mockeado
    EmpleadoController empleadoController;

    @MockBean // clase/interfaz falseada con cláusulas when
    EmpleadoService empleadoService;

    @Autowired
    MockMvc mockMvc;

    // Variables necesarias para los test
    ArrayList<Empleado> mockList;
    Empleado empleadoSinId, empleadoConId;

    @BeforeAll
    public void init() {
        mockList = new ArrayList<>();
        mockList.add(new Empleado(1L, "test1", "test1@gmail.com", 5000F, true, Genero.MASCULINO));
        mockList.add(new Empleado(2L, "test2", "test2@gmail.com", 5000F, true, Genero.FEMENINO));
        mockList.add(new Empleado(3L, "test3", "test3@gmail.com", 5000F, false, Genero.MASCULINO));
        mockList.add(new Empleado(4L, "test4", "test4@gmail.com", 5000F, true, Genero.FEMENINO));

        empleadoSinId = new Empleado(null, "Nombre1", "nombre1@gmail.com", 5000F, true, Genero.MASCULINO);
        empleadoConId = new Empleado(5L, "Nombre1", "nombre1@gmail.com", 5000F, true, Genero.MASCULINO);
    }

    /// Test obtener todos
    @Test
    public void getAllEmpleadoTest() throws Exception {
        when(empleadoService.obtenerTodos()).thenReturn(mockList);
        mockMvc.perform(get("/empleados") // testear método get de la ruta "/empleados"
                .contentType(MediaType.APPLICATION_JSON))
                // Esto equivaldría a los assertsEquals
                .andExpect(status().isOk()) // se espera un 200
                .andExpect(jsonPath("$", hasSize(4))) // El $ representa al json
                .andExpect(jsonPath("$[0].id", is(1)))// Valor devuelto, frente al esperado
                .andExpect(jsonPath("$[0].nombre", is("test1")))
                .andExpect(jsonPath("$[0].email", is("test1@gmail.com")))
                .andExpect(jsonPath("$[0].salario", is(5000.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("test2")))
                .andExpect(jsonPath("$[1].email", is("test2@gmail.com")))
                .andExpect(jsonPath("$[1].salario", is(5000.0)));
    }

    // Test obtener un empleado
    @Test
    public void getOneEmpleadoTest() throws Exception {
        when(empleadoService.obtenerPorId(1L)).thenReturn(mockList.get(0));
        mockMvc.perform(get("/empleado/1") // testear método get de la ruta "/empleado/1"
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("test1")))
                .andExpect(jsonPath("$.salario", is(5000.0)));
    }

    // test del alta de empleado: método POST
    @Test
    public void addEmpleadoTest() throws Exception {
        when(empleadoService.add(empleadoSinId)).thenReturn(empleadoConId);
        mockMvc.perform(post("/empleado/") // testear método post de la ruta "/empleado/"
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(empleadoSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.nombre", is("Nombre1")))
                .andExpect(jsonPath("$.salario", is(5000.0)));
    }

    // Test borrado correcto
    @Test
    public void deleteOneEmpleadoTest_ok() throws Exception {
        when (empleadoService.obtenerPorId(1L)).thenReturn(mockList.get(0));
        mockMvc.perform(delete("/empleado/1") // testear ruta delete
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(empleadoService, times(1)).obtenerPorId(1L);
        verify(empleadoService, times(1)).eliminarPorId(1L);
    }

    // Test borrado con error
    @Test
    public void deleteOneEmpleadoTest_Ko() throws Exception {
        // when(empleadoService.obtenerPorId(999L)).thenThrow(new RuntimeException("Empleado no encontrado"));
        when(empleadoService.obtenerPorId(999L)).thenReturn(null);

        mockMvc.perform(delete("/empleado/999") // testear ruta delete
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Se espera el error NotFound

        verify(empleadoService, times(1)).obtenerPorId(999L);
        verify(empleadoService, times(0)).eliminarPorId(999L);

    }
}