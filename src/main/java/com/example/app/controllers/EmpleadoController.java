package com.example.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.services.EmpleadoService;

@Controller

// Para que nos almacene la variable en la sesión y poder acceder a ella
@SessionAttributes("txtErr")

public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;

    // Variable global para almacenar los textos de error
    private String txtErr;

    @GetMapping({ "/list", "/" })
    public String showList(@RequestParam(required = false) Integer err, Model model) {
        // Si el RequestParam no es null se muestra el contenido de la variable txtErr
        if (err != null) {
            model.addAttribute("txtErr", txtErr);
        }
        // Reseteamos variable del mensaje
        txtErr = null;

        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        return "listView";

    }

    @GetMapping("/listone/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("empleado", empleadoService.obtenerPorId(id));

        } catch (RuntimeException e) {
            // Si se lanza la excepción guardamos el mensaje en la variable txtErr para
            // mostrarla
            txtErr = e.getMessage();
            return "redirect:/list?err=1";
        }
        return "listOneView";
    }

    @GetMapping("/add")
    public String showNew(
            @RequestParam(required = false) Integer err,
            Model model) {

        // Para los errores genéricos que llegan por el @Valid
        // Como el formato del email o los campos vacíos.
        if (err != null) {
            model.addAttribute("textErr", "Hubo un error en los datos del formulario");
        }

        // Para los errores que llegan por la excepción lanzada desde el servicio
        // Id repetido y salario <18000
        if (txtErr != null) {
            model.addAttribute("textErr", txtErr);
            // Reseteamos la variable
            txtErr = null;

        }

        model.addAttribute("empleadoForm", new Empleado());
        return "newFormView";
    }

    @PostMapping("/add/submit")
    public String showNewSubmit(@Valid Empleado empleadoForm, BindingResult bindingResult, Model model) {
        // Para los errores que llegan por el @Valid
        if (bindingResult.hasErrors()) {
            return "redirect:/add?err=1";
        }
        try {
            empleadoService.add(empleadoForm);
        } catch (RuntimeException e) {
            // Capturamos las excepciones que llegan del service
            txtErr = e.getMessage();
            return "redirect:/add";
        }

        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String getEdit(
            @PathVariable long id,
            @RequestParam(required = false) Integer err,
            Model model) {

        // Para los errores genéricos que llegan por el @Valid
        // Como el formato del email o los campos vacíos.
        if (err != null) {
            model.addAttribute("text2Err", "Hubo un error en los datos actualizados");
        }

        // Para los errores que llegan por la excepción lanzada desde el servicio
        // Id repetido y salario <18000
        if (txtErr != null) {
            model.addAttribute("text2Err", txtErr);
            // Reseteamos la variable
            txtErr = null;
        }
        Empleado empleado = empleadoService.obtenerPorId(id);
        model.addAttribute("empleadoForm", empleado);
        return "editFormView";
    }

    @PostMapping("/edit/submit")
    public String getEditSubmit(
            @Valid Empleado empleadoForm,
            BindingResult bindingResult) {
        // Para los errores que llegan por el @Valid
        if (bindingResult.hasErrors()) {
            return "redirect:/edit/submit?err=1";
        }
        try {
            empleadoService.actualizar(empleadoForm);
        } catch (RuntimeException e) {
            // Capturamos las excepciones que llegan del service
            txtErr = e.getMessage();
            return "redirect:/edit/submit";
        }

        return "redirect:/list";
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable Long id) {
        empleadoService.eliminarPorId(id);
        return "redirect:/list";
    }

    // BUSCADOR

    @GetMapping("/findByName")
    public String showFindByName() {
        return "listView";
    }

    @PostMapping("/findByName")
    public String showFindByNameSubmit(
            @RequestParam("busqueda") String busqueda,
            Model model) {
        // Creamos el arrayList con los empleados encontrados
        List<Empleado> empleadosEncontrados = empleadoService.buscarPorNombre(busqueda);

        // Pasamos los empleados encontrados a la vista
        model.addAttribute("listaEmpleados", empleadosEncontrados);

        // Mantenemos el valor de 'nombreABuscar' ingresado para mostrarlo en el campo
        // del formulario
        model.addAttribute("nombre", busqueda);

        return "listView";
    }

    // BÚSQUEDA POR GÉNERO
    @GetMapping("/findByGenero/{genero}")
    public String showFindByGenero(
        @PathVariable Genero genero, 
        Model model) {
            //Llamamos a la función buscarPorGénero y mostramos el resultado
        model.addAttribute("listaEmpleados", empleadoService.buscarPorGenero(genero));
            //Pasamos a la vista la opción seleccionada
        model.addAttribute("generoSeleccionado", genero);
        return "listView";
    }

}
