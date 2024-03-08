package com.bancadigital.servicios;

import com.bancadigital.entidades.CuentaCredito;
import com.bancadigital.entidades.CuentaAhorro;
import com.bancadigital.entidades.CuentaBancaria;
import com.bancadigital.repositorios.CuentaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BancoService {

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    public void consultar(){
        CuentaBancaria cuentaBancariaBBDD = cuentaBancariaRepository.findById("1").orElse(null);

        if (cuentaBancariaBBDD != null){
            System.out.println("***********************************");
            System.out.println("ID: " + cuentaBancariaBBDD.getId());
            System.out.println("Balance: " + cuentaBancariaBBDD.getBalance());
            System.out.println("Estado: " + cuentaBancariaBBDD.getEstadoCuenta());
            System.out.println("Fecha Creación: " + cuentaBancariaBBDD.getFechaCreacion());
            System.out.println("Nombre: " + cuentaBancariaBBDD.getCliente().getNombre());
            System.out.println("Nombre de la clase: " + cuentaBancariaBBDD.getClass().getSimpleName());

            if (cuentaBancariaBBDD instanceof CuentaAhorro){
                System.out.println("Tasa de interés: " + ((CuentaAhorro) cuentaBancariaBBDD).getTasaDeInteres());
            }
            else if (cuentaBancariaBBDD instanceof CuentaCredito) {
                System.out.println("Sobregiro: " + ((CuentaCredito) cuentaBancariaBBDD).getSobreGiro());
            }

            cuentaBancariaBBDD.getOperaciones().forEach(operacionesCuentas -> {
                System.out.println("----------------------------------------------------");
                System.out.println("Tipo de operación: " + operacionesCuentas.getTipoOperacion());
                System.out.println("Fecha operación: " + operacionesCuentas.getFechaOperacion());
                System.out.println("Monto: " + operacionesCuentas.getMonto());
            });


        }



    }


}
