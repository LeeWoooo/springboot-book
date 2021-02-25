package com.leewooo.book.springboot.web.dto;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.junit.Assert.*;

//롬복 테스트
public class HelloResponseDTOTest {

    @Test
    public void 롬복테스트(){
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDTO helloResponseDTO = new HelloResponseDTO(name,amount);

        //then
        //Assertions는 JUnit과 동일한 test를 편리하게 해주는 라이브러리
        //assertThat는 검증하고 싶은 대상을 method의 인자로 받는다.
        //isEqualTo는 동등비교이다. 앞 assertThat에 인자를 어떠한 값으로 비교할 지 넣어준다.
        Assertions.assertThat(helloResponseDTO.getName()).isEqualTo(name);
        Assertions.assertThat(helloResponseDTO.getAmount()).isEqualTo(amount);

    }
}