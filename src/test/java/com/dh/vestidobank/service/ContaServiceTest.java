package com.dh.vestidobank.service;

import com.dh.vestidobank.exception.ArgumentNotValidException;
import com.dh.vestidobank.exception.ClienteFalidoException;
import com.dh.vestidobank.exception.ObjectNotFoundException;
import com.dh.vestidobank.model.entity.Conta;
import com.dh.vestidobank.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ContaServiceTest {

    @MockBean
    private ContaRepository contaRepository;

    private ContaService contaService;
    private Conta conta;

    @BeforeEach
    void setUp() {
        this.contaService = new ContaService(contaRepository);
        this.conta = Conta.builder()
            .id(1L)
            .saldo(100.0)
            .limiteEspecial(0)
            .build();
    }

    @Test
    void findByIdSuccessTest() {
        when(contaRepository.findById(any())).thenReturn(Optional.ofNullable(conta));
        Conta c = contaService.findById(conta.getId());
        assertThat(conta).isEqualTo(c);
    }

    @Test
    void findByIdNullTest() {
        assertThrows(ArgumentNotValidException.class, () -> contaService.findById(null));
    }

    @Test
    void findByIdFailTest() {
        when(contaRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> contaService.findById(1L));
    }

    @Test
    void sacarSuccessTest() {
        double saldo = conta.getSaldo();
        double valor = 50.0;
        contaService.sacar(valor, conta);
        assertThat(conta.getSaldo()).isEqualTo(saldo - valor);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void sacarFalidoTest() {
        double valor = conta.getSaldo() + 10;
        assertThrows(ClienteFalidoException.class, () -> contaService.sacar(valor, conta));
        verify(contaRepository, never()).save(any());
    }

    @Test
    void depositarTest() {
        double saldo = conta.getSaldo();
        double valor = 50.0;
        contaService.depositar(valor, conta);
        assertThat(conta.getSaldo()).isEqualTo(saldo + valor);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void transferirTest() {
        Conta destino = Conta.builder().saldo(0).build();
        double valor = conta.getSaldo();
        contaService.transferir(conta.getSaldo(), conta, destino);
        assertThat(conta.getSaldo()).isZero();
        assertThat(destino.getSaldo()).isEqualTo(valor);
        verify(contaRepository, times(2)).save(any(Conta.class));
    }

    @Test
    void alterarLimiteTest() {
        double valor = 100.0;
        contaService.alterarLimite(conta, valor);
        assertThat(conta.getLimiteEspecial()).isEqualTo(valor);
        verify(contaRepository, times(1)).save(conta);
    }

}
