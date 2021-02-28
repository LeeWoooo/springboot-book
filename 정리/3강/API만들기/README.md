3장(스프링 부트에서 JPA로 데이터베이스 다뤄보자)
===

등록, 수정 , 조회 API만들기 

Entity Class와 Controller에서 사용하는 DTO를 분리해서 코드를 작성할 것입니다. 그 이유는 Entity class는 DB에 맞닿은 핵심 class이기 때무에 Request나 Response class로 사용해서는 안되기 때문입니다. 사소한 변경이지만 이를 위해 Entity Class를 변경하는 것은 너무 큰 변경입니다. 그 이유는 수 많은 서비스나 로직들이 Entity Class를 중심으로 동작하고 있기 떄문입니다. <br>

또한 Bean을 주입 받을 때 생성자 주입을 권장하는데 생성자 주입을 Lombok으로도 처리할 수 있습니다. @RequiredArgsConstructor 을 제공해 줍니다. 이 Annotation을 사용하면 얻을 수 있는 이점은 @Autowired를 사용하면 주입받는 객체가 변경되면 생성자관련 코드도 변경해주어야 했지만 @RequiredArgsConstructor를 사용하면 주입되는 객체 명만 수정하면 되기 때문에 번거로움을 해결할 수 있습니다.

그럼 API를 설계를 해보면 다음과 같습니다.

<br>

Method | url | return
:---: | :---: | :---:
GET | /api/v1/posts | List&lt;Posts>
GET | /api/v1/posts/{id} | PostsResponseDTO
PUT | /api/v1/posts/{id} | Long
POST | /api/v1/posts/{id} | Long
Delete | /api/v1/poats/{id} | Long

<Br>

먼저 DTO class들을 살펴보자

* PostsUpdateRequestDTO
```java
@Getter
@NoArgsConstructor
public class PostsUpdateRequestDTO {

    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDTO(String title, String content){
        this.title = title;
        this.content = content;
    }

}
```

* PostsSaveRequestDTO
```java
@Getter
@NoArgsConstructor
public class PostsSaveRequestDTO {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDTO(String title,String content,String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
```

* PostsResponseDTO
```java
@Getter
public class PostsResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDTO(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
```

<Br>

이와 같이 각 기능에 필요한 DTO를 생성함으로 Entity class를 직접 사용하는 것이 아니라 각 Layer간의 데이터 이동을 위한 DTO 객체를 생성해서 사용한다. <br>

다음은 Service class이다.

* PostsService
```java
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepositroy postsRepositroy;

    @Transactional
    public Long save(PostsSaveRequestDTO postsSaveRequestDTO){
        return postsRepositroy.save(postsSaveRequestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO){
        //update기능은 id를 입력하여 Entity Class를 찾고 
        Posts posts = postsRepositroy.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        //Entity Class에 미리 생성해둔 method를 이용하여 update를 진행한다.
        posts.update(requestDTO.getTitle(), requestDTO.getContent());
        return id;
    }

    public PostsResponseDTO findById(Long id){
        Posts entity = postsRepositroy.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        return new PostsResponseDTO(entity);
    }

}
```

<Br>

각각의 DTO를 받아서 DI로 주입받은 postsRepositroy를 이용해 DB에 접근을 합니다. @Transactional은 transaction이 commit될 때 작동을 합니다. 또한 처리중 예외가 발생할 시 rollback처리를 자동으로 수행해줍니다.(이 Annotation을 선언하게되면 트랜잭션 기능이 적용된 프록시 객체가 생성이 된다.) <br>

이전 정리에서도 말했지만 Entity를 변경할 때는 의도와 사용이 분명한 method를 선언해서 사용하는데 update를 위해 이러한 method를 생성했다. <BR >

```java
public void update(String title,String content){
    this.title = title;
    this.content = content;
}
```

<br>

이 때 update기능에서 데이터베이스에 쿼리를 날리는 부분이 없다. 하지만 **update기능을 수행할 수 있는 이유는 JPA 영속성 컨텍스트 때문이다.** 영속성 컨텍스트란 엔티티를 영구 저장하는 환경이다. JPA의 핵심은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈리게 된다. JPA의 엔티티 매니저가 활성화된 상태(Spring Data JPA를 사용하면 기본값이다.)로 **트랜잭션안에서 데이터베이스에서 데이터를 가져오면 이  데이터는 영속성 컨텍스트가 유지된 상태이다.** 이 상태에서 **값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영한다.** 즉 Entity객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다는 것이다. 이러한 개념을 더티체킹이라한다. <br>

위의 더티 체킹 개념을 정리하자면 <br>

1. 데이터를 가져와서 영속성 컨텍스트에 포함시키기 (트랜젝션 안에서 데이터를 가져오면 영속성 컨텍스트에 포함)
```java
 @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO){
        //update기능은 id를 입력하여 Entity Class를 찾고 
        Posts posts = postsRepositroy.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
```

<Br>

2. Entity의 값 변경하기 (이렇게 변경을하면 트랜젝션이 끝나는 시점에서 해당 테이블에 변경분을 반영)
```java
        //Entity Class에 미리 생성해둔 method를 이용하여 update를 진행한다.
        posts.update(requestDTO.getTitle(), requestDTO.getContent());
        return id;
    }
```

<br>

마지막으로 Controller만 작성하면 Repository부터 controller까지 작성이 완료된다.

* PostsController
```java
@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDTO postsSaveRequestDTO){
        return postsService.save(postsSaveRequestDTO);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDTO requestDTO){
        return postsService.update(id,requestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id){
        return postsService.findById(id);
    }

}
```

<br>

여기에서도 확인할 수 있듯이 각 요청에 맞는 DTO를 생성해서 값을 받고 받은 값을 이용해서 Layer간의 Data를 조작한다.