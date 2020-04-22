package com.fdm.trading.dao;

import com.fdm.trading.domain.StockListEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockListEntityDao extends CrudRepository<StockListEntity, Long> {

    List<StockListEntity> findByStockId(long stockId);

    List<StockListEntity> findByAccountId(long accountId);

    List<StockListEntity> findByAccountIdAndStockId(long accountId, long stockId);




}
