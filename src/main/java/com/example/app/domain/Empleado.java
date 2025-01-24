package com.example.app.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//Cuando comparamos con indexOf dos instancias de Empleado se compara el id
@EqualsAndHashCode(of = "id")
public class Empleado {

    @NotNull
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Double salario;
    private Boolean enActivo;
    @NotNull
    private Genero genero;
}
