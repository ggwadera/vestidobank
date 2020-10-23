package com.dh.vestidobank.repository;

import com.dh.vestidobank.model.entity.Cliente;
import com.dh.vestidobank.model.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByGerenteAndAtivoFalse(Gerente gerente);

    Optional<Cliente> findByCpf(String cpf);

}
