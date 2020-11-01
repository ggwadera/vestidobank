package com.dh.vestidobank.service;

import com.dh.vestidobank.exception.ArgumentNotValidException;
import com.dh.vestidobank.exception.DataIntegrityException;
import com.dh.vestidobank.exception.ObjectNotFoundException;
import com.dh.vestidobank.model.entity.Cliente;
import com.dh.vestidobank.model.entity.Conta;
import com.dh.vestidobank.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
public class ClienteServiceTest {

    @MockBean
    private ContaService contaService;

    @MockBean
    private GerenteService gerenteService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private ClienteRepository clienteRepository;

    private ClienteService clienteService;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        this.clienteService = new ClienteService(clienteRepository, contaService, gerenteService, passwordEncoder);
        Conta conta = Conta.builder()
            .id(1L)
            .saldo(100.0)
            .limiteEspecial(0)
            .build();
        this.cliente = Cliente.builder()
            .id(1L)
            .ativo(false)
            .conta(conta)
            .nome("Nome Teste")
            .endereco("Endereço de Teste")
            .build();
    }

    @Test
    void findByIdSuccessTest() {
        when(clienteRepository.findById(any())).thenReturn(Optional.ofNullable(cliente));
        Cliente c = clienteService.findById(cliente.getId());
        assertThat(cliente).isEqualTo(c);
    }

    @Test
    void findByIdNullTest() {
        assertThrows(ArgumentNotValidException.class, () -> clienteService.findById(null));
    }

    @Test
    void findByIdFailTest() {
        when(clienteRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> clienteService.findById(1L));
    }

    @Test
    void ativarTest() {
        when(clienteRepository.findById(any())).thenReturn(Optional.ofNullable(cliente));
        clienteService.ativar(cliente.getId());
        assertThat(cliente.isAtivo()).isTrue();
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void ativarFailTest() {
        cliente.setAtivo(true);
        when(clienteRepository.findById(any())).thenReturn(Optional.ofNullable(cliente));
        assertThrows(DataIntegrityException.class, () -> clienteService.ativar(cliente.getId()));
        verify(clienteRepository, never()).save(any());
    }

}
