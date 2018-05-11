package com.zhph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 这是一个spring boot 的启动类
 *
 * @author Administrator
 *
 */
@SpringBootApplication(scanBasePackages = "com.zhph",exclude={DataSourceAutoConfiguration.class})
@ServletComponentScan
public class AppStart extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppStart.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppStart.class, args);
	}

}
