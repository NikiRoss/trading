package com.fdm.trading.exceptions;

public class UnsecurePasswordException extends Exception{

    public UnsecurePasswordException(String message){
        super(message);
    }
}
