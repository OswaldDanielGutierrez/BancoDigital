package com.bancadigital;

import com.bancadigital.dtos.ClienteDTO;
import com.bancadigital.dtos.CuentaAhorroDTO;
import com.bancadigital.dtos.CuentaBancariaDTO;
import com.bancadigital.dtos.CuentaCreditoDTO;
import com.bancadigital.entidades.*;
import com.bancadigital.servicios.BancoService;
import com.bancadigital.servicios.CuentaBancariaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class BancaDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancaDigitalApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(BancoService bancoService){
        return args -> {
            bancoService.consultar();
        };
    }

    @Bean
    CommandLineRunner start(CuentaBancariaService cuentaBancariaService){
        return args ->{
            Stream.of("Pablo Mostaza", "Julia Martinez", "Pedro Diaz", "Martina Cantona").forEach(nombre->{
                ClienteDTO cliente = new ClienteDTO();
                cliente.setNombre(nombre);
                cliente.setEmail(nombre+"@gmail.com");
                cuentaBancariaService.guardarCliente(cliente);

                });

            cuentaBancariaService.listarClientes().forEach(cliente ->{
                try{
                    cuentaBancariaService.crearCuentaBancariaAhorro(Math.random() * 900000, 9000, cliente.getId());
                    cuentaBancariaService.crearCuentaBancariaCredito(Math.random() * 100000 , 3.2, cliente.getId());

                    List<CuentaBancariaDTO> listaCuentasBancarias = cuentaBancariaService.listarCuentasBancarias();

                    for (CuentaBancariaDTO cuentaBancaria : listaCuentasBancarias){
                        for (int i = 0; i < 10 ; i++){
                            String cuentaId;

                            if (cuentaBancaria instanceof CuentaAhorroDTO){
                                cuentaId = ((CuentaAhorroDTO) cuentaBancaria).getId();
                            } else {
                                cuentaId = ((CuentaCreditoDTO) cuentaBancaria).getId();

                            }

                            cuentaBancariaService.credit(cuentaId, 100000, "Credito");
                            cuentaBancariaService.debit(cuentaId, 10000+Math.random()*100000, "Debito");
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            });
        };
    }
}
