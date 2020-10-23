package com.dh.vestidobank.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "contas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Conta implements Serializable {

    private static final long serialVersionUID = 4604925791367022563L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "numero_conta", nullable = false, unique = true)
    @NotNull
    private Long numeroConta;
    private double saldo;
    @Column(name = "limite_especial")
    private double limiteEspecial;
    @Column(name = "tipo_conta", nullable = false)
    @NotNull
    private int tipoConta;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    @JsonIgnore
    private Cliente cliente;


    public boolean valorDisponivelSaque(double valor) {
        return this.saldo + this.limiteEspecial >= valor;
    }

    public void sacar(double valor) {
        this.saldo -= valor;
    }

    public void depositar(double valor) {
        this.saldo += valor;
    }
}
