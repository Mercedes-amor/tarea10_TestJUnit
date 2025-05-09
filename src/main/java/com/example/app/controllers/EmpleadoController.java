package com.example.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.services.EmpleadoService;

import jakarta.validation.Valid;

@RestController
// Sustituimos el @Controller por @RestController,
// Estamos indicando que va a ser una API REST

// Para que nos almacene la variable en la sesión y poder acceder a ella
@SessionAttributes("txtErr")

public class EmpleadoController {

    @Autowired(required = true)
    EmpleadoService empleadoService;

    //MOSTRAR TODOS
    @GetMapping("/empleados")
    public List<Empleado> showList() {
        // Quitamos todos los model.addAttribute
        // y demás información que transmitíamos a la vista

        // Solo pasamos todos los empleados y que sea el cliente
        // el que organice la visualización como quiera.
        // El status que devuelve será 200
        return empleadoService.obtenerTodos();
    }

    // MOSTRAR UN EMPLEADO
    @GetMapping("/empleado/{id}")
    public ResponseEntity<?> showOne(@PathVariable Long id) {

        // Obteneos el empleado
        Empleado empleado = empleadoService.obtenerPorId(id);

        // Si encontramos el empleado
        if (empleado != null) {
            // Devolvemos status Ok y el empleado
            // Sintaxis equivalente: ResponseEntity.ok(empleado);
            return ResponseEntity.status(HttpStatus.OK).body(empleado);

        } else {
            // Si no encontramos el empleado devolvemos status NOT_FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //AÑADIR EMPLEADO
    @PostMapping("/empleado/")

    // Añadimos el @RequestBody y eliminamos BindingResult (ya no aplicable)
    public ResponseEntity<?> showNew(@Valid @RequestBody Empleado empleadoForm) {

        // Si los datos no son correctos @Valid ya lanzará error 400
        // Por ello no necesitamos BindingResult

        // Creamos el empleado
        Empleado empleado = empleadoService.add(empleadoForm);

        // Si creamos correctamente el empleado
        if (empleado != null) {
            // Devolvemos status CREATED (201) y el empleado
            return ResponseEntity.status(HttpStatus.CREATED).body(empleado);

        } else {
            // Si no se crea correctamente el empleado devolvemos status BAD_REQUEST
            // versión equivalente: ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().body("Error en alta empleado");
        }

    }


    // EDITAR EMPLEADO
    @PutMapping("/empleado/{id}")
    public ResponseEntity<?> showEdit(@PathVariable Long id,
            @Valid @RequestBody Empleado empleadoForm) {

        // Primero buscamos el empleado a editar
        Empleado empleado = empleadoService.obtenerPorId(id);

        // Si NO encontramos el empleado
        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si encontramos el empleado lo editamos
        empleado = empleadoService.actualizar(empleadoForm);

        // Si editamos correctamente
        if (empleado != null) {
            // El status es 200, no 201 que es para creación
            return ResponseEntity.ok().body(empleado);
        } else {
            // BadRequest (400)
            return ResponseEntity.badRequest().body("Error en edición empleado");
        }

    }

    // BORRAR EMPLEADO
    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<?> showDelete(@PathVariable Long id) {

        // Primero buscamos el empleado a eliminar
        Empleado empleado = empleadoService.obtenerPorId(id);

        // Si NO encontramos el empleado
        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si encontramos el empleado lo borramos
        empleadoService.eliminarPorId(id);
        // Código 204
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // BUSCADORES

    //BÚSQUEDA POR SALARIO
    @GetMapping("/bysalary/{salario}")
    public ResponseEntity<?> geBySalary(@PathVariable Float salario) {

        // Obtenemos los empleados por salario
        List<Empleado> empleados = empleadoService.obtenerPorSalarioMayor(salario);
        if (empleados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(empleados);
        }
    }

    //BÚSQUEDA POR MAXID
    @GetMapping("/maxid")
    public ResponseEntity<?> getMaxId(Model model) {

        // Obtenemos el empleado de maxId
        Empleado empleado = empleadoService.obtenerMaxIdEmpleado();

        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(empleado);
        }
    }

    //BÚSQUEDA POR NOMBRE
    @PostMapping("/findByName")
    public ResponseEntity<?> showFindByName(
            @RequestParam("busqueda") String busqueda) {
        // Creamos el arrayList con los empleados encontrados
        List<Empleado> empleadosEncontrados = empleadoService.buscarPorNombre(busqueda);
        if (empleadosEncontrados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(empleadosEncontrados);
        }
    }

    // BÚSQUEDA POR GÉNERO
    @GetMapping("/findByGenero/{genero}")
    public ResponseEntity<?> showFindByGenero(
            @PathVariable Genero genero) {

        // Creamos el arrayList con los empleados encontrados
        List<Empleado> empleadosEncontrados = empleadoService.buscarPorGenero(genero);
        if (empleadosEncontrados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok().body(empleadosEncontrados);
        }
    }
}
