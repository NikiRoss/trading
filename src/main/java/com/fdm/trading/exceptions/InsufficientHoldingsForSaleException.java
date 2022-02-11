package com.fdm.trading.exceptions;

public class InsufficientHoldingsForSaleException extends Exception{

    public InsufficientHoldingsForSaleException(String message) {
        super(message);
    }
}
