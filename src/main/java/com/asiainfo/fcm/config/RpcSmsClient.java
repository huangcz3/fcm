package com.asiainfo.fcm.config;

import com.asiainfo.sms.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by PuMg on 2017/12/29/0029.
 */
@Configuration
public class RpcSmsClient {


    private static final Logger logger = LoggerFactory.getLogger(RpcSmsClient.class);

    @Value("${fcm.sms.init.10086.businessId}")
    private String smsBusinessId;

    @Value("${fcm.sms.init.10086.key}")
    private String smsKey;

    @Value("${fcm.sms.init.yhtx.businessId}")
    private String yhtxBusinessId;

    @Value("${fcm.sms.init.yhtx.key}")
    private String yhtxKey;



    @Bean(name = "yhtxClient" )
    public Client yhtxClient(){
        Client  yhtxClient = new Client();
        yhtxClient.init(yhtxKey,yhtxBusinessId);
        return yhtxClient;
    }

    @Bean(name = "smsClient" )
    public Client init(){
        Client  client = new Client();
        client.init(smsKey,smsBusinessId);
        return client;
    }
}
