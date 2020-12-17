package com.fdm.trading.utils;

import java.io.File;

public interface DataWriter<T> {

    public void writeData(T data, String fileName);
}
