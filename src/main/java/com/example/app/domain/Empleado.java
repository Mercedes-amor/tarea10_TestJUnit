package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Empleado {

    private Long id;
    private String nombre;
    private String email;
    private Double salario;
    private Boolean enActivo;
    private Genero genero;
}
