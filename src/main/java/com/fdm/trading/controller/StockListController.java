package com.fdm.trading.controller;

import com.fdm.trading.dao.StocksDao;
import com.fdm.trading.domain.Stocks;
import com.fdm.trading.service.stocksServiceImpl.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StockListController {

    private final StocksDao stocksDao;

    @Autowired
    StockServiceImpl stockService;


    public StockListController(StocksDao stocksDao) {
        this.stocksDao = stocksDao;
    }

    @RequestMapping("/")
    public String home(){
        return "redirect:/index";
    }

    /*RequestMapping("/index")
    public String index(){
        return "index";
    }*/

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getStocks(Model model){
        List<Stocks> stocksList = this.stockService.findAll();
        System.out.println(stocksList);
        model.addAttribute("stocks", stocksList);
        return "index";
    }
    /*@Controller
    @RequestMapping(value="/reservations")
    public class ReservationController {

        @Autowired
        private ReservationService reservationService;

        @RequestMapping(method= RequestMethod.GET)
        public String getReservations(@RequestParam(value="date", required=false)String dateString, Model model){
            List<RoomReservation> roomReservationList = this.reservationService.getRoomReservationsForDate(dateString);
            model.addAttribute("roomReservations", roomReservationList);
            return "reservations";
        }
    }*/

}
