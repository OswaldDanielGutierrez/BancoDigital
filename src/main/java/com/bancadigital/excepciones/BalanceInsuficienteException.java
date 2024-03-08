package com.bancadigital.excepciones;

public class BalanceInsuficienteException extends Exception{

    public BalanceInsuficienteException(String message) {
        super(message);
    }
}
