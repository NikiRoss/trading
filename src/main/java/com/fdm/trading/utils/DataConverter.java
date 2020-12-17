package com.fdm.trading.utils;

import org.w3c.dom.Document;

import java.util.List;

public interface DataConverter<T,S> {

    S convert(List<T> dataList);
}
