package com.bancadigital.repositorios;

import com.bancadigital.entidades.OperacionesCuentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionCuentaRepository extends JpaRepository <OperacionesCuentas,String> {
}
