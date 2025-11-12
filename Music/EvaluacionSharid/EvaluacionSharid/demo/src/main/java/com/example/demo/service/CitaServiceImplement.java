package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Cita;
import com.example.demo.repository.CitaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImplement implements CitaService {

	@Autowired
	private CitaRepository repo;

	@Override
	public List<Cita> findAll() {
		return repo.findAll();
	}

	@Override
	public Optional<Cita> findById(Integer id) {
		return repo.findById(id);
	}

	@Override
	public Cita save(Cita cita) {
		return repo.save(cita);
	}

	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}