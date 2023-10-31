package com.example.clinica.service;


import com.example.clinica.model.Odontologo;
import com.example.clinica.model.Paciente;
import com.example.clinica.model.Turno;
import com.example.clinica.repository.OdontologoRepository;
import com.example.clinica.repository.PacienteRepository;
import com.example.clinica.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TurnoService {

    private TurnoRepository turnoRepository;
    private OdontologoRepository odontologoRepository;
    private PacienteRepository pacienteRepository;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, OdontologoRepository odontologoRepository, PacienteRepository pacienteRepository) {
        this.turnoRepository = turnoRepository;
        this.odontologoRepository = odontologoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Turno asignarTurno(Integer matricula, Integer dni, String fechaTurno){
        if (matricula <= 0 || dni <= 0 || fechaTurno == null){
            throw new RuntimeException("Los parametros para asignar un turno no son validos");
        }

        Optional<Odontologo> odontologo = odontologoRepository.findById(matricula);
        Optional<Paciente> paciente = pacienteRepository.findById(dni);
        if(odontologo.isPresent() && paciente.isPresent()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(fechaTurno, formatter);
            Turno turno = new Turno (odontologo.get(), paciente.get(), localDateTime);
            turnoRepository.save(turno);
            return turno;
         }
        return new Turno();
    }

    public Integer sumar (Integer numero1, Integer numero2) {
        if (numero1 < 0 || numero2 < 0) {
            return 0;
        }
        return numero1 + numero2;
    }

    public boolean validarTurnoExistentePorDni(Integer dni, String fechaTurno) {
        if (dni == null || dni <= 0 || fechaTurno == null) {
            throw new RuntimeException("Los parámetros para validar un turno existente no son válidos");
        }

        Optional<Paciente> paciente = pacienteRepository.findById(dni);
        if (paciente.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(fechaTurno, formatter);
            return turnoRepository.existsByPacienteAndFechaTurno(paciente.get(), localDateTime);
        }

        return false;
    }

    public void eliminarTurnoPorDni(Integer dni, LocalDateTime fechaTurno) {
        if (dni == null || dni <= 0 || fechaTurno == null) {
            throw new RuntimeException("Los parámetros para eliminar un turno no son válidos");
        }

        Optional<Paciente> paciente = pacienteRepository.findById(dni);
        if (paciente.isPresent()) {
            Optional<Turno> turno = turnoRepository.findByPacienteAndFechaTurno(paciente.get(), fechaTurno);
            if (turno.isPresent()) {
                turnoRepository.delete(turno.get());
            } else {
                throw new RuntimeException("No se encontró el turno a eliminar");
            }
        } else {
            throw new RuntimeException("No se encontró el paciente con DNI proporcionado");
        }
    }

}
