package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImplement implements UsuarioService {

    @Autowired
    private UsuarioRepository usuariorepository;

    @Override
    public List<Usuario> findAll() {
        return usuariorepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuariorepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Si es nuevo usuario, asigna fecha de registro
        if (usuario.getId() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        return usuariorepository.save(usuario);
    }

    @Override
    public void delete(Integer id) {
        usuariorepository.deleteById(id);
    }
}
