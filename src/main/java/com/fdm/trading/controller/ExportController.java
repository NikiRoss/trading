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
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/transData/{filetype}")
    public ResponseEntity<Resource> downloadTransData(@PathVariable String filetype){
        return exportData("transaction", filetype);
    }

    @GetMapping("/stocksData/{filetype}")
    public ResponseEntity<Resource> downloadStocksData(@PathVariable String filetype){
        return exportData("stocks", filetype);
    }

    public ResponseEntity<Resource> exportData(String type, String filetype){
        if("xml".equals(filetype)) {
            return exportDataXML(type);
        }
        return exportDataJson(type);
    }

    public ResponseEntity<Resource> exportDataXML(String type){
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
        String filename = type + "_" + System.currentTimeMillis() + ".json";
        new JsonWriter().writeData(doc.toString(), filename);
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
