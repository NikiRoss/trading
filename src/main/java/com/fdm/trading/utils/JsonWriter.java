package com.fdm.trading.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter implements DataWriter<String>{

    @Override
    public void writeData(String data, String target) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(target))){
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
