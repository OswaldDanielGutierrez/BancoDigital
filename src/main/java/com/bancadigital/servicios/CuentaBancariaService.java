package com.bancadigital.servicios;

import com.bancadigital.entidades.Cliente;
import com.bancadigital.entidades.CuentaCredito;
import com.bancadigital.entidades.CuentaAhorro;
import com.bancadigital.entidades.CuentaBancaria;
import com.bancadigital.excepciones.BalanceInsuficienteException;
import com.bancadigital.excepciones.ClienteNoEncontradoException;
import com.bancadigital.excepciones.CuentaBancariaNoEncontradaException;

import java.util.List;

public interface CuentaBancariaService {

    Cliente saveCliente (Cliente cliente);

    CuentaCredito crearCuentaBancariaCredito(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNoEncontradoException;

    CuentaAhorro crearCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNoEncontradoException;

    List <Cliente> listarClientes();

    CuentaBancaria getCuentaBancaria(String cuentaId) throws CuentaBancariaNoEncontradaException;

    void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException;

    void credit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException;

    void transferirDinero (String idPropietario, String idDestinatario, double monto) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException;

    List<CuentaBancaria> listarCuentasBancarias();
}
