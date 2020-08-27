package com.fdm.trading.utils;

import com.fdm.trading.domain.Transaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class JsonConverter implements DataConverter<Transaction, JsonElement> {
    @Override
    public JsonElement convert(List<Transaction> dataList) {

        JsonArray jsonArray =  new JsonArray();
        for(Transaction transaction:dataList){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("transactionDate", transaction.getDate().toString());
            jsonObject.addProperty("volume", transaction.getVolume());
            jsonObject.addProperty("price", transaction.getPrice());
            jsonObject.addProperty("account", transaction.getAccount().getAccountNumber());
            jsonObject.addProperty("stocks", transaction.getStocks().getCompany());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
