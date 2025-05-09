package com.example.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.repositories.EmpleadoRepository;

@Service
@Primary
public class EmpleadoServiceImplBd implements EmpleadoService {
    private static Double LIMITE_MIN_SALARIO = 18000D;
    private static Double LIMITE_MAX_SALARIO = 100000D;

    @Autowired
    EmpleadoRepository empleadoRepository;

    public List<Empleado> obtenerTodos() {
        List<Empleado> todos = empleadoRepository.findAll();
        List<Empleado> activos = new ArrayList<>();

        //Filtramos por empleados activos
        for (Empleado e : todos) {
            if (e.getEnActivo()) {
                activos.add(e);
            }
        }
        return activos;
    }

    public List<Empleado> obtenerPorSalarioMayor(Float salario) {
        return empleadoRepository.findEmpleadosConSalarioMayorIgual(salario);
    }

    public Empleado obtenerMaxIdEmpleado() {
        return empleadoRepository.obtenerMaxIdEmpleado();
    }

    public Empleado obtenerPorId(Long id) {

        return empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public Empleado add(Empleado empleado) {

        if (empleado.getSalario() >= LIMITE_MIN_SALARIO && empleado.getSalario() <= LIMITE_MAX_SALARIO) {
            return empleadoRepository.save(empleado);
        }else{
            throw new RuntimeException("El salario fuera de rango");
        }
    }

    public Empleado actualizar(Empleado empleado) {
        if (empleado.getSalario() >= LIMITE_MIN_SALARIO && empleado.getSalario() <= LIMITE_MAX_SALARIO) {
            return empleadoRepository.save(empleado);
        }else{
            throw new RuntimeException("El salario fuera de rango");
        }
    }

    public void eliminarPorId(Long id) throws RuntimeException{
        obtenerPorId(id); //Lanza excepción si no existe
        empleadoRepository.deleteById(id);
    }

    public void eliminar(Empleado empleado) {
        empleadoRepository.delete(empleado);
    }

    // BUSCADOR
    public List<Empleado> buscarPorNombre(String textoNombre) {

        // Convertimos todo a minúscula para que no no sea case sensitive
        textoNombre = textoNombre.toLowerCase();

        // Creamos un nuevo arrayList para almacenar los empleados encontrados
        List<Empleado> encontrados = empleadoRepository.findByNombreContainingIgnoreCase(textoNombre);

        return encontrados;
    }

    // BÚSQUEDA POR GÉNERO
    public List<Empleado> buscarPorGenero(Genero genero) {
        List<Empleado> encontrados = empleadoRepository.findByGenero(genero);

        return encontrados;
    }
}
