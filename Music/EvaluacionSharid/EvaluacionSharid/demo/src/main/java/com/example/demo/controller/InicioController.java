package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Usuario;
import com.example.demo.model.Profesional;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.ProfesionalService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class InicioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProfesionalService profesionalService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Página de inicio
	@GetMapping("/")
	public String index() {
		return "index";
	}

	// Mostrar página de login
	@GetMapping("/login")
	public String mostrarLogin() {
		return "login";
	}

	// Mostrar página de registro
	@GetMapping("/registro")
	public String mostrarRegistro() {
		return "registro";
	}

	// Registro de usuario/profesional
	@PostMapping("/registro")
	public String registro(@RequestParam("userType") String userType, @RequestParam("nombre") String nombre,
			@RequestParam("email") String email, @RequestParam("telefono") String telefono,
			@RequestParam("password") String password,
			@RequestParam(value = "especialidad", required = false) String especialidad,
			RedirectAttributes redirectAttributes) {

		try {
			// Verificar si el email ya existe
			Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
			if (usuarioExistente.isPresent()) {
				redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
				return "redirect:/registro";
			}

			// Crear usuario
			Usuario usuario = new Usuario();
			usuario.setNombre(nombre);
			usuario.setEmail(email);
			usuario.setTelefono(telefono);
			usuario.setPassword(password);
			usuario.setFechaRegistro(LocalDateTime.now());

			Usuario usuarioGuardado = usuarioService.save(usuario);

			// Si es profesional, crear registro en tabla profesional
			if ("profesional".equals(userType)) {
				Profesional profesional = new Profesional();
				profesional.setEspecialidad(especialidad);
				profesional.setUsuario(usuarioGuardado);
				profesionalService.save(profesional);
			}

			redirectAttributes.addFlashAttribute("success", "Registro exitoso. Por favor inicia sesión");
			return "redirect:/login";

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
			return "redirect:/registro";
		}
	}

	// Login (POST)
	@PostMapping("/login")
	public String login(@RequestParam("username") String email, @RequestParam("password") String password,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
		if (usuarioOpt.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
			return "redirect:/login";
		}

		Usuario usuario = usuarioOpt.get();
		if (!usuario.getPassword().equals(password)) {
			redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta");
			return "redirect:/login";
		}

		// Guardar datos en sesión
		session.setAttribute("usuarioId", usuario.getId());
		session.setAttribute("nombreUsuario", usuario.getNombre());

		// Verificar si es profesional
		if (usuario.getProfesional() != null) {
			session.setAttribute("profesionalId", usuario.getProfesional().getId());
			return "redirect:/profesional/dashboard";
		} else {
			return "redirect:/usuario/home";
		}
	}

	// Logout
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("success", "Sesión cerrada exitosamente");
		return "redirect:/";
	}
}