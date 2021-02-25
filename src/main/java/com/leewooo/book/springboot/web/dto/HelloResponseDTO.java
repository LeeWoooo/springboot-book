package com.leewooo.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor//필드에 멤버변수로 선언된 변수중 final을 포함한 변수들을 매개변수로 갖는 생성자 선언
@Getter //필드에 멤버변수로 선언된 변수들의 getter를 생성
public class HelloResponseDTO {

    private final String name;
    private final int amount;
}
