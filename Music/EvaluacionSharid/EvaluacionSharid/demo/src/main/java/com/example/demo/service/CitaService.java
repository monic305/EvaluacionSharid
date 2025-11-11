package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Cita;

public interface CitaService {
    List<Cita> findAll();
    Optional<Cita> findById(Integer id);
    Cita save(Cita cita);
    void delete(Integer id);
}
