package com.leewooo.book.springboot.web.controller;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void 메인페이지_로딩(){
        //when
        String body = this.testRestTemplate.getForObject("/",String.class);

        //then
        //"/"에 요청을 한 후 결과를 얻어와서 안에 아래와 같은 내용을 포함하고 있는지를 확인한다.
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }

}