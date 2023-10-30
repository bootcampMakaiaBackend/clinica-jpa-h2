package com.example.clinica.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "matricula")
    private Odontologo odontologo;

    @OneToOne
    @JoinColumn(name = "dni")
    private Paciente paciente;

    @Column
    private LocalDateTime fechaTurno;

    public Turno() {
    }

    public Turno(Odontologo odontologo, Paciente paciente, LocalDateTime fechaTurno) {
        this.odontologo = odontologo;
        this.paciente = paciente;
        this.fechaTurno = fechaTurno;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getFfechaTurno() {
        return fechaTurno;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "odontologo=" + odontologo +
                ", paciente=" + paciente +
                ", fechaHora=" + fechaTurno +
                '}';
    }
}
