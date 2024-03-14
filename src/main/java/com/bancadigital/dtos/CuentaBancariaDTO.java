package com.bancadigital.dtos;

import com.bancadigital.enums.EstadoCuenta;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;

@Data
public class CuentaBancariaDTO {

    private String tipo;

}
