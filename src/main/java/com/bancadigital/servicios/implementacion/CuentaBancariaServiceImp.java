package com.bancadigital.servicios.implementacion;

import com.bancadigital.entidades.*;
import com.bancadigital.enums.EstadoCuenta;
import com.bancadigital.enums.TipoOperacion;
import com.bancadigital.excepciones.BalanceInsuficienteException;
import com.bancadigital.excepciones.ClienteNoEncontradoException;
import com.bancadigital.excepciones.CuentaBancariaNoEncontradaException;
import com.bancadigital.repositorios.ClienteRepository;
import com.bancadigital.repositorios.CuentaBancariaRepository;
import com.bancadigital.repositorios.OperacionCuentaRepository;
import com.bancadigital.servicios.CuentaBancariaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class CuentaBancariaServiceImp implements CuentaBancariaService {

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OperacionCuentaRepository operacionCuentaRepository;

    @Override
    public Cliente saveCliente(Cliente cliente) {
        log.info("Guardando el nuevo cliente");
        Cliente clienteBBDD = clienteRepository.save(cliente);
        return clienteBBDD;
    }

    //GUARDAR CUENTA BANCARIA TIPO ACTUAL Y SE LE ASIGNARÁ A UN CLIENTE QUE YA EXISTE
    @Override
    public CuentaCredito crearCuentaBancariaCredito(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNoEncontradoException {

        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        if (cliente == null){
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        }

        CuentaCredito cuentaCredito = new CuentaCredito();
        cuentaCredito.setId(UUID.randomUUID().toString());    //Asignamos el ID a la cuenta
        cuentaCredito.setCliente(cliente);                    //Asignamos la cuenta al cliente
        cuentaCredito.setEstadoCuenta(EstadoCuenta.CREADA);
        cuentaCredito.setBalance(balanceInicial);
        cuentaCredito.setSobreGiro(sobregiro);
        cuentaCredito.setFechaCreacion(new Date());

        CuentaCredito cuentaCreditoBBDD = cuentaBancariaRepository.save(cuentaCredito);
        return cuentaCreditoBBDD;
    }

    @Override
    public CuentaAhorro crearCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNoEncontradoException {

        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        if (cliente == null){
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        }

        CuentaAhorro cuentaAhorro = new CuentaAhorro();
        cuentaAhorro.setId(UUID.randomUUID().toString());    //Asignamos el ID a la cuenta
        cuentaAhorro.setCliente(cliente);                    //Asignamos la cuenta al cliente
        cuentaAhorro.setEstadoCuenta(EstadoCuenta.CREADA);
        cuentaAhorro.setBalance(balanceInicial);
        cuentaAhorro.setTasaDeInteres(tasaInteres);
        cuentaAhorro.setFechaCreacion(new Date());

        CuentaAhorro cuentaAhorroBBDD = cuentaBancariaRepository.save(cuentaAhorro);
        return cuentaAhorroBBDD;


    }

    @Override
    public List<Cliente> listarClientes() {

        return clienteRepository.findAll();
    }

    @Override
    public CuentaBancaria getCuentaBancaria(String cuentaId) throws CuentaBancariaNoEncontradaException {

        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(()-> new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada"));

        return cuentaBancaria;
    }

    @Override //QUITAR DINERO
    public void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException {
        CuentaBancaria cuentaBancaria = getCuentaBancaria(cuentaId);

        if (cuentaBancaria.getId() == null){
            throw new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada");
        }
        if (cuentaBancaria.getBalance() < monto){
            throw new BalanceInsuficienteException("Su balance es insuficiente para pagar el debito");
        }

        OperacionesCuentas operacionesCuentas = new OperacionesCuentas();

        operacionesCuentas.setTipoOperacion(TipoOperacion.DEBITO);
        operacionesCuentas.setMonto(monto);
        operacionesCuentas.setDescripcion(descripcion);
        operacionesCuentas.setFechaOperacion(new Date());
        operacionesCuentas.setCuentaBancaria(cuentaBancaria);
        operacionesCuentas.setId(UUID.randomUUID().toString());
        operacionCuentaRepository.save(operacionesCuentas);


        double nuevoMonto = cuentaBancaria.getBalance() - monto;
        cuentaBancaria.setBalance(nuevoMonto);
        cuentaBancariaRepository.save(cuentaBancaria);
    }

    @Override //AGREGAR DINERO
    public void credit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException {
        CuentaBancaria cuentaBancaria = getCuentaBancaria(cuentaId);

        if (cuentaBancaria.getId() == null){
            throw new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada");
        }

        OperacionesCuentas operacionesCuentas = new OperacionesCuentas();

        operacionesCuentas.setTipoOperacion(TipoOperacion.CREDITO);
        operacionesCuentas.setMonto(monto);
        operacionesCuentas.setDescripcion(descripcion);
        operacionesCuentas.setFechaOperacion(new Date());
        operacionesCuentas.setCuentaBancaria(cuentaBancaria);
        operacionesCuentas.setId(UUID.randomUUID().toString());
        operacionCuentaRepository.save(operacionesCuentas);


        double nuevoMonto = cuentaBancaria.getBalance() + monto;
        cuentaBancaria.setBalance(nuevoMonto);
        cuentaBancariaRepository.save(cuentaBancaria);
    }

    @Override
    public void transferirDinero(String idPropietario, String idDestinatario, double monto) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException {
        debit(idPropietario, monto, "Transferir dinero a: "+idDestinatario);
        credit(idDestinatario, monto, "Recibir dinero de: "+idPropietario);
    }

    @Override
    public List<CuentaBancaria> listarCuentasBancarias() {
        return cuentaBancariaRepository.findAll();
    }
}
