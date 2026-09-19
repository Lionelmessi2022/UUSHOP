package com.test.util;

import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "ihuyi")
public class SmsUtil implements InitializingBean {
    private String account;
    private String password;
    private String sign;

    public static String Account;
    public static String Password;
    public static String Sign;

    @Override
    public void afterPropertiesSet() throws Exception {
        Account = this.account;
        Password = this.password;
        Sign = this.sign;
    }
}
