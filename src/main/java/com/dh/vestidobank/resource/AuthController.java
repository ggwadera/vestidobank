package com.dh.vestidobank.resource;

import com.dh.vestidobank.model.dto.auth.Credentials;
import com.dh.vestidobank.model.dto.auth.LoginResponseDTO;
import com.dh.vestidobank.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid Credentials credentials) {
        final LoginResponseDTO loginResponseDTO = authService.authenticate(credentials);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
