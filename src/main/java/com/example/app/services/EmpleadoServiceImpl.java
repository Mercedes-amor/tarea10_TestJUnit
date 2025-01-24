package com.example.app.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    List<Empleado> empleadoRepository = new ArrayList<>();

    public List<Empleado> obtenerTodos() {

        //Añadimos un Collections.sort para que devuelva un ArrayList con los 
        //Empleados ordenados alfabéticamente  
        Collections.sort(empleadoRepository, Comparator.comparing(Empleado::getNombre, String::compareToIgnoreCase));

        return empleadoRepository;
    }

    public List<Empleado> obtenerPorSalarioMayor(Double salario) {
        List<Empleado> empleados = new ArrayList<>();
        for (Empleado empleado : empleadoRepository) {
            if (empleado.getSalario() > salario) {
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    public Empleado obtenerPorId(Long id) {
        for (Empleado empleado : empleadoRepository) {
            if (empleado.getId().equals(id)) {
                return empleado;
            }
        }
        // Lanzamos una excepción al no encontrar el empleado buscado por id
        throw new RuntimeException("Empleado no encontrado");
    }

    public Empleado add(Empleado empleado) {

        // Lanzamos excepción si el salario es inferior a 1800
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
        // Comprobamos si ya existe ese empleado, la forma de comparar dos instancias de
        // Empleado es por el id
        // Si encuentra el mismo id indicará que ya existe ese empleado
        int pos = empleadoRepository.indexOf(empleado);
        if (pos != -1)
            throw new RuntimeException("Ya existe un empleado con ese id");

        empleadoRepository.add(empleado);
        return empleado;
    }

    public Empleado actualizar(Empleado empleado) {
        // Comprobación de que el salario introducido no es menor a 18.000€,
        // Si es así lanza una excepción
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
        // Aquí no comprobamos si ya existe el mismo id porque efectivamente tiene que
        // existir, ya que estamos actualizando un Empleado.
        // Bloqueamos la opción de poder modificar el id en el formulario de editar
        // if (pos != -1)
        // throw new RuntimeException("Ya existe un empleado con ese id");

        int pos = empleadoRepository.indexOf(empleado);
        empleadoRepository.set(pos, empleado);
        return empleado;
    }

    public void eliminarPorId(Long id) {
        Empleado empleado = obtenerPorId(id);
        if (empleado != null) {
            empleadoRepository.remove(empleado);
        }
    }

    public void eliminar(Empleado empleado) {
        empleadoRepository.remove(empleado);
    }

    // BUSCADOR
    public List<Empleado> buscarPorNombre(String textoNombre) {
        // Convertimos todo a minúscula para que no no sea case sensitive
        textoNombre = textoNombre.toLowerCase();
        // Creamos un nuevo arrayList para almacenar los empleados encontrados
        List<Empleado> encontrados = new ArrayList<>();
        // Comparacion con .contains para comprobar si el nombre contiene el texto
        // buscado
        for (Empleado empleado : empleadoRepository)
            if (empleado.getNombre().toLowerCase().contains(textoNombre))
                // Si lo encuentra lo añade al arrayList
                encontrados.add(empleado);
        return encontrados;
    }

    // BÚSQUEDA POR GÉNERO
    public List<Empleado> buscarPorGenero(Genero genero) {
        List<Empleado> encontrados = new ArrayList<>();
        for (Empleado empleado : empleadoRepository)
            if (empleado.getGenero() == genero)
                encontrados.add(empleado);
        return encontrados;
    }
}
