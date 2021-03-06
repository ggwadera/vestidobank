package com.dh.vestidobank.repository;

import com.dh.vestidobank.model.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GerenteRepository extends JpaRepository<Gerente, Long> {

    Optional<Gerente> findByEmail(String email);

}
