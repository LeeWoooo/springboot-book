2장 (스프링 부트에서 테스트 코드를 작성하자)
===

## Goal

테스트 코드에 대한 개념 및 작성 방법을 공부합니다.<br>

* 간단한 Controller를 만들어 Test code작성하고 Test해봅니다.

<br>

## Test 코드

견고한 서비스를만들고 싶은 개발자나 팀에서는 TDD나 단위테스트를 작성하여 코드를 검증합니다. 여기서 TDD와 단위테스트는 다른 이야기 입니다.<br>

TDD는 테스트가 주도한느 개발을 이야기 합니다. **테스트 코드를 먼저 작성하는 것** 

* 사이클

    <img src = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcECfwD%2FbtqBUTp55pN%2FkpRuUmLg6k7IzmDr12aFS0%2Fimg.gif" width=300>

    * 항상 실패하는 테스트를 먼저 작성하고(RED)
    * 테스터가 통과하는 프로덕션 코드를 작성하고(Green)
    * 테스트가 통가하면 프로덕션 코드를 리팩토링합니다(Refactor)

<Br>

반면 단위테스트는 TDD의 첫번째 단계인 기능 단위 테스트 코드를 작성하는 것을 이야기 합니다. TDD와 달리 테스트 코드를 꼭 먼저 작성해야 하는것도 아니고 리팩토링도 포함되지 않습니다. 단위테스트를 할 시 얻을 수 있는 이점은 이러합니다.

1. 단위 테스트는 개발단계 초기에 문제를 발견하게 도와줍니다.
2. 단위 테스트는 개발자가 나중에 코드를 리팩토링하거나 라이브러리 업그레이드 등에서 기존 기능이 올바르게 작동하는지 확인할 수 있습니다(회귀 테스트)
3. 단위테스트는 기능에 대한 불확실성을 감소시킬 수 있습니다.

<br>

위의 글들은 책에서 정의된 Test의 개념이다. 아래는 내가 생각하고 이해한 점들이다. <br>

Test Code를 작성하면 **일단 서버를 올렸다 내렸다를 반복하지 않아도 된다는 이점을 가지고 있다.** 또한 **여러개의 Test를 동시에 진행 할 수 있다는 점에서 좋은 이점이 있다.** 또한 단위 테스트이다 보니 **각각의 기능들을 보호하면서 Test를 진행할 수 있다는 점**에서 꼭 작성을 해야된다는 말인것 같다. <br>

이전에 공부했던 Test관련 정리해 둔 글도 참고하자(https://github.com/LeeWoooo/TIL/tree/main/spring/testCode)

<br>

---

<br>

## 작성해보기 

먼저 간단한 Contoller를 생성해 보자.
```java
//Json을 반환하는 controller를 만들어준다.
//@RestController가 나오기전에는 @Controller와 @ResponseBody를 이용하여 사용했다.
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloControllerDTO helloControllerDTO(@RequestParam(name = "name")String name, @RequestParam(name = "amount")int amount){
        return new HelloControllerDTO(name,amount);
    }
}
```

<Br>

이 두가지는 비슷하지만 하나는 query문자열이 없이 그냥 요청이 들어오고 다른 하나는 query문자열로 매개변수가 포함해서 요청이 들어오는 controller이다. <br>

Annotation을 살펴보면 

* @RestController : 현재 class가 Json을 반환하는 Controller라는 의미를 갖는다.
* @GetMapping : HTTP Method중 GET방식을 이용해 요청이 들어올 때 controller가 실행된다.
* @RequestParam : 요청이 들어올 때 query문자렬로 넘어오는 값들을 받을 수 있다.

ARC(PostMan)와 같은 툴을 이용해서 Test를 하는 방법도 있지만 Test Code를 작성해 spring 안에서 Test를 진행해보자.

```java

@RunWith(SpringRunner.class)
@WebMvcTest
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception{
        String hello = "hello";

        //MockMvc를 통해 Get방식으로 요청을 보낸다.
        mvc.perform(get("/hello"))
            //  mvc.perform의 결과를 검증한다. Header의 상태를 검증(200,404,500)
            .andExpect(status().isOk())
            //   mvc.perform의 결과를 검증한다. 응답되는 본문을(body)을 검증한다.
            .andExpect(content().string(hello));
    }//hello가_리턴된다()

    @Test
    public void helloDTO가_리턴된다() throws Exception{

        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                //.param를 통하여 요청을 보낼 때 query문자열로 파라미터를 추가할 수 있다.
                .param("name",name)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                //JSON으로 응답을 받을 때 필드별로 검증할 수 있는 method이다.
                //$를 기준으로 필드명을 명시한다.
                //jsonPath의 인자로 필드명일 지정해주고 value로 비교할 값을 입력해준다.
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.amount").value(amount));
    }//helloDTO가_리턴된다()
}
```

<br>

Controller 2가지 Test를 하는 Code이다 주석으로 설명은 적어놓았지만 천천히 하나하나 뜯어보자<br>

Annotation 및 코드를 살펴보면 

* @RunWith(SpringRunner.class) : 테스트를 진행할 때 JUnit에 내장된 싱행자 외에 다른 실행자를 실행시킨다. 즉 스프링 부트 테스트와 JUnit사이에 연결자 역할을 한다.
* @WebMvcTest : 여러 스프링 어노테이션중 Web(Spring MVC)에 집중할 수 있는 어노테이션으로 선언할 경우 @Controllr, @ControllerAdvice 등을 사용할 수 있다. (이 예제 코드에서는 Controller만 Test하기 때문에 선언)
* @Autowired : 현재 MockMvc를 스프링 컨테이너에 등록된 Bean으로 주입을 받는다(DI)
* MockMvc : 웹 API를 테스트 할 때 사용한다. 스프링MVC 테스트의 시작점이며 이 class를 이용해서 API를 테스트 할 수 있다.
*  mvc.perform : MockMVC를 통해 HTTP 요청을을 할 수 있다.

<br>

이와같이 Test code를 작성해서 각 하나하나의 method별로 Test를 진핼할 수 있지만 한번에 Test도 가능하다. 

<br>

* Test 결과

    <img src = https://user-images.githubusercontent.com/74294325/109100194-caa3c780-7767-11eb-90ec-3bdd401674af.JPG>


