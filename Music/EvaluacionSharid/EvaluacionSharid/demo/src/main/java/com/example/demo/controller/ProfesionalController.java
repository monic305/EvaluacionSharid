package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Usuario;
import com.example.demo.model.Profesional;
import com.example.demo.model.Servicio;
import com.example.demo.model.Cita;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.ProfesionalService;
import com.example.demo.service.ServicioService;
import com.example.demo.service.CitaService;
import com.example.demo.repository.CitaRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProfesionalService profesionalService;

	@Autowired
	private ServicioService servicioService;

	@Autowired
	private CitaService citaService;

	@Autowired
	private CitaRepository citaRepository;

	// Dashboard profesional
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		String nombreUsuario = (String) session.getAttribute("nombreUsuario");
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");

		if (usuarioId == null || profesionalId == null) {
			return "redirect:/";
		}

		try {
			Profesional profesional = profesionalService.buscarPorId(profesionalId);
			Usuario usuario = usuarioService.buscarPorId(usuarioId);

			// Obtener citas activas (pendientes y confirmadas)
			List<Cita> todasCitas = citaRepository.findByProfesionalIdOrderByFechaHoraDesc(profesionalId);
			todasCitas.removeIf(cita -> "completada".equals(cita.getEstado()) || "cancelada".equals(cita.getEstado()));
			System.out.println("Citas activas del profesional: " + todasCitas.size());

			// Obtener servicios del profesional
			List<Servicio> servicios = servicioService.findByProfesionalId(profesionalId);
			System.out.println("Servicios del profesional: " + servicios.size());

			// Calcular estadísticas
			Map<String, Object> estadisticas = calcularEstadisticas(profesionalId);

			model.addAttribute("usuarioId", usuarioId);
			model.addAttribute("nombreUsuario", nombreUsuario);
			model.addAttribute("profesionalId", profesionalId);
			model.addAttribute("emailUsuario", usuario.getEmail());
			model.addAttribute("telefonoUsuario", usuario.getTelefono());
			model.addAttribute("especializacionProfesional", profesional.getEspecialidad());
			model.addAttribute("citas", todasCitas != null ? todasCitas : List.of());
			model.addAttribute("servicios", servicios != null ? servicios : List.of());
			model.addAttribute("estadisticas", estadisticas);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("citas", List.of());
			model.addAttribute("servicios", List.of());
			Map<String, Object> estadisticas = new HashMap<>();
			estadisticas.put("citasHoy", 0);
			estadisticas.put("citasSemana", 0);
			estadisticas.put("ingresosMes", "$0");
			model.addAttribute("estadisticas", estadisticas);
		}

		return "profesional";
	}

	// Actualizar perfil profesional
	@PostMapping("/actualizar-perfil")
	public String actualizarPerfil(@RequestParam("usuarioId") Integer usuarioId,
			@RequestParam("profesionalId") Integer profesionalId, @RequestParam("nombre") String nombre,
			@RequestParam("email") String email, @RequestParam("telefono") String telefono,
			@RequestParam("especialidad") String especialidad, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Optional<Usuario> usuarioOpt = usuarioService.findById(usuarioId);
		Optional<Profesional> profesionalOpt = profesionalService.findById(profesionalId);

		if (usuarioOpt.isEmpty() || profesionalOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Usuario o profesional no encontrado");
			return "redirect:/profesional/dashboard";
		}

		Usuario usuario = usuarioOpt.get();
		usuario.setNombre(nombre);
		usuario.setEmail(email);
		usuario.setTelefono(telefono);
		usuarioService.save(usuario);

		Profesional profesional = profesionalOpt.get();
		profesional.setEspecialidad(especialidad);
		profesionalService.save(profesional);

		session.setAttribute("nombreUsuario", nombre);

		redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Eliminar cuenta profesional
	@PostMapping("/eliminar-cuenta")
	public String eliminarCuenta(@RequestParam("usuarioId") Integer usuarioId,
			@RequestParam("profesionalId") Integer profesionalId, HttpSession session,
			RedirectAttributes redirectAttributes) {

		profesionalService.delete(profesionalId);
		usuarioService.delete(usuarioId);
		session.invalidate();

		redirectAttributes.addFlashAttribute("success", "Cuenta eliminada exitosamente");
		return "redirect:/";
	}

	// Listar citas del profesional
	@GetMapping("/citas")
	@ResponseBody
	public List<Cita> listarCitas(HttpSession session) {
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");
		if (profesionalId == null) {
			return null;
		}
		List<Cita> todasCitas = citaRepository.findByProfesionalIdOrderByFechaHoraDesc(profesionalId);
		todasCitas.removeIf(cita -> "completada".equals(cita.getEstado()) || "cancelada".equals(cita.getEstado()));
		return todasCitas;
	}

	// Confirmar cita
	@PostMapping("/confirmar-cita")
	public String confirmarCita(@RequestParam("citaId") Integer citaId, RedirectAttributes redirectAttributes) {
		Optional<Cita> citaOpt = citaService.findById(citaId);

		if (citaOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Cita no encontrada");
			return "redirect:/profesional/dashboard";
		}

		Cita cita = citaOpt.get();
		cita.setEstado("confirmada");
		citaService.save(cita);

		redirectAttributes.addFlashAttribute("success", "Cita confirmada exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Cancelar cita
	@PostMapping("/cancelar-cita")
	public String cancelarCita(@RequestParam("citaId") Integer citaId, RedirectAttributes redirectAttributes) {
		Optional<Cita> citaOpt = citaService.findById(citaId);

		if (citaOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Cita no encontrada");
			return "redirect:/profesional/dashboard";
		}

		Cita cita = citaOpt.get();
		cita.setEstado("cancelada");
		citaService.save(cita);

		redirectAttributes.addFlashAttribute("success", "Cita cancelada exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Reprogramar cita
	@PostMapping("/reprogramar-cita")
	public String reprogramarCita(@RequestParam("citaId") Integer citaId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora, RedirectAttributes redirectAttributes) {

		Optional<Cita> citaOpt = citaService.findById(citaId);

		if (citaOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Cita no encontrada");
			return "redirect:/profesional/dashboard";
		}

		Cita cita = citaOpt.get();
		cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
		cita.setEstado("confirmada");
		citaService.save(cita);

		redirectAttributes.addFlashAttribute("success", "Cita reprogramada exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Finalizar cita
	@PostMapping("/finalizar-cita")
	public String finalizarCita(@RequestParam("citaId") Integer citaId, RedirectAttributes redirectAttributes) {
		Optional<Cita> citaOpt = citaService.findById(citaId);

		if (citaOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Cita no encontrada");
			return "redirect:/profesional/dashboard";
		}

		Cita cita = citaOpt.get();
		cita.setEstado("completada");
		citaService.save(cita);

		redirectAttributes.addFlashAttribute("success", "Cita finalizada exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Listar servicios del profesional
	@GetMapping("/servicios")
	@ResponseBody
	public List<Servicio> listarServicios(HttpSession session) {
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");
		if (profesionalId == null) {
			return List.of();
		}
		return servicioService.findByProfesionalId(profesionalId);
	}

	// Crear servicio
	@PostMapping("/crear-servicio")
	public String crearServicio(@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
			@RequestParam("duracion") String duracion, @RequestParam("precio") Double precio, HttpSession session,
			RedirectAttributes redirectAttributes) {

		try {
			Integer profesionalId = (Integer) session.getAttribute("profesionalId");

			if (profesionalId == null) {
				redirectAttributes.addFlashAttribute("error", "Sesión expirada");
				return "redirect:/";
			}

			Optional<Profesional> profesionalOpt = profesionalService.findById(profesionalId);

			if (profesionalOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Profesional no encontrado");
				return "redirect:/profesional/dashboard";
			}

			Servicio servicio = new Servicio();
			servicio.setNombre(nombre);
			servicio.setDescripcion(descripcion);
			servicio.setDuracion(duracion);
			servicio.setPrecio(precio);
			servicio.setProfesional(profesionalOpt.get());

			servicioService.save(servicio);

			redirectAttributes.addFlashAttribute("success", "Servicio creado exitosamente");
			return "redirect:/profesional/dashboard";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al crear el servicio");
			return "redirect:/profesional/dashboard";
		}
	}

	// Actualizar servicio
	@PostMapping("/actualizar-servicio")
	public String actualizarServicio(@RequestParam("id") Integer id, @RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion, @RequestParam("duracion") String duracion,
			@RequestParam("precio") Double precio, RedirectAttributes redirectAttributes) {

		Optional<Servicio> servicioOpt = servicioService.findById(id);

		if (servicioOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
			return "redirect:/profesional/dashboard";
		}

		Servicio servicio = servicioOpt.get();
		servicio.setNombre(nombre);
		servicio.setDescripcion(descripcion);
		servicio.setDuracion(duracion);
		servicio.setPrecio(precio);

		servicioService.save(servicio);

		redirectAttributes.addFlashAttribute("success", "Servicio actualizado exitosamente");
		return "redirect:/profesional/dashboard";
	}

	// Eliminar servicio
	@PostMapping("/eliminar-servicio")
	public String eliminarServicio(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			servicioService.delete(id);
			redirectAttributes.addFlashAttribute("success", "Servicio eliminado exitosamente");
			return "redirect:/profesional/dashboard";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al eliminar el servicio");
			return "redirect:/profesional/dashboard";
		}
	}

	// Listar estadisticas
	@GetMapping("/estadisticas")
	@ResponseBody
	public Map<String, Object> obtenerEstadisticas(HttpSession session) {
		Integer profesionalId = (Integer) session.getAttribute("profesionalId");
		if (profesionalId == null) {
			Map<String, Object> stats = new HashMap<>();
			stats.put("citasHoy", 0);
			stats.put("citasSemana", 0);
			stats.put("ingresosMes", "0");
			return stats;
		}
		return calcularEstadisticas(profesionalId);
	}

	// Obtener estadísticas del dashboard
	private Map<String, Object> calcularEstadisticas(Integer profesionalId) {
		Map<String, Object> stats = new HashMap<>();

		try {
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
				if ("cancelada".equals(cita.getEstado())) {
					continue;
				}

				LocalDateTime fechaCita = cita.getFechaHora();

				if (fechaCita.isAfter(inicioHoy) && fechaCita.isBefore(finHoy)) {
					citasHoy++;
				}

				if (fechaCita.isAfter(inicioSemana)) {
					citasSemana++;
				}

				if ("completada".equals(cita.getEstado()) && fechaCita.isAfter(inicioMes)) {
					ingresosMes += cita.getServicio().getPrecio();
				}
			}

			stats.put("citasHoy", citasHoy);
			stats.put("citasSemana", citasSemana);
			stats.put("ingresosMes", String.format("$%.0f", ingresosMes));
		} catch (Exception e) {
			e.printStackTrace();
			stats.put("citasHoy", 0);
			stats.put("citasSemana", 0);
			stats.put("ingresosMes", "$0");
		}

		return stats;
	}
}