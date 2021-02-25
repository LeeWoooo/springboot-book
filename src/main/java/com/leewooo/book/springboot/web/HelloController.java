package com.leewooo.book.springboot.web;

import com.leewooo.book.springboot.web.dto.HelloResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//JSON을 반환하는 Controller를 만들어준다
//@Controller + @ResponseBody로 대체가 가능하다.
@RestController
public class HelloController {

    //HTTP Method인 get요청을 받을수 있는 API를 생성한다.
    //예전에는 @RequestMapping(method=RequestMethod.GET)로 사용되었다.
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    //@RequestParam는 query문자열로 넘어온 값을 받을 때 사용한다
    //인자로 query문자열의 key값을 넣어주고 자료형과 변수명을 입력해준다.
    public HelloResponseDTO helloDTO(@RequestParam("name") String name, @RequestParam("amount") int amount){
        return new HelloResponseDTO(name,amount);
    }

}
