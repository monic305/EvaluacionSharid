package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
	List<Servicio> findByProfesionalId(Integer profesionalId);
}