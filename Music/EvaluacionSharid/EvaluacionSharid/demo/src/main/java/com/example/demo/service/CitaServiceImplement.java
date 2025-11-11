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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Cita> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Cita save(Cita cita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}


}
