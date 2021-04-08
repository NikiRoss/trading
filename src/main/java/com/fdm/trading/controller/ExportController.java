package com.fdm.trading.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import com.fdm.trading.service.transactionService.TransactionServiceImpl;
import com.fdm.trading.utils.json.*;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;

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

    @Autowired
    private StockServiceImpl stockService;

    @GetMapping("/transData")
    public ResponseEntity<Resource> downloadTransData(){
        return exportData("transaction");
    }

    @GetMapping("/transData/Json")
    public ResponseEntity<Resource> downloadTransDataJson(){
        return exportDataJson("transaction");
    }

    @GetMapping("/stocksData")
    public ResponseEntity<Resource> downloadStocksData(){
        return exportData("stocks");
    }

    public ResponseEntity<Resource> exportData(String type){
        DataConverter dc;
        Document doc = null;
        if(type.equals("transaction")){
             dc = new XmlTransactionConverter();
            List<Transaction> transactions = transService.findAll();
            doc = (Document) dc.convert(transactions);
        } else if (type.equals("stocks")){
            dc = new XmlStocksConverter();
            List<Stocks> stocks = stockService.findAll();
            doc = (Document) dc.convert(stocks);
        }
        String filename = type + "_" + String.valueOf(System.currentTimeMillis()) + ".xml";
        new XmlWriter().writeData(doc, filename);
        File file = new File(filename);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(CONTENT_DISPOSITION, "attachment; filename=\""+filename+"\"")
                .body(resource);
    }

    public ResponseEntity<Resource> exportDataJson(String type){
        DataConverter dc;
        JsonElement doc = null;
        if(type.equals("transaction")){
            dc = new JsonConverter();
            List<Transaction> transactions = transService.findAll();
            doc = (JsonElement) dc.convert(transactions);
        } else if (type.equals("stocks")){
            dc = new JsonConverter();
            List<Stocks> stocks = stockService.findAll();
            doc = (JsonElement) dc.convert(stocks);
        }
        String docString = doc.getAsString();
        String filename = type + "_" + String.valueOf(System.currentTimeMillis()) + ".xml";
        new JsonWriter().writeData(docString, filename);
        File file = new File(filename);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(CONTENT_DISPOSITION, "attachment; filename=\""+filename+"\"")
                .body(resource);
    }
}
