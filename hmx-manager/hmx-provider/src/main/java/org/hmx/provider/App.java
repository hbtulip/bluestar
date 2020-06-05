package org.hmx.provider;

import java.util.Date;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableDubbo //开启dubbo的注解支持
@MapperScan({"org.hmx.utils.storage.mapper","org.hmx.*.mapper"}) 
public class App{ 
	
    public static void main(String[] args) { 
    	
    	System.out.println( "Hello World! [provider]" );
    	SpringApplication.run(App.class, args);
    }
}


 /*   

public class App implements ApplicationRunner{
 
    public static void main(String[] args) { 
    	
    	System.out.println( "Hello World! [provider]" );

    	//以非web方式启动 
        new SpringApplicationBuilder(App.class) 
            .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET 
            .bannerMode(Mode.OFF) 
            .run(args); 
        
    }

	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
        while(true) { 
            //System.out.println("now is " + new Date()); 
            Thread.sleep(1000); 
        } 		
	} 
*/
    	
