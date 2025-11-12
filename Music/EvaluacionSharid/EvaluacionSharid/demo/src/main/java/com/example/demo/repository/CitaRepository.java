package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Cita;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
	List<Cita> findByUsuarioId(Integer usuarioId);

	List<Cita> findByProfesionalId(Integer profesionalId);

	List<Cita> findByUsuarioIdOrderByFechaHoraDesc(Integer usuarioId);

	List<Cita> findByProfesionalIdOrderByFechaHoraDesc(Integer profesionalId);

	List<Cita> findByProfesionalIdAndEstadoNot(Integer profesionalId, String estado);
}
