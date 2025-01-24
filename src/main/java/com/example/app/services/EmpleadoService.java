package com.example.app.services;

import java.util.List;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;

public interface EmpleadoService {
    List<Empleado> obtenerTodos();

    List<Empleado> obtenerPorSalarioMayor(Double salario);

    Empleado obtenerPorId(Long id);

    Empleado add(Empleado empleado);

    Empleado actualizar(Empleado empleado);

    void eliminarPorId(Long id);

    void eliminar(Empleado empleado);

    List<Empleado> buscarPorNombre(String textoNombre);

    List<Empleado> buscarPorGenero(Genero genero);


}
