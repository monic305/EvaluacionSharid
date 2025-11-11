package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Profesional;

public interface ProfesionalService {
    List<Profesional> findAll();
    Optional<Profesional> findById(Integer id);
    Profesional save(Profesional profesional);
    void delete(Integer id);
}
