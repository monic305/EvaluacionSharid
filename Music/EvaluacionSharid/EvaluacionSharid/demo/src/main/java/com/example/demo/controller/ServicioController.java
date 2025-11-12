package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Servicio;
import com.example.demo.model.Profesional;
import com.example.demo.service.ServicioService;
import com.example.demo.service.ProfesionalService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/servicio")
public class ServicioController {

	@Autowired
	private ServicioService servicioService;

	@Autowired
	private ProfesionalService profesionalService;

	// Listar todos los servicios (para usuario)
	@GetMapping("/listar")
	@ResponseBody
	public List<Servicio> listarServicios() {
		return servicioService.findAll();
	}

	// Obtener servicio por ID (para mostrar detalles al usuario)
	@GetMapping("/detalle/{id}")
	@ResponseBody
	public Servicio obtenerServicio(@PathVariable Integer id) {
		return servicioService.findById(id).orElse(null);
	}

	// Crear servicio (desde profesional)
	@PostMapping("/crear")
	@ResponseBody
	public String crearServicio(@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
			@RequestParam("duracion") String duracion, @RequestParam("precio") Double precio, HttpSession session) {

		try {
			Integer profesionalId = (Integer) session.getAttribute("profesionalId");

			if (profesionalId == null) {
				return "error_session";
			}

			Profesional profesional = profesionalService.findById(profesionalId).orElse(null);

			if (profesional == null) {
				return "error_profesional";
			}

			Servicio servicio = new Servicio();
			servicio.setNombre(nombre);
			servicio.setDescripcion(descripcion);
			servicio.setDuracion(duracion);
			servicio.setPrecio(precio);
			servicio.setProfesional(profesional);

			servicioService.save(servicio);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Actualizar servicio (desde profesional)
	@PostMapping("/actualizar")
	@ResponseBody
	public String actualizarServicio(@RequestParam("id") Integer id, @RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion, @RequestParam("duracion") String duracion,
			@RequestParam("precio") Double precio) {

		try {
			Servicio servicio = servicioService.findById(id).orElse(null);

			if (servicio == null) {
				return "error";
			}

			servicio.setNombre(nombre);
			servicio.setDescripcion(descripcion);
			servicio.setDuracion(duracion);
			servicio.setPrecio(precio);

			servicioService.save(servicio);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Eliminar servicio (desde profesional)
	@PostMapping("/eliminar")
	@ResponseBody
	public String eliminarServicio(@RequestParam("id") Integer id) {
		try {
			servicioService.delete(id);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Listar servicios del profesional logueado
	@GetMapping("/mis-servicios")
	@ResponseBody
	public List<Servicio> listarMisServicios(HttpSession session) {
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");

		if (profesionalId == null) {
			return null;
		}

		return servicioService.findByProfesionalId(profesionalId);
	}
}