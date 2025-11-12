package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Usuario;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    void delete(Integer id);
	Usuario buscarPorId(Integer usuarioId);
}
