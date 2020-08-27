package com.fdm.trading.utils;

public interface DataWriter<T> {

    void writeData(T data, String target);
}
