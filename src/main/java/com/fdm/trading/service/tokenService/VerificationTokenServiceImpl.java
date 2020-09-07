package com.fdm.trading.service.tokenService;

import com.fdm.trading.dao.VerificationTokenDao;
import com.fdm.trading.domain.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service
public class VerificationTokenServiceImpl {

    @Autowired
    private VerificationTokenDao tokenDao;

    public VerificationToken findTokenById(long id){
        return tokenDao.findById(id);
    }

    public Date calculateExpiry(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
