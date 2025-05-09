package com.example.empleado.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.app.Main;
import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.repositories.EmpleadoRepository;
import com.example.app.services.EmpleadoServiceImplBd;

@SpringBootTest(classes = Main.class)
@TestInstance(Lifecycle.PER_CLASS) // import org.junit.jupiter.api



public class EmpleadoServiceTest {

    @InjectMocks // Clase a testear
    EmpleadoServiceImplBd empleadoService;

    @Mock // Repositorio que queremos falsear
    EmpleadoRepository empleadoRepository;

    // Variables necesarias para los test
    ArrayList<Empleado> mockList;
    Empleado empleadoSinId, empleadoConId;

    // Para las pruebas no se utiliza el repositorio real, ya que no es lo que
    // estamos testeando
    // Por eso creamos un array con Mockito para simular una respuesta y lo cargamos
    // antes de cada test
    @BeforeAll
    public void init() {
        mockList = new ArrayList<>();
        mockList.add(new Empleado(1L, "test1", "test1@gmail.com", 5000f, true, Genero.MASCULINO));
        mockList.add(new Empleado(2L, "test2", "test2@gmail.com", 5000f, true, Genero.FEMENINO));
        mockList.add(new Empleado(3L, "test3", "test3@gmail.com", 5000f, false, Genero.MASCULINO));
        mockList.add(new Empleado(4L, "test4", "test4@gmail.com", 5000f, true, Genero.FEMENINO));

        empleadoSinId = new Empleado(null, "Nombre1", "nombre1@gmail.com", 5000f, true, Genero.MASCULINO);
        empleadoConId = new Empleado(5L, "Nombre1", "nombre1@gmail.com", 5000f, true, Genero.MASCULINO);
    }

    // Test para el método obtenerTodos --> Debe devolver solo los activos
    @Test
    // El Mock de findAll devuelve 4 filas, pero el método obtenerTodos
    // nos debe de devolVer solo los 3 activos.
    public void obtenerTodosTest() {
        // Cuando se llame al método .findAll() del repositorio en su
        // lugar se devolverá la lista de Mockito
        when(empleadoRepository.findAll()).thenReturn(mockList);
        // Guardamos el resultado en la variable empList
        List<Empleado> empList = empleadoService.obtenerTodos();
        // Se espera que el tamaño de la lista sea 3 y se compara
        assertEquals(3, empList.size());
        // Se verifica las veces que se llama al método findAll() del repositorio, debe
        // ser 1
        verify(empleadoRepository, times(1)).findAll();
    }

    // Test para obtener por Id
    @Test
    public void obtenerPorIdTest() {
        // Cuando se llama al repositorio se devuelve nuevamente la lista de Mock
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(mockList.get(0)));
        Empleado empleado = empleadoService.obtenerPorId(1L);
        assertEquals("test1", empleado.getNombre());
        assertEquals("test1@gmail.com", empleado.getEmail());
        assertEquals(5000, empleado.getSalario());

    }

    // Test para añadir Empleado Correcto
    @Test
    public void addTest_Ok() {
        // Simulamos la llamada al repositorio para empleado sinId y lo
        // sustituimos por el empleado conId, ya que es autogenerado realmente.
        when(empleadoRepository.save(empleadoSinId)).thenReturn(empleadoConId);
        Empleado insertado = empleadoService.add(empleadoSinId);
        assertEquals(5L, insertado.getId());
        assertEquals("Nombre1", insertado.getNombre());

        // Se verifica las veces que se llama al método save() del repositorio, debe ser
        // 1
        verify(empleadoRepository, times(1)).save(empleadoSinId);

    }

    // Test para añadir Empleado generando error
    @Test
    public void addTest_Ko() {
        Empleado empleadoErr = new Empleado(null, "err", "err@gmail.com", 100f, false, Genero.FEMENINO);

        // Debe lanzar excepción por no cumplir el límite del salario
        assertThrows(RuntimeException.class, () -> {
            empleadoService.add(empleadoErr);
        });

        // Se verifica las veces que se llama al método save() del repositorio, debe ser
        // 0
        verify(empleadoRepository, times(0)).save(empleadoErr);

    }

    // Test borrado correcto
    @Test
    public void borrarTest_ok() {
        when(empleadoRepository.findById(1L))
                .thenReturn(Optional.of(mockList.get(0)));
        empleadoService.eliminarPorId(1L);
        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).deleteById(1L);
    }

    // Test borrado con error
    @Test
    public void borrarTest_notFound() {
        when(empleadoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            empleadoService.eliminarPorId(999L);
        });

        verify(empleadoRepository, times(1)).findById(999L);
        verify(empleadoRepository, times(0)).deleteById(999L);
    }

}