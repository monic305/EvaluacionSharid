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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Servicio> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Servicio save(Servicio servicio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		
	}


}
