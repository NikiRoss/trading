package com.fdm.trading.service.searchService;

import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.Stocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl {


    @Autowired
    StocksDao stocksDao;

    public List<Stocks> searchByTicker(String ticker){
        return null;
    }
}
