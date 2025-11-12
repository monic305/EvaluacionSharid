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
import java.util.List;
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

        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("nombreUsuario", nombreUsuario);
        model.addAttribute("profesionalId", profesionalId);

        return "profesional";
    }

    // Actualizar perfil profesional
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("profesionalId") Integer profesionalId,
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("especialidad") String especialidad,
            HttpSession session,
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
    public String eliminarCuenta(
            @RequestParam("usuarioId") Integer usuarioId,
            @RequestParam("profesionalId") Integer profesionalId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        profesionalService.delete(profesionalId);
        usuarioService.delete(usuarioId);
        session.invalidate();

        redirectAttributes.addFlashAttribute("success", "Cuenta eliminada exitosamente");
        return "redirect:/";
    }

    // Listar citas del profesional (AJAX)
    @GetMapping("/citas")
    @ResponseBody
    public List<Cita> listarCitas(HttpSession session) {
        Integer profesionalId = (Integer) session.getAttribute("profesionalId");
        if (profesionalId == null) {
            return null;
        }
        return citaRepository.findByProfesionalId(profesionalId);
    }

    // Cancelar cita
    @PostMapping("/cancelar-cita")
    @ResponseBody
    public String cancelarCita(@RequestParam("citaId") Integer citaId) {
        Optional<Cita> citaOpt = citaService.findById(citaId);
        if (citaOpt.isEmpty()) {
            return "error";
        }

        Cita cita = citaOpt.get();
        cita.setEstado("cancelada");
        citaService.save(cita);

        return "success";
    }

    // Reprogramar cita
    @PostMapping("/reprogramar-cita")
    @ResponseBody
    public String reprogramarCita(
            @RequestParam("citaId") Integer citaId,
            @RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora) {

        Optional<Cita> citaOpt = citaService.findById(citaId);
        if (citaOpt.isEmpty()) {
            return "error";
        }

        Cita cita = citaOpt.get();
        cita.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
        citaService.save(cita);

        return "success";
    }

    // Finalizar cita
    @PostMapping("/finalizar-cita")
    @ResponseBody
    public String finalizarCita(@RequestParam("citaId") Integer citaId) {
        Optional<Cita> citaOpt = citaService.findById(citaId);
        if (citaOpt.isEmpty()) {
            return "error";
        }

        Cita cita = citaOpt.get();
        cita.setEstado("completada");
        citaService.save(cita);

        return "success";
    }

    // Listar servicios del profesional (AJAX)
    @GetMapping("/servicios")
    @ResponseBody
    public List<Servicio> listarServicios() {
        return servicioService.findAll();
    }

    // Crear servicio
    @PostMapping("/crear-servicio")
    @ResponseBody
    public String crearServicio(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("duracion") String duracion,
            @RequestParam("precio") Double precio) {

        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setDuracion(duracion);
        servicio.setPrecio(precio);

        servicioService.save(servicio);

        return "success";
    }

    // Actualizar servicio
    @PostMapping("/actualizar-servicio")
    @ResponseBody
    public String actualizarServicio(
            @RequestParam("id") Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("duracion") String duracion,
            @RequestParam("precio") Double precio) {

        Optional<Servicio> servicioOpt = servicioService.findById(id);
        if (servicioOpt.isEmpty()) {
            return "error";
        }

        Servicio servicio = servicioOpt.get();
        servicio.setNombre(nombre);
        servicio.setDescripcion(descripcion);
        servicio.setDuracion(duracion);
        servicio.setPrecio(precio);

        servicioService.save(servicio);

        return "success";
    }

    // Eliminar servicio
    @PostMapping("/eliminar-servicio")
    @ResponseBody
    public String eliminarServicio(@RequestParam("id") Integer id) {
        servicioService.delete(id);
        return "success";
    }
}