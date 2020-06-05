package org.hmx.consumer;

import java.util.List;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.hmx.utils.common.interceptor.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableDubbo // 开启dubbo的注解支持
public class App implements WebMvcConfigurer {

	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		// TODO Auto-generated method stub
		// WebMvcConfigurer.super.configureHandlerExceptionResolvers(resolvers);

		// 全局异常处理并不会直接生效，需要注册，可以在WebMvcConfigurer中实现注册
		resolvers.add(0, new GlobalExceptionHandler());
	}

	public static void main(String[] args) {
		System.out.println("Hello World! [consumer]");
		SpringApplication.run(App.class, args);
	}

}
