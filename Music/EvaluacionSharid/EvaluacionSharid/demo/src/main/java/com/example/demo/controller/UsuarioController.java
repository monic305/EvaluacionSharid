package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Usuario;
import com.example.demo.model.Cita;
import com.example.demo.model.Servicio;
import com.example.demo.model.Profesional;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.CitaService;
import com.example.demo.service.ServicioService;
import com.example.demo.service.ProfesionalService;
import com.example.demo.repository.CitaRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CitaService citaService;

	@Autowired
	private ServicioService servicioService;

	@Autowired
	private ProfesionalService profesionalService;

	@Autowired
	private CitaRepository citaRepository;

	// Página principal de usuario
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		String nombreUsuario = (String) session.getAttribute("nombreUsuario");

		if (usuarioId == null) {
			return "redirect:/";
		}

		try {
			Usuario usuario = usuarioService.buscarPorId(usuarioId);

			// Obtener todos los servicios para mostrar en la vista
			List<Servicio> servicios = servicioService.findAll();
			System.out.println("Servicios encontrados: " + servicios.size());

			// Obtener las citas del usuario
			List<Cita> citas = citaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
			System.out.println("Citas encontradas: " + citas.size());

			model.addAttribute("usuarioId", usuario.getId());
			model.addAttribute("nombreUsuario", usuario.getNombre());
			model.addAttribute("emailUsuario", usuario.getEmail());
			model.addAttribute("telefonoUsuario", usuario.getTelefono());
			model.addAttribute("servicios", servicios != null ? servicios : List.of());
			model.addAttribute("citas", citas != null ? citas : List.of());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("servicios", List.of());
			model.addAttribute("citas", List.of());
		}

		return "usuario";
	}

	// Actualizar perfil
	@PostMapping("/actualizar-perfil")
	public String actualizarPerfil(@RequestParam("id") Integer id, @RequestParam("nombre") String nombre,
			@RequestParam("email") String email, @RequestParam("telefono") String telefono, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Optional<Usuario> usuarioOpt = usuarioService.findById(id);

		if (usuarioOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
			return "redirect:/usuario/home";
		}

		Usuario usuario = usuarioOpt.get();
		usuario.setNombre(nombre);
		usuario.setEmail(email);
		usuario.setTelefono(telefono);

		usuarioService.save(usuario);

		// actualizar sesión
		session.setAttribute("nombreUsuario", nombre);

		redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
		return "redirect:/usuario/home";
	}

	// Eliminar cuenta
	@PostMapping("/eliminar-cuenta")
	public String eliminarCuenta(@RequestParam("id") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {

		usuarioService.delete(id);
		session.invalidate();

		redirectAttributes.addFlashAttribute("success", "Cuenta eliminada exitosamente");
		return "redirect:/";
	}

	// Listar servicios
	@GetMapping("/servicios")
	@ResponseBody
	public List<Servicio> listarServicios() {
		return servicioService.findAll();
	}

	// Listar profesionales
	@GetMapping("/profesionales")
	@ResponseBody
	public List<Profesional> listarProfesionales() {
		return profesionalService.findAll();
	}

	// Listar profesionales por servicio
	@GetMapping("/profesionales/{servicioId}")
	@ResponseBody
	public List<Profesional> listarProfesionalesPorServicio(@PathVariable Integer servicioId) {
		Optional<Servicio> servicioOpt = servicioService.findById(servicioId);
		if (servicioOpt.isPresent()) {
			Servicio servicio = servicioOpt.get();
			Profesional profesional = servicio.getProfesional();
			return List.of(profesional);
		}
		return List.of();
	}

	// Obtener detalles de un servicio específico
	@GetMapping("/servicio/{servicioId}")
	@ResponseBody
	public Servicio obtenerServicio(@PathVariable Integer servicioId) {
		Optional<Servicio> servicioOpt = servicioService.findById(servicioId);
		return servicioOpt.orElse(null);
	}

	// Listar citas del usuario
	@GetMapping("/mis-citas")
	@ResponseBody
	public List<Cita> listarCitas(HttpSession session) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		if (usuarioId == null) {
			return null;
		}
		return citaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
	}

	@GetMapping("/citas")
	@ResponseBody
	public List<Cita> listarMisCitas(HttpSession session) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		if (usuarioId == null) {
			return null;
		}
		return citaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
	}

	// Agendar cita
	@PostMapping("/agendar-cita")
	public String agendarCita(@RequestParam("servicioId") Integer servicioId,
			@RequestParam("profesionalId") Integer profesionalId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora, HttpSession session, RedirectAttributes redirectAttributes) {

		try {
			Integer usuarioId = (Integer) session.getAttribute("usuarioId");

			if (usuarioId == null) {
				redirectAttributes.addFlashAttribute("error", "Sesión expirada");
				return "redirect:/";
			}

			Optional<Usuario> usuarioOpt = usuarioService.findById(usuarioId);
			Optional<Servicio> servicioOpt = servicioService.findById(servicioId);
			Optional<Profesional> profesionalOpt = profesionalService.findById(profesionalId);

			if (usuarioOpt.isEmpty() || servicioOpt.isEmpty() || profesionalOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Datos inválidos");
				return "redirect:/usuario/home";
			}

			Cita cita = new Cita();
			cita.setUsuario(usuarioOpt.get());
			cita.setServicio(servicioOpt.get());
			cita.setProfesional(profesionalOpt.get());
			cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
			cita.setEstado("pendiente");

			citaService.save(cita);

			redirectAttributes.addFlashAttribute("success", "Cita agendada exitosamente");
			return "redirect:/usuario/home";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al agendar la cita");
			return "redirect:/usuario/home";
		}
	}

	// Cancelar cita desde el usuario
	// Cancelar cita desde el usuario - ACTUALIZADO
	@PostMapping("/cancelar-cita")
	public String cancelarCita(@RequestParam("citaId") Integer citaId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer usuarioId = (Integer) session.getAttribute("usuarioId");

			if (usuarioId == null) {
				redirectAttributes.addFlashAttribute("error", "Sesión expirada");
				return "redirect:/";
			}

			Optional<Cita> citaOpt = citaService.findById(citaId);

			if (citaOpt.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Cita no encontrada");
				return "redirect:/usuario/home";
			}

			Cita cita = citaOpt.get();

			if (!cita.getUsuario().getId().equals(usuarioId)) {
				redirectAttributes.addFlashAttribute("error", "No tienes permiso para cancelar esta cita");
				return "redirect:/usuario/home";
			}

			cita.setEstado("cancelada");
			citaService.save(cita);

			redirectAttributes.addFlashAttribute("success", "Cita cancelada exitosamente");
			return "redirect:/usuario/home";
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al cancelar la cita");
			return "redirect:/usuario/home";
		}
	}

}