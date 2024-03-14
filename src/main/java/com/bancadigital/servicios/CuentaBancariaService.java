package com.bancadigital.servicios;

import com.bancadigital.dtos.*;
import com.bancadigital.entidades.CuentaCredito;
import com.bancadigital.entidades.CuentaAhorro;
import com.bancadigital.entidades.CuentaBancaria;
import com.bancadigital.excepciones.BalanceInsuficienteException;
import com.bancadigital.excepciones.ClienteNoEncontradoException;
import com.bancadigital.excepciones.CuentaBancariaNoEncontradaException;

import java.util.List;

public interface CuentaBancariaService {

    ClienteDTO guardarCliente(ClienteDTO clienteDTO);

    ClienteDTO getCliente(Long clienteId) throws ClienteNoEncontradoException;

    ClienteDTO actualizarCliente( ClienteDTO clienteDTO) throws ClienteNoEncontradoException;

    void borrarCliente(Long id) throws ClienteNoEncontradoException;

    CuentaCreditoDTO crearCuentaBancariaCredito(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNoEncontradoException;

    CuentaAhorroDTO crearCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNoEncontradoException;

    List <ClienteDTO> listarClientes();

    CuentaBancariaDTO getCuentaBancaria(String cuentaId) throws CuentaBancariaNoEncontradaException;

    void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException;

    void credit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException;

    void transferirDinero (String idPropietario, String idDestinatario, double monto) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException;

    List<CuentaBancariaDTO> listarCuentasBancarias();

    List<OperacionCuentaDTO> listarOperacionesCuentas(String cuentaId);
}
