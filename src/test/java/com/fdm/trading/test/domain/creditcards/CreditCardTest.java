package com.fdm.trading.test.domain.creditcards;

import com.fdm.trading.dao.CreditCardDao;
import com.fdm.trading.domain.Account;
import com.fdm.trading.domain.CreditCard;
import com.fdm.trading.domain.User;
import com.fdm.trading.security.Authorities;
import com.fdm.trading.service.cardServiceImpl.CardServiceImpl;
import com.fdm.trading.service.userServiceImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardTest {

    @Autowired
    CardServiceImpl cardService;

    @Autowired
    CreditCardDao cardDao;

    @Autowired
    UserServiceImpl userService;

    @Test
    public void test_Successful_encryption(){
        cardService.registerCreditCard("4449990007773456", "11/19", 890, "Mr N Ross");
    }

    @Test
    public void find_Card_By_Name(){
        cardService.registerCreditCard("123456789123456", "12/04/2022", 123, "name");
        //CreditCard cc = cardService.findCardByNameOnCard("Mr N Ross");
        //System.out.println(cc.toString());
    }

    @Test
    public void find_Card_By_Number(){
        CreditCard cc = cardService.findCardByLongNumber("4449990007773456");
        System.out.println(cc.toString());
        // failing due to encrypted number
    }

    @Test
    public void foo() throws Exception {
        User user = new User();
        user.setEnabled(true);
        user.setUsername("username");
        user.setPassword("foobar");
        user.setEmail("email@email.com");
        user.setSurname("surname");
        user.setFirstName("name");
        user.setDob("17/08/1987");
        CreditCard card = new CreditCard();
        card.setCardNo("1234567890123456");
        card.setCardId(46);
        card.setNameOnCard("name");
        card.setExpiry("12/04/2022");
        card.setCvv(123);
        Set<CreditCard> cards = new HashSet<>();
        cards.add(card);
        cardDao.save(card);
        user.setCreditCard(cards);
        Authorities auth = new Authorities();
        auth.setAuthority("USER_ROLE");
        Set<Authorities> auths = new HashSet<>();
        auths.add(auth);
        user.setUserAuthorities(auths);
        userService.createNewUser(null, user, "ROLE_USER");

    }
}
