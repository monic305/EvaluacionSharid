package com.example.demo.service;

import com.example.demo.model.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {
	List<Servicio> findAll();

	Optional<Servicio> findById(Integer id);

	List<Servicio> findByProfesionalId(Integer profesionalId);

	Servicio save(Servicio servicio);

	void delete(Integer id);
}