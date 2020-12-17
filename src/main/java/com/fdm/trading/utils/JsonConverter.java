package com.fdm.trading.utils;

import com.fdm.trading.dao.StockListEntityDao;
import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.StockListEntity;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.reflect.Modifier.TRANSIENT;

public class JsonConverter implements DataConverter<Transaction, JsonElement> {

    @Autowired
    StockListEntityDao stockListEntityDao;

    @Autowired
    StocksDao stocksDao;

    @Override
    public JsonElement convert(List<Transaction> dataList) {
        System.out.println(dataList);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().excludeFieldsWithModifiers(TRANSIENT).serializeNulls().create();
        Type listType = new TypeToken<List<Transaction>>() {}.getType();
        JsonArray arr = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        for(Transaction transaction:dataList){
//

            System.out.println(transaction.toString());
            jsonObject.addProperty("transactionDate", transaction.getDate().toString());
            jsonObject.addProperty("volume", transaction.getVolume());
            jsonObject.addProperty("price", transaction.getPrice());
            jsonObject.addProperty("account", transaction.getAccount().getAccountNumber());
            jsonObject.addProperty("stock", transaction.getStocks().getCompany());
            arr.add(jsonObject);
        }
        JsonElement json = gson.toJsonTree(dataList, listType);
        System.out.println(dataList.get(0));
        System.out.println(">>>>>>>"+arr.toString());
        return arr;
    }


}
