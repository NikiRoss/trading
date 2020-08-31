package com.fdm.trading.utils.json;

public interface DataWriter<T> {

    void writeData(T data, String target);
}
