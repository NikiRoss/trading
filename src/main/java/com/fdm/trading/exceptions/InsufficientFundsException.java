package com.fdm.trading.exceptions;

public class InsufficientFundsException extends Exception{

    public InsufficientFundsException(String message){
        super(message);
    }
}
