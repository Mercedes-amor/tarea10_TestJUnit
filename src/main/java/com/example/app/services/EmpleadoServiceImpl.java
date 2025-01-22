package com.example.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Empleado;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    List<Empleado> empleadoRepository = new ArrayList<>();

    public List<Empleado> obtenerTodos() {
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
        return null;
    }

    public Empleado add(Empleado empleado) {
        empleadoRepository.add(empleado);
        return empleado;
    }

    public Empleado actualizar(Empleado empleado) {
        int pos = empleadoRepository.indexOf(empleado);
        if (pos == -1)
            return null;
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

}
