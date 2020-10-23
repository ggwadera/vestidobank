package com.dh.vestidobank.service;

import com.dh.vestidobank.exception.ArgumentNotValidException;
import com.dh.vestidobank.exception.DataIntegrityException;
import com.dh.vestidobank.exception.ObjectNotFoundException;
import com.dh.vestidobank.model.dto.ValorOperacao;
import com.dh.vestidobank.model.dto.create.ClienteCreateDTO;
import com.dh.vestidobank.model.dto.update.ClienteUpdateDTO;
import com.dh.vestidobank.model.entity.*;
import com.dh.vestidobank.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ContaService contaService;
    private final BCryptPasswordEncoder passwordEncoder;


    public void alterarLimite(Long id, ValorOperacao valor) {
        // todo se o cliente é do gerente logado

        Cliente cliente = this.findById(id);
        if (cliente.isAtivo()) {
            this.contaService.alterarLimite(cliente.getConta(), valor.getValor());
        } else {
            throw new DataIntegrityException("Aguardando aprovação do gerente");
        }
    }

    public void ativar(Long id) {
        Cliente cliente = this.findById(id);
        if (!cliente.isAtivo()) {
            cliente.setAtivo(true);
        } else {
            throw new DataIntegrityException("Cliente ja está ativo");
        }
        this.clienteRepository.save(cliente);
    }


    @Transactional
    public void sacar(ValorOperacao valor, String cpf) {
        Cliente cliente = this.findByCpf(cpf);
        if (cliente.isAtivo()) {
            this.contaService.sacar(valor.getValor(), cliente.getConta());
        } else {
            throw new DataIntegrityException("Aguardando aprovação do gerente");
        }
    }

    private Cliente findByCpf(String cpf) {
        return this.clienteRepository.findByCpf(cpf)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente de CPF " + cpf + " não encontrado"));
    }


    @Transactional
    public void depositar(ValorOperacao valor, String cpf) {
        Cliente cliente = this.findByCpf(cpf);
        if (cliente.isAtivo()) {
            this.contaService.depositar(valor.getValor(), cliente.getConta());
        } else {
            throw new DataIntegrityException("Aguardando aprovação do gerente");
        }
    }


    @Transactional
    public void transferir(ValorOperacao valor, String cpfOrigem, String cpfDestino) {
        Cliente clienteOrigem = this.findByCpf(cpfOrigem);
        Cliente clienteDestino = this.findByCpf(cpfDestino);

        if (clienteOrigem.isAtivo() && clienteDestino.isAtivo()) {
            this.contaService.transferir(valor.getValor(), clienteOrigem.getConta(), clienteDestino.getConta());
        } else {
            throw new DataIntegrityException("Aguardando aprovação do gerente");
        }
    }


    @Transactional
    public Cliente create(ClienteCreateDTO clienteDto) {
        Cliente cliente = fromCreateDto(clienteDto);
        return this.clienteRepository.save(cliente);
    }


    @Transactional
    public Cliente update(ClienteUpdateDTO clienteNovoDto) {
        Cliente clienteSalvo = this.findById(clienteNovoDto.getId());

        clienteSalvo.setNome(clienteNovoDto.getNome());
        clienteSalvo.setEndereco(clienteNovoDto.getEndereco());

        return this.clienteRepository.save(clienteSalvo);

    }


    public Cliente findById(Long id) {
        Optional
            .ofNullable(id)
            .orElseThrow(() -> new ArgumentNotValidException("Id não pode ser nulo"));

        return this.clienteRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente de id " + id + " não encontrado"));
    }

    public List<Cliente> findAll() {
        return this.clienteRepository.findAll();
    }


    public List<Cliente> findByGerenteAndInativo(Gerente gerente) {
        return this.clienteRepository.findByGerenteAndAtivoFalse(gerente);
    }


    public void delete(Long id) {
        this.findById(id);
        this.clienteRepository.deleteById(id);
    }

    public Cliente fromCreateDto(ClienteCreateDTO clienteDto) {

        Cliente cliente = Cliente.builder()
            .nome(clienteDto.getNome())
            .cpf(clienteDto.getCpf())
            .endereco(clienteDto.getEndereco())
            .conta(
                Conta.builder()
                    .tipoConta(clienteDto.getTipoConta())
                    .numeroConta(clienteDto.getNumeroConta())
                    .build())
            .gerente(
                Gerente.builder()
                    .id(clienteDto.getGerenteId())
                    .build())
            .usuario(
                Usuario.builder()
                    .senha(this.passwordEncoder.encode(clienteDto.getSenha()))
                    .username(clienteDto.getCpf())
                    .role(RoleEnum.CLIENTE)
                    .build())
            .build();

        cliente.getConta().setCliente(cliente);

        return cliente;

    }

}
