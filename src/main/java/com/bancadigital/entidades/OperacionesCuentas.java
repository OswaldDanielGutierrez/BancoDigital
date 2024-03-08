package com.bancadigital.entidades;

import com.bancadigital.enums.TipoOperacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacionesCuentas {

    @Id
    private String id;

    private Date fechaOperacion;
    private double monto;

    @Enumerated(EnumType.STRING)
    private TipoOperacion tipoOperacion;

    private String descripcion;

    @ManyToOne
    private CuentaBancaria cuentaBancaria;
}
