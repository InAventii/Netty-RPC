package com.zzt.pro_basic.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zzt.pro_basic.service.BasicService;


@Configuration
@ComponentScan("com.zzt")
public class BasicController {
	

	    public static void main(String[] args) throws InterruptedException {
	        ApplicationContext context = 
	            new AnnotationConfigApplicationContext(BasicController.class);
	        BasicService basicService = context.getBean(BasicService.class);
	        basicService.testSaveUser();
	    }



}
