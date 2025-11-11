package com.example.demo.controller;

import com.example.demo.model.Profesional;
import com.example.demo.model.Usuario;
import com.example.demo.service.ProfesionalService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/profesionales")
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos los profesionales
    @GetMapping
    public String listarProfesionales(Model model) {
        model.addAttribute("profesionales", profesionalService.findAll());
        model.addAttribute("profesional", new Profesional());
        model.addAttribute("usuarios", usuarioService.findAll()); // Para dropdown
        return "profesionales"; // Nombre del template HTML
    }

    // Guardar o actualizar profesional
    @PostMapping("/guardar")
    public String guardarProfesional(@ModelAttribute Profesional profesional,
                                     @RequestParam("usuarioId") Integer usuarioId) {

        // Buscar el usuario en BD
        Usuario usuario = usuarioService.findById(usuarioId).orElse(null);
        profesional.setUsuario(usuario);

        profesionalService.save(profesional);
        return "redirect:/profesionales";
    }

    // Editar profesional (opcional, si quieres una ruta aparte)
    @GetMapping("/editar/{id}")
    public String editarProfesional(@PathVariable Integer id, Model model) {
        Optional<Profesional> profesionalOpt = profesionalService.findById(id);
        if (profesionalOpt.isPresent()) {
            model.addAttribute("profesional", profesionalOpt.get());
            model.addAttribute("profesionales", profesionalService.findAll());
            model.addAttribute("usuarios", usuarioService.findAll());
            return "profesionales";
        }
        return "redirect:/profesionales";
    }

    // Eliminar profesional
    @GetMapping("/eliminar/{id}")
    public String eliminarProfesional(@PathVariable Integer id) {
        profesionalService.delete(id);
        return "redirect:/profesionales";
    }
}
