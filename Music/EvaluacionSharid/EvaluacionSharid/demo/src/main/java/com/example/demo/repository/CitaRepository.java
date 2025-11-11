package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cita;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByUsuarioId(Integer usuarioId);
    List<Cita> findByProfesionalId(Integer profesionalId);
}
