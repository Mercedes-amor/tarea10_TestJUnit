package com.example.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Empleado;
import com.example.app.services.EmpleadoService;


@Controller
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;

    @GetMapping({"/list" , "/"})
    public String getList(Model model) {
        model.addAttribute("listaEmpleados", empleadoService.obtenerTodos());
        return "listView";
    }
    
    @GetMapping("/listone/{id}")
        public String getOne (@PathVariable Long id, Model model){
            model.addAttribute("empleado", empleadoService.obtenerPorId(id));
            return "listOneView";
        }
    
    @GetMapping("/add")
    public String showNew(Model model) {
        model.addAttribute("empleadoForm", new Empleado());
        return "newFormView";
    }

    @PostMapping("/add/submit")
    public String showNewSubmit(Empleado empleadoForm) {
        Empleado empleado = empleadoService.add(empleadoForm);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String getEdit(@PathVariable long id, Model model) {
        Empleado empleado = empleadoService.obtenerPorId(id);
        model.addAttribute("empleadoForm", empleado);
        return "editFormView";
    }

    @PostMapping("/edit/submit")
    public String getEditSubmit(Empleado empleadoForm) {
        Empleado empleado = empleadoService.actualizar(empleadoForm);
        return "redirect:/list";
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable Long id) {
        empleadoService.eliminarPorId(id);
        return "redirect:/list";
    }
}
