package com.fdm.trading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppProperties {

    @Value("${user.password.salt}")
    private static String salt;

    public static String getSalt(){
        return salt;
    }
}
