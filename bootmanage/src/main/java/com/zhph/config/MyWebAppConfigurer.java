package com.zhph.config;

import com.zhph.interceptor.SameUrlDataInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zhph.interceptor.SecurityInterceptor;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
	
	  public void addInterceptors(InterceptorRegistry registry) {

	        // 多个拦截器组成一个拦截器链

	        // addPathPatterns 用于添加拦截规则

	        // excludePathPatterns 用户排除拦截

	        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/**");

	        registry.addInterceptor(new SameUrlDataInterceptor()).addPathPatterns("/**");

 
	        super.addInterceptors(registry);

	    }

	 
}
