package com.dh.vestidobank.resource;

import com.dh.vestidobank.model.dto.ValorOperacao;
import com.dh.vestidobank.model.dto.create.ClienteCreateDTO;
import com.dh.vestidobank.model.dto.update.ClienteUpdateDTO;
import com.dh.vestidobank.model.entity.Cliente;
import com.dh.vestidobank.model.entity.Usuario;
import com.dh.vestidobank.service.ClienteService;
import com.dh.vestidobank.service.GerenteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PatchMapping("/{id}/alterarLimite")
    public ResponseEntity<Void> alterarLimite(@PathVariable Long id, @Valid @RequestBody ValorOperacao valor, @AuthenticationPrincipal Usuario usuario) {
        this.clienteService.alterarLimite(id, valor, usuario.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        this.clienteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/sacar")
    public ResponseEntity<Void> sacar(@Valid @RequestBody ValorOperacao valor, @AuthenticationPrincipal Usuario usuario) {
        this.clienteService.sacar(valor, usuario.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/depositar")
    public ResponseEntity<Void> depositar(@Valid @RequestBody ValorOperacao valor, @AuthenticationPrincipal Usuario usuario) {
        this.clienteService.depositar(valor, usuario.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/transferir/{cpf}")
    public ResponseEntity<Void> transferir(
        @PathVariable String cpf,
        @Valid @RequestBody ValorOperacao valor,
        @AuthenticationPrincipal Usuario usuario) {
        this.clienteService.transferir(valor, usuario.getUsername(), cpf);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ClienteCreateDTO cliente) {
        Cliente savedCliente = this.clienteService.create(cliente);
        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedCliente.getId())
            .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @Valid @RequestBody ClienteUpdateDTO cliente) {
        cliente.setId(id);
        this.clienteService.update(cliente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        Cliente cliente = this.clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }


    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> allCliente = this.clienteService.findAll();
        return ResponseEntity.ok(allCliente);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        this.clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
