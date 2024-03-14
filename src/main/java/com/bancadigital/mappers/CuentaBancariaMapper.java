package com.bancadigital.mappers;

import com.bancadigital.dtos.ClienteDTO;
import com.bancadigital.dtos.CuentaAhorroDTO;
import com.bancadigital.dtos.CuentaCreditoDTO;
import com.bancadigital.dtos.OperacionCuentaDTO;
import com.bancadigital.entidades.Cliente;
import com.bancadigital.entidades.CuentaAhorro;
import com.bancadigital.entidades.CuentaCredito;
import com.bancadigital.entidades.OperacionesCuentas;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CuentaBancariaMapper {

    //PASAMOS LOS DATOS DE UN CLIENTE A UN CLIENTEDTO
    public ClienteDTO convertirClienteToClienteDTO(Cliente cliente){
        ClienteDTO clienteDTO = new ClienteDTO();
        BeanUtils.copyProperties(cliente, clienteDTO);  //COPIA LAS PROPIEDADES DE CLIENTE A CLIENTEDTO
        return clienteDTO;
    }

    public Cliente convertirClienteDTOaCliente(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente); //COPIA LAS PROPIEDADES DE CLIENTEDTO A CLIENTE
        return cliente;
    }

    public CuentaAhorroDTO convertirCuentaAhorroToCuentaAhorroDTO (CuentaAhorro cuentaAhorro){
        CuentaAhorroDTO cuentaAhorroDTO = new CuentaAhorroDTO();
        BeanUtils.copyProperties(cuentaAhorro, cuentaAhorroDTO);
        cuentaAhorroDTO.setClienteDTO(convertirClienteToClienteDTO(cuentaAhorro.getCliente()));
        cuentaAhorroDTO.setTipo(cuentaAhorro.getClass().getSimpleName());
        return cuentaAhorroDTO;
    }

    public CuentaAhorro convertirCuentaAhorroDTOaCuentaAhorro (CuentaAhorroDTO cuentaAhorroDTO){
        CuentaAhorro cuentaAhorro = new CuentaAhorro();
        BeanUtils.copyProperties(cuentaAhorroDTO, cuentaAhorro);
        cuentaAhorro.setCliente(convertirClienteDTOaCliente(cuentaAhorroDTO.getClienteDTO()));
        return cuentaAhorro;
    }

    public CuentaCreditoDTO convertirCuentaCreditoToCuentaCreditoDTO(CuentaCredito cuentaCredito){
        CuentaCreditoDTO cuentaCreditoDTO = new CuentaCreditoDTO();
        BeanUtils.copyProperties(cuentaCredito, cuentaCreditoDTO);
        cuentaCreditoDTO.setClienteDTO(convertirClienteToClienteDTO(cuentaCredito.getCliente()));
        cuentaCreditoDTO.setTipo(cuentaCredito.getClass().getSimpleName());
        return cuentaCreditoDTO;
    }

    public CuentaCredito convertirCuentaCreditoDTOaCuentaCredito(CuentaCreditoDTO cuentaCreditoDTO){
        CuentaCredito cuentaCredito = new CuentaCredito();
        BeanUtils.copyProperties(cuentaCreditoDTO, cuentaCredito);
        cuentaCredito.setCliente(convertirClienteDTOaCliente(cuentaCreditoDTO.getClienteDTO()));
        return cuentaCredito;
    }

    public OperacionCuentaDTO convertirOperacionToOperacionDTO (OperacionesCuentas operacionesCuentas){

        OperacionCuentaDTO operacionCuentaDTO = new OperacionCuentaDTO();
        BeanUtils.copyProperties(operacionesCuentas, operacionCuentaDTO);
        return operacionCuentaDTO;
    }































}
