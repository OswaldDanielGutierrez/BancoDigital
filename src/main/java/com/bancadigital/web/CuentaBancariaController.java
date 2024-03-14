package com.bancadigital.web;

import com.bancadigital.dtos.CuentaBancariaDTO;
import com.bancadigital.dtos.OperacionCuentaDTO;
import com.bancadigital.excepciones.CuentaBancariaNoEncontradaException;
import com.bancadigital.servicios.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
public class CuentaBancariaController {

    @Autowired
    private CuentaBancariaService cuentaBancariaService;

    @GetMapping("/obtenerCuenta/{cuentaId}")
    public CuentaBancariaDTO listarDatosDeCuentaBancaria(@PathVariable String cuentaId) throws CuentaBancariaNoEncontradaException {
        return cuentaBancariaService.getCuentaBancaria(cuentaId);
    }

    @GetMapping("/listarCuentas")
    public List<CuentaBancariaDTO> listarTodasCuentasBancarias(){
        return cuentaBancariaService.listarCuentasBancarias();
    }

    @GetMapping("/historialCuentas/{cuentaId}")
    public List<OperacionCuentaDTO> listarHistorialCuentas(@PathVariable String cuentaId){
        return cuentaBancariaService.listarOperacionesCuentas(cuentaId);
    }


}
