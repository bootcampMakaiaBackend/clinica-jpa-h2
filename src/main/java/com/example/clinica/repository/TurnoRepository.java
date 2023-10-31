package com.example.clinica.repository;

import com.example.clinica.model.Paciente;
import com.example.clinica.model.Turno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Integer> {
    boolean existsByPacienteAndFechaTurno(Paciente paciente, LocalDateTime fechaTurno);

    Optional<Turno> findByPacienteAndFechaTurno(Paciente paciente, LocalDateTime fechaTurno);

}
