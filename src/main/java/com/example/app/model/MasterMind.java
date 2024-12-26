package com.example.app.model;

import java.util.ArrayList;

public class MasterMind {
    private String numeroSecreto;
    private ArrayList<Intento> listaIntentos;
    private EstadoJuego estadoJuego;
    public static final int TAM_NUMERO = 4;
    public static final int MAX_INTENTOS = 10;

    public MasterMind() {
        listaIntentos = new ArrayList<>();
    }

    public String getNumeroSecreto() {
        return numeroSecreto;
    }

    public void setNumeroSecreto(String numeroSecreto) {
        this.numeroSecreto = numeroSecreto;
    }

    public ArrayList<Intento> getListaIntentos() {
        return listaIntentos;
    }

    public void setListaIntentos(ArrayList<Intento> listaIntentos) {
        this.listaIntentos = listaIntentos;
    }

    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public void setEstadoJuego(EstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }

}
