package com.example.app.services;

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

        @Autowired
        EmpleadoRepository empleadoRepository;



    public List<Empleado> obtenerTodos() {

        //Utilizamos el método del repositorio llamado findAll 
        //Podríamos usar un sort para ordenar las entradas por un campo
        return empleadoRepository.findAll();
    }

    public List<Empleado> obtenerPorSalarioMayor(Float salario) {
        return empleadoRepository.findEmpleadosConSalarioMayorIgual(salario);
    }

    public Empleado obtenerMaxIdEmpleado(){
        return empleadoRepository.obtenerMaxIdEmpleado();
    }

    public Empleado obtenerPorId(Long id) {
        //findById() devuelve un Opctional (una clase que envuelve a 
        //otra clase para evitar nulos) con lo que hay que ajustarlo

        //Lo más sencillo .orElse(null) -> Devuelve un null si no lo encuentras
        //empleadoRepository.findById(id).orElse(null);

        //Lo más correcto, devolver una excepción
        return empleadoRepository.findById(id).orElseThrow(()-> new RuntimeException("Empleado no encontrado"));
    }

    public Empleado add(Empleado empleado) {

        //Solamente debemos cambiar el .add por .save

        // Lanzamos excepción si el salario es inferior a 1800
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
      
        return empleadoRepository.save(empleado); //.save ya nos devuelve un empleado
    }

    public Empleado actualizar(Empleado empleado) {
        // Comprobación de que el salario introducido no es menor a 18.000€,
        // Si es así lanza una excepción
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
        // Ya no tenemos que buscar el Empleado porque directamente busca el id  y actualiza los campos,
        //Si no lo encuentra lo crea

        return empleadoRepository.save(empleado);
    }

    public void eliminarPorId(Long id) {
        //Ya tiene un método propio para borrar por id
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
