package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.model.ConfiguracionFormulario;
import com.example.app.model.EstadoJuego;
import com.example.app.model.FormInfo;

@Controller
@Scope("session")
public class MasterMindController {

    @Autowired
    public MasterMindService masterMindService;

    // Añadimos el formulario a la página de inicio para introducir los valores de
    // configuración
    @GetMapping("/")
    public String showHome(Model model) {
        model.addAttribute("configForm", new ConfiguracionFormulario());

        return "indexView";
    }

    // Añadimos ruta POST para "/" dónde se establecerán los parámetros de
    // configuración del juego
    @PostMapping("/")
    public String configureGame(ConfiguracionFormulario configuracionFormulario) {
        masterMindService.setMaxIntentos(configuracionFormulario.getMaxIntentos());
        masterMindService.setLongCombi(configuracionFormulario.getLongCombi());
        return "redirect:/nuevoJuego";
    }

    @GetMapping("/juego")
    public String showGame(Model model) {
        model.addAttribute("formInfo", new FormInfo());
        model.addAttribute("listaIntentos", masterMindService.masterMind.getListaIntentos());
        model.addAttribute("estadoJuego", masterMindService.masterMind.getEstadoJuego());
        model.addAttribute("intentosRestantes", masterMindService.getIntentosRestantes());// Contador intentos restantes
        model.addAttribute("mensajeError", masterMindService.getMensajeError()); // Agrega el mensaje de error al modelo

        return "juegoView";
    }

    @PostMapping("/juego")
    public String newTry(FormInfo formInfo, Model model) {
        masterMindService.comprobarIntento(formInfo.getIntento());
        System.out.println("Lista intentos:" + formInfo.getIntento());

        if (masterMindService.masterMind.getEstadoJuego() != EstadoJuego.JUGANDO) {
            // Si el juego terminó, cargar la vista actualizada
            //Así evitamos el problema de que no se muestre en tiempo el mensaje de victoria
            model.addAttribute("formInfo", new FormInfo());
            model.addAttribute("listaIntentos", masterMindService.masterMind.getListaIntentos());
            model.addAttribute("estadoJuego", masterMindService.masterMind.getEstadoJuego().toString());
            
            System.out.println("Estado juego: " + masterMindService.masterMind.getEstadoJuego().toString());
            return "juegoView"; //Si hacemos un redirect no vemos directamente el mensaje de victoria
        }

        // Si el juego no terminó sí redirigimos para continuar jugando
        return "redirect:/juego";

    }

    @GetMapping("/nuevoJuego")
    public String newGame() {
        masterMindService.nuevoJuego();
        return "redirect:/juego";
    }
}
