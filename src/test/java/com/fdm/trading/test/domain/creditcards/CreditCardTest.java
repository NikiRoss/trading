package com.fdm.trading.test.domain.creditcards;

import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardTest {

    @Autowired
    CardServiceImpl cardService;

    @Test
    public void test_Successful_encryption(){
        cardService.registerCreditCard("4449990007773456", "11/19", 890, "Mr N Ross");
    }

    @Test
    public void find_Card_By_Name(){
        CreditCard cc = cardService.findCardByNameOnCard("Mr N Ross");
        System.out.println(cc.toString());
    }

    @Test
    public void find_Card_By_Number(){
        CreditCard cc = cardService.findCardByLongNumber("4449990007773456");
        System.out.println(cc.toString());
        // failing due to encrypted number
    }
}
