1장 (그레이들을 이용해 프로젝트 생성하기)
===

## Goal

build.gradle이 무슨역할을 하는지 알아보는 것이 목표이다.<br>
현재 프로젝트와 github와 연동을 한다.

<br>

## Gradle

기본적으로 Gradle로 프로젝트를 생성하면 build.gradle이 있다. 프로젝트의 구조를 조금더 자세하게 알고싶으면 이전 정리를 해 놓은 [스프링(gradle)프로젝트의구조](https://github.com/LeeWoooo/TIL/tree/main/spring/%EC%8A%A4%ED%94%84%EB%A7%81%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EA%B5%AC%EC%A1%B0)를 참고하면 될 것 같다. <br>

처음에 생성하면 프로젝트 구조중에 gradle이라는 폴더가 있고 그 밑에 gradle-wrapper.properties가 있다. 그 파일을 확인해 보면 현재 프로젝트에서 적용하고 있는 gradle의 버전을 확인할 수 있다. 

* 이미지

    <img src =https://user-images.githubusercontent.com/74294325/109093477-c1acf900-775b-11eb-9091-7251c6d59b8e.JPG>

<br>

이 책에서는 그레이들을 4.10.2버전을 사용하기 때문에 버전을 맞춰줘야했다. 인텔리제이 안에서 터미널을 열어 다음과 같이 입력을 하면 그레이들의 버전을 설정할 수 있다.

```
gradlew wrapper --gradle-version 4.10.2
```

하지만 여기서 'bash:gradlew: command not found'라는 메세지가 등장하고 버전이 변경되지 않는다면 앞에 ./를 붙여 상대경로를 지정해주면 된다.(이것 때문에 시간을 좀 많이 소비했지만 해결!)

<br>

그럼 이제 build.gradle의 구조를 살펴보면

```java
buildscript {
    ext{ //전역변수를 설정
        //springBootVersion라는 변수를 만들고 값으로 '2.1.7.RELEASE'를 지정
        springBootVersion ='2.1.7.RELEASE'
    }

    repositories {//라이브러리를 다운받아올 저장소 
        mavenCentral()
        jcenter()
    }

    //현재 프로젝트에서 의존하고 있는 의존성들
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

//플러그인 의존성들
apply plugin : 'java'
apply plugin : 'eclipse'
apply plugin : 'org.springframework.boot'
apply plugin : 'io.spring.dependency-management'

group 'org.example'
version '1.0-SNAPSHOT'
//현재 사용하고 있는 Java version
sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

//현재 프로젝트에서 의존하고 있는 의존성
dependencies {
    compile('org.springframework.boot:spring-boot-starter-web')
    testCompile('org.springframework.boot:spring-boot-starter-test')
    compile('org.projectlombok:lombok')
}
```

<br>

이와 같이 build.gradle은 버전을 관리해 주고 프로젝트가 필요로 하는(의존하는) 라이브러리들을 당겨오는 역할을 한다. 또한 프로젝트가 완성이 되면 build까지 해주는 편리한 놈인것 같다. <br>

또한 추가로 의존성을 추가하려면 dependencies에 추가를 해주면 된다. 현재 build.gradle를 확인해보면 compile('org.projectlombok:lombok') 또한 프로젝트를 생성 후 의존성을 추가해 준 것인데 이처럼 프로젝트 생성 이후에도 필요한 의존성을 가져올 수 있게 된다.

<br>

---

## github

인텔리제이로 생성된 프로젝트를 github에 올라는 것은 간단하다. 인텔리제이 Action에서 `share project on Githib`를 사용하면 된다. <br>

이 후 로그인을 하고 Repository를 생성하면 현재 프로젝트와 github과 연동이 된다. (로그인이 안될 시 token으로 로그인 가능)

* 결과

    <img src = https://user-images.githubusercontent.com/74294325/109094548-7e538a00-775d-11eb-8586-a810636b628f.JPG>

<Br>

이 후 .gitignore을 추가하여 필요없는 파일들이 올라가지 않도록 처리해 주면 끝!

