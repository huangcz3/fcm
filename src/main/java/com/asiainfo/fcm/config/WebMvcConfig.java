package com.asiainfo.fcm.config;

import com.asiainfo.fcm.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by RUOK on 2017/6/26.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/verifyUser/")
                .excludePathPatterns("/verifyUser/otherBrowser")
                .excludePathPatterns("/verifyUser/getLoginInfo")
                .excludePathPatterns("/pcc/validation")
                .excludePathPatterns("/pcc/activityPolicyIssued")
                .excludePathPatterns("/pcc/pccResultFeedback")
                .excludePathPatterns("/customerGroups/calculateCustomerGroup")
                .excludePathPatterns("/activities/saveApprovalByBackground")
                .excludePathPatterns("/mental/saveMentalMapAct")
                .excludePathPatterns("/files/uploadTo228")
                .excludePathPatterns("/zd/passive")
                .excludePathPatterns("/files/uploadTo228")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/error_info.html")
                .excludePathPatterns("/lib/**");
        super.addInterceptors(registry);
    }
}
