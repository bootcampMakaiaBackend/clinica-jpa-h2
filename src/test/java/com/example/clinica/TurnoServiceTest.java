package com.example.clinica;

import com.example.clinica.model.Odontologo;
import com.example.clinica.model.Paciente;
import com.example.clinica.model.Turno;
import com.example.clinica.repository.OdontologoRepository;
import com.example.clinica.repository.PacienteRepository;
import com.example.clinica.repository.TurnoRepository;
import com.example.clinica.service.TurnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TurnoServiceTest {
    /*
     1- mirar cuales son las dependencias de mi clase que voy a testear.
     donde lo miro? en el constructor.
     2- Agregar dependencias en la clase test.
     */
    private PacienteRepository pacienteRepository;
    private TurnoRepository turnoRepository;
    private OdontologoRepository odontologoRepository;
    private TurnoService turnoService;

    // 3 -Crear la instancia de la clase que yo voy a testear.
    //Constructor
    @BeforeEach
    public  void setUp() {
        // mock - que es crear una clase falsa y emular su comportamiento.
        this.turnoRepository = mock(TurnoRepository.class);
        this.odontologoRepository = mock(OdontologoRepository.class);
        this.pacienteRepository = mock(PacienteRepository.class);
        this.turnoService = new TurnoService(this.turnoRepository, this.odontologoRepository, this.pacienteRepository);
    }

    @Test
    public void sumarDosNumerosPositivos(){
        //Arrange
        Integer numero1 = 3;
        Integer numero2 = 1;
        //Act
        Integer resultado = this.turnoService.sumar(numero1, numero2);
        //Assert
        assertEquals(4, resultado);
    }

    @Test
    public void sumarDosNumerosNegativos(){
        //Arrange
        Integer numero1 = -3;
        Integer numero2 = -1;
        //Act
        Integer resultado = this.turnoService.sumar(numero1, numero2);
        //Assert
        assertEquals(0, resultado);
    }

    @Test
    public void sumarDosNumerosCuandoUnoEsNegativo(){
        //Arrange
        Integer numero1 = 3;
        Integer numero2 = -1;
        //Act
        Integer resultado = this.turnoService.sumar(numero1, numero2);
        //Assert
        assertEquals(0, resultado);
    }

    @Test
    public void asignarTurnoConFechaNula(){
        //Arrange
        Integer matricula = 123;
        Integer dni = 345;
        String fecha = null;
        //act , assert
        assertThrows(RuntimeException.class,
                () -> this.turnoService.asignarTurno(matricula, dni, fecha));
    }

    @Test
    public void asignarTurnoConDniNegativo(){
        //Arrange
        Integer matricula = 123;
        Integer dni = -345;
        String fecha = "2012-10-23";
        //act , assert
        assertThrows(RuntimeException.class,
                () -> this.turnoService.asignarTurno(matricula, dni, fecha));
    }

    @Test
    public void asignarTurnoConMatriculaNegativo(){
        //Arrange
        Integer matricula = -123;
        Integer dni = 345;
        String fecha = "2012-10-23";
        //act , assert
        assertThrows(RuntimeException.class,
                () -> this.turnoService.asignarTurno(matricula, dni, fecha));
    }

    @Test
    public void asignarTurnoOdontologoNoExisteEnDB(){
        //Arrange
        Integer matricula = 123;
        Integer dni = 345;
        String fecha = "2012-10-23";
        when(odontologoRepository.findById(any())).thenReturn(Optional.empty());
        // act
        Turno turno = this.turnoService.asignarTurno(matricula, dni, fecha);
        // assert
        assertTrue(turno.getOdontologo() == null);
    }

    @Test
    public void asignarTurnoPacienteNoExisteEnDB(){
        //Arrange
        Integer matricula = 123;
        Integer dni = 345;
        String fecha = "2012-10-23";
        when(pacienteRepository.findById(any())).thenReturn(Optional.empty());

        // act
        Turno turno = this.turnoService.asignarTurno(matricula, dni, fecha);
        // assert
        assertTrue(turno.getPaciente() == null);
    }

    @Test
    public void asignarTurnoConFechaSinPatronCorrecto() {
        //Arrange
        Integer matricula = 123;
        Integer dni = 345;
        String fecha = "2012-10-23";
        Paciente paciente = new Paciente("Juan", "perez", dni, "asd");
        Odontologo odontologo = new Odontologo(matricula, "Ana", "Ramirez");
        when(odontologoRepository.findById(any())).thenReturn(Optional.of(odontologo));
        when(pacienteRepository.findById(any())).thenReturn(Optional.of(paciente));
        // act
         assertThrows(DateTimeParseException.class,
                 () -> this.turnoService.asignarTurno(matricula, dni, fecha));

    }

    @Test
    public void asignarTurno() {
        //Arrange
        Integer matricula = 123;
        Integer dni = 345;
        String fecha = "2012-10-23 13:10";
        Paciente paciente = new Paciente("Juan", "perez", dni, "asd");
        Odontologo odontologo = new Odontologo(matricula, "Ana", "Ramirez");
        when(odontologoRepository.findById(any())).thenReturn(Optional.of(odontologo));
        when(pacienteRepository.findById(any())).thenReturn(Optional.of(paciente));
        // act
        Turno turno = this.turnoService.asignarTurno(matricula, dni, fecha);
        // assert
        verify(turnoRepository, times(1)).save(any());
    }

}
