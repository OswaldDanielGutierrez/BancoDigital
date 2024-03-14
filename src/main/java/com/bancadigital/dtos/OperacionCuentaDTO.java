package com.bancadigital.dtos;

import com.bancadigital.enums.TipoOperacion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class OperacionCuentaDTO {

    private String id;

    private Date fechaOperacion;

    private double monto;

    private TipoOperacion tipoOperacion;

    private String descripcion;

    private CuentaBancariaDTO cuentaBancariaDTO;

}
