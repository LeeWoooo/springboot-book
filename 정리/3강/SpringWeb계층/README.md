3장(스프링 부트에서 JPA로 데이터베이스 다뤄보자)
===

* 웹 계층 

    <img src = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbFruEV%2FbtqAUv4HJLQ%2FH5TVBjqkKc5KBgD4Vdyvkk%2Fimg.png">


<Br>

### Web Layer

흔히 사용하는 컨트롤러(@Controller)와 JSP/Freemarker 등의 뷰 템플릿의 영역입니다. 이외에도 필터, 인터셉터, 컨트롤러 어드바이스 등의 외부 요청과 응답에 대한 전반적인 영역을 이야기 합니다.

<br>

### Service Layer

@Service에 사용되는 서비스 영역입니다. 일반적으로 Controller와 Repository의 중간 영역에서 사용되며 @Transactional이 사용되어야 하는 영역이기도 합니다.

<br>

### Repositroy Layer

DataBase와 같이 데이터 저장소에 접근하는 영역이다.(DAO와 비슷)

### DTOs

계층 간에 데이터를 교환하기 위한 객체(Data Transfer Object)입니다. 예를 들어 뷰 템플릿 엔진에서 사용될 객체나 Repositroy Layer에서 결과로 넘겨준 객체 등을 이야기 합니다.

<br>

### Domain Model

도메인이라 불리는 개발 대상을 모든 사림이 동일한 관점에서 이해할수 있고 공유할 수 있도록 단순화 시킨 것을 도메인 모델이라 합니다. 예를 들어 택시 앱이라고 하면 배차,탑승,요금 등이 모두 도메인이 될 수 있습니다. @Entity를 사용된 영역 또한 도메인 모델이라 할 수 있다. **비지니스 로직을 처리하는 곳**


