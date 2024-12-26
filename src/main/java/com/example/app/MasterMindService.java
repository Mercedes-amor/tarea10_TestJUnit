package com.example.app;

import java.util.Random;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.app.model.EstadoJuego;
import com.example.app.model.Intento;
import com.example.app.model.MasterMind;

@Service
@Scope("session")
public class MasterMindService {

    public MasterMind masterMind;
    private String mensajeError = ""; // Variable del mensaje de error

    // Establecemos unos valores por defecto para la configuración del juego
    private int maxIntentos = MasterMind.MAX_INTENTOS;
    private int longCombi = MasterMind.TAM_NUMERO;

    // Métodos para establecer los parámetros de configuración (maxIntentos y
    // longCombi)
    public void setMaxIntentos(int maxIntentos) {
        this.maxIntentos = maxIntentos;
    }

    public void setLongCombi(int longCombi) {
        this.longCombi = longCombi;
    }

    public void nuevoJuego() {
        masterMind = new MasterMind();
        do {
            masterMind.setNumeroSecreto(cadenaAlAzar(longCombi));
        } while (cadenaConDuplicados(masterMind.getNumeroSecreto()));
        masterMind.getListaIntentos().clear();
        masterMind.setEstadoJuego(EstadoJuego.JUGANDO);
        // Reseteamos la variable de mensajeError
        mensajeError = "";
        System.out.println("=====> Num secreto: " + masterMind.getNumeroSecreto());
    }

    public void comprobarIntento(String intento) {

        // Mensaje error: la longitud del intento no cohincide con la longitud de la
        // combinación secreta
        if (intento.length() != masterMind.getNumeroSecreto().length()) {
            mensajeError = "Debes introducir una combinación de " + masterMind.getNumeroSecreto().length()
                    + " números.";
            return;
        }

        // Mensaje error: El intento tiene números repetidos
        if (cadenaConDuplicados(intento)) {
            mensajeError = "No puedes introducir una combinación con números repetidos.";
            return;
        }

        // Mensaje error: Si el intento no contiene solo números
        if (!esNumerico(intento)) {
            mensajeError = "Debe introducir una combinación sólo de números";
            return;
        }

        // Mensaje error: Intento repetido
        if (yaIntroducido(intento)) {
            mensajeError = "Ya ha probado esta combinación.";
            return;
        }

        /*
         * Ya no es necesario este fragmento de código
         * if (cadenaConDuplicados(intento) || intento.length() != longCombi) {
         * 
         * return;
         * }
         */

        // Reseteamos variable mensaje error si el intento es válido
        mensajeError = "";

        int bienColocados = 0, malColocados = 0;
        String numeroSecreto = masterMind.getNumeroSecreto();

        for (int cont = 0; cont < intento.length(); cont++) {
            char letra = intento.charAt(cont);
            if (letra == numeroSecreto.charAt(cont))
                bienColocados++;
            else if (numeroSecreto.indexOf(letra) != -1)
                malColocados++;
        }
        masterMind.getListaIntentos().add(new Intento(intento, bienColocados, malColocados));
        if (bienColocados == longCombi)
            masterMind.setEstadoJuego(EstadoJuego.GANO);
        if (masterMind.getListaIntentos().size() >= maxIntentos)
            masterMind.setEstadoJuego(EstadoJuego.PERDIO);
    }

    private boolean cadenaConDuplicados(String cad) {
        for (int i = 0; i < cad.length(); i++) {
            char c = cad.charAt(i);
            if (cad.indexOf(c, i + 1) != -1)
                return true;
        }
        return false;
    }

    private String cadenaAlAzar(int tamanho) {
        Random random = new Random();
        String cad = "";
        int x;
        for (int i = 0; i < tamanho; i++) {
            x = random.nextInt(10);
            cad += Integer.toString(x);
        }
        return cad;
    }

    // MÉTODOS NUEVOS

    // Obtener intentos faltantes restando a maxIntentos el tamaño del Array
    // listaIntentos
    public int getIntentosRestantes() {
        return maxIntentos - masterMind.getListaIntentos().size();
    }

    // Booleano para comprobar si un intento ya fué introducido
    private boolean yaIntroducido(String intento) {
        for (Intento i : masterMind.getListaIntentos()) {
            if (i.getCombinacion().equals(intento)) {
                return true;
            }
        }
        return false;
    }

    // Método para saber si un String contiene solo números
    private boolean esNumerico(String intento) {
        try {
            Integer.parseInt(intento);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Get mensaje error
    public String getMensajeError() {
        return mensajeError;
    }


}
