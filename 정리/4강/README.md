4강(템플릿 엔진을 이용하여 화면 구상하기)
===

### 템플릿 엔진 (서버 템플릿 엔진)

템플릿 언젠이란 웹 개발에 있어서 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트 웨어를 이야기 합니다. 즉 Service에서 데이터를 처리한 후 Controller에서 데이터를 view에 넘겨주고 view에서는 받은 데이터로 화면을 출력하는것을 이야기 합니다. <br>

서버 템플릿 엔진은 서버에서 구동이 된다. 서버 템플릿 엔진을 이용한 화면 생성은 서버에서 자바 코드로 문자열을 만든 뒤 이 문자열을 HTML로 변환혀여 브라우저로 전달합니다. <Br>

현재 책에서는 서버 템플릿 엔진을 머스테치를 사용하지만 앞으로 하는 프로젝트에 맞춰서 템플릿엔진을 사용하면 될 것 같다.

<br>

### 머스테치 

수 많은 템플릿엔진이 존재하지만 이번 책에서는 머스테치라는 템플릿엔진을 사용합니다. 주로 thymleaf를 사용하지만(실제로 스프링에서도 적극적으로 사용을 권장하고 있다) 문법이 어렵다는 단점이 있어서 이 책에서는 머스테치를 선택한 것 같습니다. <br>

머스테치 또한 스프링에서 정식으로 지원하는 템플릿 엔진입니다. 플러그 인으로 설치를 하고 의존성만 추가해 주면 사용을 할 수 있다.

```java
compile('org.springframework.boot:spring-boot-starter-mustache')
```

<br>

이제는 이전 3강에서 만들어 놓은 API를 사용하기만 하면된다. 

<Br>

---

<Br>

## 사용하기

먼저 디자인은 부트스트랩을 사용하고 API통신을 위하여 AJAX 통신을 하여 데이터를 주고 받을 예정이다. AJAX을 조금 더 간편하게 사용하기 위해 Jquery를 통해 사용합니다. <Br>

여기서 페이지가 로딩되는 순서를 이해하고 CDN방식으로 불러와 사용합니다. CSS는 head부분에 선언을 하고 JS는 HTML의 BODY부 가장 밑에 배치를 합니다. <br>

그 이유는 head가 다 실행 되고 body가 실행이 되는데 화면을 그리는데 필요한 CSS는 head에 선언해 사용자에게 보여지는 화면을 깨지지 않게 잘 불러올 수 있도록 하고 JS의 경우 용량이 크면 클 수록 로딩이 늦어지는데 이것을 head에 놓게 되면 JS가 로딩되는 시간동안 사용자는 기다리게 됩니다. 그렇기에 페이지를 먼저 다 로딩하고 JS를 로딩하도록 body부 밑에 선언을 해줍니다.<br>

그 이후 페이지 모듈화를 위해 header부분과 footer부분을 나누었습니다. 모든 페이지에서 부트스트랩 및 jquery를 사용할 것이 때문입니다.<br>

* header

```html
<!doctype HTML>
<html>
<head>
    <title>스프링 부트 웹서비스</title>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
```

<br>

* footer

```html
<script src="https://code.jquery.com/jquery-3.5.1.min.js" type="text/javascript"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<!--index.js 추가-->
<script src="/js/app/index.js"></script>
</body>
</html>
```

<br>

이 후 이런 모듈화 시킨 페이지를 포함 시킬 때 머스테치에서는 아래와 같은 문법을 지원합니다.

```
{{>포함시킬 페이지 경로 및 페이지 명}} (경로는 현재 페이지를 기준으로 시작합니다.)
```


* 파일 구조

    <img src = https://user-images.githubusercontent.com/74294325/109738197-bc8ff400-7c0a-11eb-9d26-ac54a1deb96c.JPG>

<br>

위 사진과 같은 구조를 가질 때 index에서는 이러한 문법을 가지고 layout에 있는 페이지 들을 포함시킵니다.

* index

```
{{>layout/header}}
    ,,,
{{>layout/footer}}
```

<br>

정리에서는 html의 작성 코드는 넣지 않고 기본적인 흐름에 대해서만 정리하려 합니다! 기본 wecolme 페이지 만드는 방법은 이전 정리해 둔 [welcome페이지만들기](https://github.com/LeeWoooo/TIL/tree/main/spring/welcomepage%EB%A7%8C%EB%93%A4%EA%B8%B0)를 참조하면 될 것 같다.  <br>

처음 welcome페이지로 요청이 왔을 때 게시판 목록을 보여주기 위해 컨트롤러에서 작업이 필요합니다. 먼저 이전 작성해 두었던 service 객체를 주입받고 findAll()를 실행하여 Model에 List를 추가시켜 넘겨버린다!

```java
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts",postsService.findAllDesc());
        return "index";
    }

    ,,,
}
```

그럼 이 후 index 페이지 에서는 넘겨받은 posts를 가지고 view를 그리는데 여기서도 머스테치 문법이 사용된다. 넘겨받은 리스트의 요소들을 순회하는 문법입니다.

```
{{#넘겨받은 리스트 변수명}}

{{/넘겨받은 리스트 변수명}}

//이 값을 이용할 때는 List의  제네릭으로 설정되어 있는 객체의 변수명을 직접 입력해서 사용
```

이 문법을 사용한 index페이지의 코드입니다.

```html
<tbody id="tbody">
    {{#posts}}
        <tr>
            <td>{{id}}</td>
            <td><a href="/posts/update/{{id}}">{{title}}</a></td>
            <td>{{author}}</td>
            <td>{{modifiedDate}}</td>
        </tr>
    {{/posts}}
</tbody>
```

<Br>

위의 코드를 조금 설명해보자면 넘겨받은 list를 순회하면서 넘겨받은 객체를 가지고 tr을 생성하는 반복문 형태이다. <br>

이와 같이 진행해보면 MVC패턴을 조금 더 쉽게 이해할 수 있을 것 같다 간단히 내가 이해한 것을 간단히 정리해보자면 

1. 리포지토리를 통해 데이터 베이스에 접근을 하여 데이터를 처리한다.

2. 서비스는 리포지토리를 주입받아 리포지토리가 가져온 데이터를 이용하여 비지니스 로직을 구성한다.

3. 컨트롤러는 서비스에서 작성한 비지니스 로직을 통해 view에 데이터를 넘겨준다.

4. 뷰는 넘겨받은 데이터를 통해 view를 그려나간다. 


전부는 아니겠지만 대부분 이러한 흐름대로 진행을 하는 것 같다. 그 중간중간 데이터를 주고 받을 때 사용하는 것이 DTO이며 Entity class로는 요청이나 응답을 받아서는 안된다. 그렇기에 이전 강의에서 DTO를 생성했던 것이다.

<br>

---

<br>

## js

이 강의에서 조금 더 집중해서 본 부분이 js를 작성하는 부분이다. 각각 기능에 필요한 js들이 증가해 나가면 또는 혼자 구현하는 것이 아니라 협업을 통해 작성하는 것이라면 함수명이나 등의 중복이 발생할 수 있게 된다. 그렇다 보면 제일 마지막에 로딩된 js에 해당하는 함수가 다 덮어쓰게 된다. 그렇기 때문에 각각 js에서는 자신만의 scope를 생성하여 기능을 구현하도록 하는 것이 바람직하다. <br>

사용방법은 다음과 같다. let index와 같은 객체를 만들어 그 객체 안에서 필요한 함수를 선언하는 방법이다. 

```javascript
let main = {
    init : function (){
        let _this = this; //현재 this는 main객체를 가르킨다.
        $('#btn-save').on('click',function (){
            _this.save();
        })
        $('#btn-update').on('click',function (){
            _this.update();
        })

        $('#btn-delete').on('click',function (){
            _this.delete();
        })
    },
    save : function (){
        let data = {
            title:$('#title').val(),
            author:$('#author').val(),
            content:$('#content').val()
        };

        $.ajax({
            type : 'POST',
            url : '/api/v1/posts',
            dataType:'json',
            contentType:'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function (){
            alert('글이 등록되었습니다.')
            window.location.href='/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });

    },
    update : function (){
        let data = {
            title: $('#title').val(),
            content: $('#content').val()
        }

        const id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url:`/api/v1/posts/${id}`,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글 수정이 완료 되었습니다.');
            window.location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error))
        })
    },
    delete : function (){
        const id = $('#id').val();
        $.ajax({
            type:"DELETE",
            url:`/api/v1/posts/${id}`
        }).done(function (){
            alert('삭제가 완료되었습니다.');
            window.location.href ="/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        })

    }
};

main.init();
```

<Br>

위와 같은 코드도 let main이라는 객체를 만들어 필요한 함수를 그 안에서 구현했다. this로 자기자신을 변수에 할당한 후 이벤트를 등록해서 함수를 호출하여 사용합니다. <br>

이렇게 해서 위의 코드들은 게시판의 CRUD를 위한 ajax통신을 작성한 것 입니다. ajax통신한 코드를 Post를 기준으로  조금 살펴보자면 

```javascript
  $.ajax({
        type : 'POST',
        url : '/api/v1/posts',
        dataType:'json',
        contentType:'application/json; charset=utf-8',
        data : JSON.stringify(data)
    }).done(function (){
        alert('글이 등록되었습니다.')
        window.location.href='/';
    }).fail(function (error){
        alert(JSON.stringify(error));
    });
```

<br>

- type = HTTP method를 값으로 갖는다.(GET,POST,PUT,DELETE,,)
- url = 요청을 보낼 url (controller에서 mapping를 한 주소)
- dataType : 요청을 할 때 보낼 데이터 타입
- data : 요청을 할 때 보낼 데이터 ( 대부분 json을 문자열로 바꿔서 보낸다.)
- contentType : 간단히 말해 보내는 자원의 형식을 명시하기 위해 헤더에 실리는 정보 (POST방식과 PUT방식은 JSON을 강제하고 있음)

<br>

이러한 속성들을 이용하여 ajax통신을 한 후 만약 서버에서 반환값이 있다면 .done의 매개변수로 받아서 사용할 수 있게 되는 것이다.