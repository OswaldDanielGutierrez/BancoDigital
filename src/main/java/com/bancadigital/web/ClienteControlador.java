package com.bancadigital.web;

import com.bancadigital.dtos.ClienteDTO;
import com.bancadigital.excepciones.ClienteNoEncontradoException;
import com.bancadigital.servicios.CuentaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bancoDigital")
public class ClienteControlador {

    @Autowired
    private CuentaBancariaService cuentaBancariaService;

    @GetMapping("/mostrarClientes")
    public List <ClienteDTO> mostrarClientes(){
        return cuentaBancariaService.listarClientes();
    }

    @GetMapping("/clientes/{id}")
    public ClienteDTO listarDatosCliente(@PathVariable(name = "id") Long clienteId) throws ClienteNoEncontradoException{
        return cuentaBancariaService.getCliente(clienteId);
    }

    @PostMapping("/guardarCliente")
    public ClienteDTO guardarCliente(@RequestBody ClienteDTO clienteDTO){
        return cuentaBancariaService.guardarCliente(clienteDTO);
    }

    @PutMapping("/actualizarCliente/{id}")
    public ClienteDTO modificarCliente(@PathVariable(name = "id") Long clienteId, @RequestBody ClienteDTO clienteDTO) throws ClienteNoEncontradoException {
        clienteDTO.setId(clienteId);
        return cuentaBancariaService.actualizarCliente(clienteDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarCliente(@PathVariable Long id) throws ClienteNoEncontradoException{
        cuentaBancariaService.borrarCliente(id);
    }

}
