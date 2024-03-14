package com.bancadigital.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long id;

    private String nombre;
    private String email;

    //Un cliente tiene muchas cuentas bancarias
    @OneToMany(mappedBy = "cliente")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CuentaBancaria> cuentas;


}