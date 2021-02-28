3장(스프링 부트에서 JPA로 데이터베이스 다뤄보자)
===

웹 서비스를 개발하고 운영하다보면 DB와 연동하는 것은 선택이 아닌 필수이다. 자바는 객체지향 프로그래밍이지만 DB와 연동할 때 JDBC를 이용해 SQL로 접근하거나 MyBatis와 같은 SQL 매퍼를 이용해 DB query를 작성하였다. 그러다 보니 객체 중심적인 개발을 하기 어려워지고 패러다임 불일치가 일어납니다.

> 관계형 데이터베이스와 객체지향 프로그래밍 언어의 패러다임이 서로 다른데, 객체를 데이터 베이스에 저장하려고 하니 여러 문제가 발생하게 되는데 이를 페러다임 불일치라 한다.

이렇게 페러다임 불일치가 일어나는 것을 해결해 주기 위해 사용하는 것이 JPA이다. JPA는 ORM(Object Relation Mapping)로 객체를 테이블에 매핑하는 방식입니다.

<br>

---

<br>

## Spring Data JPA

JPA는 인터페이스로서 자바 표준명세서이다. 인터페이스인 JPA를 사용하기 위해서는 구현체가 필요한데 대표적으로 Hibernate이 존재한다. 하지만 Spring에서는 JPA를 사용할 때 이 구현체들을 직접 다루지 않는다. 구현체들을 좀 더 쉽게 사용하고자 추상화 시킨 Spring Data JPA라는 모듈을 사용한다. 

>JPA를 스프링 부트가 한번 감싸서 사용하기 쉽게 추상화를 시킨 것이 Spring DATA JPA이다.

Spring Data JPA를 사용하면 좋은 점은 대표적으로 두가지가 있다.

1. 구현체 교체의 용의성 : Spring Data JPA 내부에서 구현체 매핑을 지원해주기 때문.

2. 저장소 교체의 용의성 : 의존성을 교체해주기만 하면 저장소를 쉽게 교체할 수 있다.

Spring Data JPA를 사용하기 위해서는 build.gradle에 의존성을 추가해줘야 한다.

```java
compile('org.springframework.boot:spring-boot-starter-data-jpa')
```

여기까지 책에 있는 내용 정리~

<Br>

---

<Br>

## 사용해보기

책에 나와있는 예제를 따라하면서 이해한 것을 정리해 보겠습니다.

우선은 Entity Class를 생성합니다. Entity Class는 DB에 있는 Table과 Mapping되는 class입니다. 또한 저장 및 수정등을 할 때 실제 query를 날리지 않고 이 class를 활용하여 저장 및 수정을 합니다. **중요한 점은 Entity와 Repository는 항상 같이 다녀야 합니다.** Entity는 Repository가 ㅇ벗다면 제대로 역할을 할 수 없기 때문입니다.

* 예제 Entity Class (Posts)
```java
@Getter
@NoArgsConstructor
@Entity 
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    private String author;

    //Builder 또한 class가 생성될 때 생성자처럼 값을 넣어준다.
    //빌터를 사용하면 조금 더 직관적이게 값을 입력해줄 수 있다.
    @Builder
    public Posts(String title,String content,String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }

}
```

<br>

사용된 Annotation을 살펴 보자면 

* @Entity : 테이블과 링크될 class임을 명시해줍니다.

* @Id : 해당 테이블의 PK 필드를 나타냅니다.

* @GeneratedValue(strategy = GenerationType.IDENTITY) : PK의 생성 규칙을 나타냅니다.
    > GenerationType.IDENTITY : DB의 PK생성 규칙을 따르겠다는 의미. <br>
    GeneratedValue(strategy = GenerationType.AUTO) : hibernate_sequence 테이블의 시퀀스 값을 가져와 PK를 생성한다.

* @Column : 테이블의 column의 역할을 하고 있음을 명시해준다. 선언해주지 않아도 현재 @Entity Class의 필드들은 column으로 인식된다.

* @Builder : 생성자와 동일하게 객체가 생성될 때 값을 초기화 해주는 역할을 하지만 생성자와 차이점은 객체를 생성할 때 직관적이게 어떠한 값이 들어가는지 확인할 수 있다.(Lombok)

<br>

**여기서 가장 중요한 점은 @Entity Class에는 Setter가 없다는 것이다. 테이블과 매칭되는 객체를 public으로 수정을 열어놓으면 나중에 데이터 저장 및 기능변경시 큰 혼란을 초래할 수 있다. 그렇기 때문에 값 변경이 필요하다면 update와 같은 method를 만들어 목적과 의도를 나타내어 수정을 해준다.**

<br>

이 후 이 객체를 이용하여 Table과 연동할 Repository를 생성합니다.(DAO와 비슷하다고 생각하면 이해가 조금 더 쉽게 될 것 같습니다.) Repository는 인터페이스로 생성을 합니다. JpaRepository를 상속받고 연결할 Entity Class와 pk의 자료형을 넣어줍니다. 

* 예제 Repository(PostsRepository)

```java
public interface PostsRepositroy extends JpaRepository<Posts,Long> {
}
```

<Br>

이렇게 생성한 Repository는 JpaRepository를 상속받아 기본적인 CRUD의 method를(부모)사용할 수 있게 되며 페이징 처리 또한 구현되어 있는 것을 가져다가 사용할 수 있다. (기본적인 CRUD method는 save,findAll,findById,delete 등이 있다.)

<br>

이제 이렇게 생성된 Entity Class와 Repository를 Test하는 code를 작성해보면 다음과 같다.

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositroyTest {

    @Autowired
    private PostsRepositroy postsRepositroy;

    @After
    public void cleanup(){
        postsRepositroy.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title = "테스트 게시글";
        String content ="테스트 본문";

        postsRepositroy.save(Posts.builder().title(title).content(content).author("leecoding2285@gmail.com").build());

        //when
        List<Posts> postsList = postsRepositroy.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }
}
```

사용된 Annotation을 살펴 보자면

* @RunWith(SpringRunner.class): @Mock 과 @Autowired등의 기능을 JUnit에서 사용할 수 있도록 해주는 브릿지

* @After : Test Method가 실행되고 종료되는 시점마다 실행되는 method 현재는 저장된 Posts를 초기화 시켜주는 역할

<br>

테스트 내용은 간단하다. 먼저 Posts를 생성하여 하나 저장하고 저장된 Posts를 불러와서 내가 저장한 title과 content와 동일한지 Test를 하는 것이다. 현재 Test에서는 org.assertj.core.api.Assertions를 사용하여 조금 더 직관적이고 보기 좋은 Test Code를 작성하였다. 

간단하게 살펴보면 assertThat()에 검증해야 하는 값을 넣고 .isEqualTo()에 비교 대상의 값을 넣어서 같은지를 검증 할 수 있다.