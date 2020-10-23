package com.dh.vestidobank.service;

import com.dh.vestidobank.model.dto.auth.Credentials;
import com.dh.vestidobank.model.dto.auth.LoginResponseDTO;
import com.dh.vestidobank.model.entity.Usuario;
import com.dh.vestidobank.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public LoginResponseDTO authenticate(Credentials credentials) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getUsername(),
            credentials.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Usuario user = this.userService.findByUsername(credentials.getUsername());
        final String token = jwtUtil.generateToken(user);
        return LoginResponseDTO.builder().token(token).build();
    }

}
