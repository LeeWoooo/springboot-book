package com.leewooo.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성 모두 자동으로 설정된다.
//@SpringBootApplication가 있는 위치부터 설정을 읽어간다. 그렇기에 프로젝트의 최상단에 위치해야함

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
