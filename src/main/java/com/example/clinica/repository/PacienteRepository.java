package com.example.clinica.repository;

import com.example.clinica.model.Paciente;
import com.example.clinica.model.Turno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PacienteRepository extends CrudRepository<Paciente, Integer> {



}
