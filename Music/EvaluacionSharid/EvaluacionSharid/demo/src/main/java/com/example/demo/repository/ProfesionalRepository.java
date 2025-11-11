package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Integer> {
}
