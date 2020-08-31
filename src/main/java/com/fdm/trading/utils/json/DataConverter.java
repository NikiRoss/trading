package com.fdm.trading.utils.json;

import java.util.List;

public interface DataConverter<T, S> {

    S convert(List<T> dataList);
}
