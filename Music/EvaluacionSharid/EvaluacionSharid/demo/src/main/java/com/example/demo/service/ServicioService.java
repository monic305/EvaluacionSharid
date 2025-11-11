package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Servicio;

public interface ServicioService {
    List<Servicio> findAll();
    Optional<Servicio> findById(Integer id);
    Servicio save(Servicio servicio);
    void delete(Integer id);
}
