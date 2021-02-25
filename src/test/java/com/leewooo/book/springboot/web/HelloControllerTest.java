package com.leewooo.book.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다.
//여기서 SpringRunner라는 스프링 실행자를 사용한다.
//즉 , 스프링 부트 테스트와 JUnit사이에 연결자 역할을 한다.
@RunWith(SpringRunner.class)
//여러 스프링 테스트 어노테이션중 Web에 집중할 수 있는 어노테이션이다.
//선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있다.
//단 @Service, @Component, @Repository 등은 사용할 수 없다.
//여기서는 컨트롤러만 사용하기 때문에 선언한다.
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    //DI로 스프링 컨테이너로 부터 주입을 받는다.
    @Autowired
    //웹 API를 테스트 할 때 사용한다.
    //스프링 MVC테스트의 시작점이다.
    //이 class를 통해 HTTP GET,POST등의 API 테스트를 할 수 있다.
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception{//perform가 예외를 던진다.
        String hello ="hello";


        //perform
        //MockMvc를 통해 /hello 주소로 HTTP GET방식으로 요청을 한다.
        //체이닝이 지원되어 이 후 요청 결과를 검증가능하다.
        mvc.perform(get("/hello"))
                //요청 결과로 온 HTTP Header의 상태를 검증한다.(200,404,500) 여기서는 200만 검
                .andExpect(status().isOk())
                //요청결과로 온 본문의 내용을검증한다.
                //현재 Controller에서 hello를 반환하기 때문에 hello가 맞는지 검증
                .andExpect(content().string(hello));
    }

    @Test
    public void helloDTO가_리턴된다() throws Exception{
        String name = "이우길";
        int amount = 10000;

        mvc.perform(get("/hello/dto")
                //요청을 보낼 때 .param으로 query문자열(파라미터)를 추가할 수 있다.
                .param("name",name)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                //응답이 json으로 올텐데 응답오는 json의 필드를 검증할 수있다 jsonPath
                //인자로 $.필드명 으로 필드명을 지정해주고
                //value로 비교할 값을 넣어주어 검증한다.
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.amount").value(amount));
    }

}