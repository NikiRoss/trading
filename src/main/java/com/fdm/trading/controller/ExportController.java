package com.fdm.trading.controller;

import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.utils.DataConverter;
import com.fdm.trading.utils.DataWriter;
import com.fdm.trading.utils.JsonConverter;
import com.fdm.trading.utils.JsonWriter;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ExportController {

    @Autowired
    private TransactionServiceImpl transService;

    @GetMapping("/transData")
    public ResponseEntity<Resource> downloadTransData(){

        String filename = "transaction_" + String.valueOf(System.currentTimeMillis()) + ".json";
        DataConverter<Transaction, JsonElement> tdc = new JsonConverter();
        List<Transaction> transactions = transService.findAll();
        JsonElement transJson = tdc.convert(transactions);
        new JsonWriter().writeData(transJson.toString(), filename);
        File file = new File(filename);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+filename+"\"")
                .body(resource);
    }

    /*@GetMapping("/stockData")
    public String downloadData(){

    }*/
}
