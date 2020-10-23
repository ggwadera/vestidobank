package com.dh.vestidobank.service;

import com.dh.vestidobank.model.entity.Usuario;
import com.dh.vestidobank.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UsuarioRepository usuarioRepository;

    public Usuario findByUsername(String username) {
        return this.usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + "não encontrado"));
    }
}
