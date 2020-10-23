package com.dh.vestidobank.service;

import com.dh.vestidobank.exception.ArgumentNotValidException;
import com.dh.vestidobank.exception.ClienteFalidoException;
import com.dh.vestidobank.exception.ObjectNotFoundException;
import com.dh.vestidobank.model.entity.Conta;
import com.dh.vestidobank.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContaService {


    private final ContaRepository contaRepository;

    public List<Conta> findAll() {
        return this.contaRepository.findAll();
    }


    public Conta findById(Long id) {
        Optional
            .ofNullable(id)
            .orElseThrow(() -> new ArgumentNotValidException("Id não pode ser nulo"));
        return this.contaRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Conta de id " + id + " não encontrado"));
    }


    @Transactional
    public void sacar(double valor, Conta conta) {
        if (conta.valorDisponivelSaque(valor)) {
            conta.sacar(valor);
        } else {
            throw new ClienteFalidoException("É bom trabalhar mais");
        }
        this.contaRepository.save(conta);

    }

    @Transactional
    public void depositar(double valor, Conta conta) {
        conta.depositar(valor);
        this.contaRepository.save(conta);
    }

    @Transactional
    public void transferir(double valor, Conta contaOrigem, Conta contaDestino) {
        this.sacar(valor, contaOrigem);
        this.depositar(valor, contaDestino);
    }


    public void alterarLimite(Conta conta, double valor) {
        conta.setLimiteEspecial(valor);
        this.contaRepository.save(conta);
    }


}
