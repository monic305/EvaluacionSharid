package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Servicio;
import com.example.demo.repository.ServicioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImplement implements ServicioService {

	@Autowired
	private ServicioRepository serviciorepository;

	@Override
	public List<Servicio> findAll() {
		return serviciorepository.findAll();
	}

	@Override
	public Optional<Servicio> findById(Integer id) {
		return serviciorepository.findById(id);
	}

	@Override
	public List<Servicio> findByProfesionalId(Integer profesionalId) {
		return serviciorepository.findByProfesionalId(profesionalId);
	}

	@Override
	public Servicio save(Servicio servicio) {
		return serviciorepository.save(servicio);
	}

	@Override
	public void delete(Integer id) {
		serviciorepository.deleteById(id);
	}
}