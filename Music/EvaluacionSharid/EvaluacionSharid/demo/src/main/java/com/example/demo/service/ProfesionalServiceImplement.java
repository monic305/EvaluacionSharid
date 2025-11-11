package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Profesional;
import com.example.demo.repository.ProfesionalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesionalServiceImplement implements ProfesionalService {

	@Autowired
	private ProfesionalRepository profesionalRepository;

	@Override
	public List<Profesional> findAll() {
		return profesionalRepository.findAll();
	}

	@Override
	public Optional<Profesional> findById(Integer id) {
		return profesionalRepository.findById(id);
	}

	@Override
	public Profesional save(Profesional profesional) {
		return profesionalRepository.save(profesional);
	}

	@Override
	public void delete(Integer id) {
		// Buscar primero el profesional en la base de datos
		Profesional profesional = profesionalRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Profesional no encontrado con id: " + id));

		// 💡 Desvincular el usuario antes de eliminar (esto evita el error
		// TransientObjectException)
		profesional.setUsuario(null);
		profesionalRepository.save(profesional);

		// Ahora sí, eliminar el profesional
		profesionalRepository.deleteById(id);
	}
}
