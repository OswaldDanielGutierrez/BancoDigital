package com.bancadigital.servicios.implementacion;

import ch.qos.logback.core.net.server.Client;
import com.bancadigital.dtos.*;
import com.bancadigital.entidades.*;
import com.bancadigital.enums.EstadoCuenta;
import com.bancadigital.enums.TipoOperacion;
import com.bancadigital.excepciones.BalanceInsuficienteException;
import com.bancadigital.excepciones.ClienteNoEncontradoException;
import com.bancadigital.excepciones.CuentaBancariaNoEncontradaException;
import com.bancadigital.mappers.CuentaBancariaMapper;
import com.bancadigital.repositorios.ClienteRepository;
import com.bancadigital.repositorios.CuentaBancariaRepository;
import com.bancadigital.repositorios.OperacionCuentaRepository;
import com.bancadigital.servicios.CuentaBancariaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CuentaBancariaMapper cuentaBancariaMapper;

    @Override
    public ClienteDTO guardarCliente(ClienteDTO clienteDTO) {
        log.info("Guardando el nuevo cliente");
        Cliente cliente = cuentaBancariaMapper.convertirClienteDTOaCliente(clienteDTO);
        Cliente clienteBBDD = clienteRepository.save(cliente);
        return cuentaBancariaMapper.convertirClienteToClienteDTO(clienteBBDD);
    }


    @Override
    public ClienteDTO getCliente(Long clienteId) throws ClienteNoEncontradoException {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));
        ClienteDTO clienteDTO = cuentaBancariaMapper.convertirClienteToClienteDTO(cliente);
        return clienteDTO;
    }

    @Override
    public ClienteDTO actualizarCliente(ClienteDTO clienteDTO) throws ClienteNoEncontradoException {
      log.info("Actualizando cliente");
      Cliente cliente = cuentaBancariaMapper.convertirClienteDTOaCliente(clienteDTO);
      clienteRepository.save(cliente);
      return cuentaBancariaMapper.convertirClienteToClienteDTO(cliente);
    }

    @Override
    public void borrarCliente(Long id) throws ClienteNoEncontradoException{
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()){
            throw new ClienteNoEncontradoException("Cliente no existe");
        }
        clienteRepository.deleteById(id);
    }


    //GUARDAR CUENTA BANCARIA TIPO ACTUAL Y SE LE ASIGNARÁ A UN CLIENTE QUE YA EXISTE
    @Override
    public CuentaCreditoDTO crearCuentaBancariaCredito(double balanceInicial, double sobregiro, Long clienteId) throws ClienteNoEncontradoException {

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
        return cuentaBancariaMapper.convertirCuentaCreditoToCuentaCreditoDTO(cuentaCreditoBBDD);
    }

    @Override
    public CuentaAhorroDTO crearCuentaBancariaAhorro(double balanceInicial, double tasaInteres, Long clienteId) throws ClienteNoEncontradoException {

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
        return cuentaBancariaMapper.convertirCuentaAhorroToCuentaAhorroDTO(cuentaAhorroBBDD);


    }

    @Override
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDTO> clienteDTOS = clientes.stream()
                .map(cliente -> cuentaBancariaMapper.convertirClienteToClienteDTO(cliente))
                .collect(Collectors.toList());
        return clienteDTOS;
    }

    @Override
    public CuentaBancariaDTO getCuentaBancaria(String cuentaId) throws CuentaBancariaNoEncontradaException {

        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(()-> new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada"));

            if (cuentaBancaria instanceof CuentaAhorro){
                CuentaAhorro cuentaAhorro = (CuentaAhorro) cuentaBancaria;
                return cuentaBancariaMapper.convertirCuentaAhorroToCuentaAhorroDTO(cuentaAhorro);
            } else {
                CuentaCredito cuentaCredito = (CuentaCredito) cuentaBancaria;
                return cuentaBancariaMapper.convertirCuentaCreditoToCuentaCreditoDTO(cuentaCredito);
            }
    }

    @Override //QUITAR DINERO
    public void debit(String cuentaId, double monto, String descripcion) throws CuentaBancariaNoEncontradaException, BalanceInsuficienteException {

        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(()-> new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada"));


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

        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findById(cuentaId)
                .orElseThrow(()-> new CuentaBancariaNoEncontradaException("Cuenta bancaria con id: "+ cuentaId + " no encontrada"));

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
    public List<CuentaBancariaDTO> listarCuentasBancarias() {
        List<CuentaBancaria> cuentasBancarias = cuentaBancariaRepository.findAll();
        List<CuentaBancariaDTO> cuentasBancariasDTO = cuentasBancarias.stream().map(cuentaBancaria -> {
            if (cuentaBancaria instanceof CuentaAhorro){
                CuentaAhorro cuentaAhorro = (CuentaAhorro) cuentaBancaria;
                return cuentaBancariaMapper.convertirCuentaAhorroToCuentaAhorroDTO(cuentaAhorro);
            } else {
                CuentaCredito cuentaCredito = (CuentaCredito) cuentaBancaria;
                return cuentaBancariaMapper.convertirCuentaCreditoToCuentaCreditoDTO(cuentaCredito);
            }
        }).collect(Collectors.toList());
        return cuentasBancariasDTO;
    }

    @Override
    public List<OperacionCuentaDTO> listarOperacionesCuentas(String cuentaId) {

        List<OperacionesCuentas> operacionesCuentas = operacionCuentaRepository.findByCuentaBancaria(cuentaId);
        return operacionesCuentas.stream().map( operacionCuenta ->
                cuentaBancariaMapper.convertirOperacionToOperacionDTO(operacionCuenta)
        ).collect(Collectors.toList());
    }
























}
