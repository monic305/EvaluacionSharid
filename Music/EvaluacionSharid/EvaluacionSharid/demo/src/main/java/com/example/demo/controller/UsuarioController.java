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

		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("nombreUsuario", nombreUsuario);

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

		// Actualizar sesión
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

	// Listar servicios (AJAX)
	@GetMapping("/servicios")
	@ResponseBody
	public List<Servicio> listarServicios() {
		return servicioService.findAll();
	}

	// Listar profesionales por servicio (AJAX)
	@GetMapping("/profesionales/{servicioId}")
	@ResponseBody
	public List<Profesional> listarProfesionales(@PathVariable Integer servicioId) {
		return profesionalService.findAll();
	}

	// Agendar cita
	@PostMapping("/agendar-cita")
	@ResponseBody
	public String agendarCita(@RequestParam("servicioId") Integer servicioId,
			@RequestParam("profesionalId") Integer profesionalId, @RequestParam("fecha") String fecha,
			@RequestParam("hora") String hora, HttpSession session) {

		Integer usuarioId = (Integer) session.getAttribute("usuarioId");

		if (usuarioId == null) {
			return "error";
		}

		Optional<Usuario> usuarioOpt = usuarioService.findById(usuarioId);
		Optional<Servicio> servicioOpt = servicioService.findById(servicioId);
		Optional<Profesional> profesionalOpt = profesionalService.findById(profesionalId);

		if (usuarioOpt.isEmpty() || servicioOpt.isEmpty() || profesionalOpt.isEmpty()) {
			return "error";
		}

		Cita cita = new Cita();
		cita.setUsuario(usuarioOpt.get());
		cita.setServicio(servicioOpt.get());
		cita.setProfesional(profesionalOpt.get());
		cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
		cita.setEstado("pendiente");

		citaService.save(cita);

		return "success";
	}

	// Listar citas del usuario (AJAX)
	@GetMapping("/mis-citas")
	@ResponseBody
	public List<Cita> listarCitas(HttpSession session) {
		Integer usuarioId = (Integer) session.getAttribute("usuarioId");
		if (usuarioId == null) {
			return null;
		}
		return citaRepository.findByUsuarioId(usuarioId);
	}
}