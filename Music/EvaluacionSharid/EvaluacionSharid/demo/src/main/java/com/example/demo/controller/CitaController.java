package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Cita;
import com.example.demo.model.Usuario;
import com.example.demo.model.Profesional;
import com.example.demo.model.Servicio;
import com.example.demo.service.CitaService;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.ProfesionalService;
import com.example.demo.service.ServicioService;
import com.example.demo.repository.CitaRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cita")
public class CitaController {

	@Autowired
	private CitaService citaService;

	@Autowired
	private CitaRepository citaRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProfesionalService profesionalService;

	@Autowired
	private ServicioService servicioService;

	// Agendar cita (desde usuario)
	@PostMapping("/agendar")
	@ResponseBody
	public String agendarCita(@RequestParam("servicioId") Integer servicioId,
			@RequestParam("profesionalId") Integer profesionalId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora, HttpSession session) {

		try {
			Integer usuarioId = (Integer) session.getAttribute("usuarioId");

			if (usuarioId == null) {
				return "error_session";
			}

			Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
			Servicio servicio = servicioService.findById(servicioId).orElse(null);
			Profesional profesional = profesionalService.findById(profesionalId).orElse(null);

			if (usuario == null || servicio == null || profesional == null) {
				return "error_data";
			}

			Cita cita = new Cita();
			cita.setUsuario(usuario);
			cita.setServicio(servicio);
			cita.setProfesional(profesional);
			cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
			cita.setEstado("pendiente");

			citaService.save(cita);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Listar citas del usuario (todas las que no estén eliminadas)
	@GetMapping("/usuario/listar")
	@ResponseBody
	public List<Cita> listarCitasUsuario(HttpSession session) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");

		if (usuarioId == null) {
			return null;
		}

		return citaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
	}

	// Listar citas del profesional (solo activas: pendientes y confirmadas)
	@GetMapping("/profesional/listar")
	@ResponseBody
	public List<Cita> listarCitasProfesional(HttpSession session) {
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");

		if (profesionalId == null) {
			return null;
		}

		// Obtener todas las citas que NO estén completadas ni canceladas
		List<Cita> todasCitas = citaRepository.findByProfesionalIdOrderByFechaHoraDesc(profesionalId);
		todasCitas.removeIf(cita -> "completada".equals(cita.getEstado()) || "cancelada".equals(cita.getEstado()));

		return todasCitas;
	}

	// Confirmar cita (cambia de pendiente a confirmada)
	@PostMapping("/confirmar")
	@ResponseBody
	public String confirmarCita(@RequestParam("citaId") Integer citaId) {
		try {
			Cita cita = citaService.findById(citaId).orElse(null);

			if (cita == null) {
				return "error";
			}

			cita.setEstado("confirmada");
			citaService.save(cita);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Cancelar cita (desde profesional o usuario)
	@PostMapping("/cancelar")
	@ResponseBody
	public String cancelarCita(@RequestParam("citaId") Integer citaId) {
		try {
			Cita cita = citaService.findById(citaId).orElse(null);

			if (cita == null) {
				return "error";
			}

			cita.setEstado("cancelada");
			citaService.save(cita);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Reprogramar cita (desde profesional)
	@PostMapping("/reprogramar")
	@ResponseBody
	public String reprogramarCita(@RequestParam("citaId") Integer citaId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora) {

		try {
			Cita cita = citaService.findById(citaId).orElse(null);

			if (cita == null) {
				return "error";
			}

			cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
			cita.setEstado("confirmada");
			citaService.save(cita);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Finalizar cita (cambia estado a completada - se oculta de la vista pero sigue
	// en BD)
	@PostMapping("/finalizar")
	@ResponseBody
	public String finalizarCita(@RequestParam("citaId") Integer citaId) {
		try {
			Cita cita = citaService.findById(citaId).orElse(null);

			if (cita == null) {
				return "error";
			}

			cita.setEstado("completada");
			citaService.save(cita);

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// Obtener estadísticas para el dashboard del profesional
	@GetMapping("/profesional/estadisticas")
	@ResponseBody
	public Map<String, Object> obtenerEstadisticas(HttpSession session) {
		Map<String, Object> stats = new HashMap<>();

		try {
			Integer profesionalId = (Integer) session.getAttribute("profesionalId");

			if (profesionalId == null) {
				stats.put("citasHoy", 0);
				stats.put("citasSemana", 0);
				stats.put("ingresosMes", "0");
				return stats;
			}

			List<Cita> todasLasCitas = citaRepository.findByProfesionalIdOrderByFechaHoraDesc(profesionalId);

			int citasHoy = 0;
			int citasSemana = 0;
			double ingresosMes = 0;

			LocalDateTime ahora = LocalDateTime.now();
			LocalDateTime inicioHoy = ahora.toLocalDate().atStartOfDay();
			LocalDateTime finHoy = inicioHoy.plusDays(1);
			LocalDateTime inicioSemana = ahora.minusDays(7);
			LocalDateTime inicioMes = ahora.minusMonths(1);

			for (Cita cita : todasLasCitas) {
				// No contar citas canceladas en las estadísticas
				if ("cancelada".equals(cita.getEstado())) {
					continue;
				}

				LocalDateTime fechaCita = cita.getFechaHora();

				// Contar citas de hoy (pendientes y confirmadas)
				if (fechaCita.isAfter(inicioHoy) && fechaCita.isBefore(finHoy)) {
					citasHoy++;
				}

				// Contar citas de la semana (pendientes y confirmadas)
				if (fechaCita.isAfter(inicioSemana)) {
					citasSemana++;
				}

				// Sumar ingresos del mes (solo citas completadas)
				if ("completada".equals(cita.getEstado()) && fechaCita.isAfter(inicioMes)) {
					ingresosMes += cita.getServicio().getPrecio();
				}
			}

			stats.put("citasHoy", citasHoy);
			stats.put("citasSemana", citasSemana);
			stats.put("ingresosMes", String.format("$%.0f", ingresosMes));

			return stats;
		} catch (Exception e) {
			e.printStackTrace();
			stats.put("citasHoy", 0);
			stats.put("citasSemana", 0);
			stats.put("ingresosMes", "0");
			return stats;
		}
	}
}