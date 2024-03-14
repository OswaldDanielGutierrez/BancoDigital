package com.bancadigital.repositorios;

import com.bancadigital.entidades.OperacionesCuentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperacionCuentaRepository extends JpaRepository <OperacionesCuentas,String> {

    List<OperacionesCuentas> findByCuentaBancaria(String cuentaId);
}
