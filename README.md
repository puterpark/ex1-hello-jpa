# 자바 ORM 표준 JPA 프로그래밍 - 기본편

## JPA 시작하기

### Hello JPA - 프로젝트 생성

#### persistence.xml
JPA 설정 파일  
/META-INF/persistence.xml 위치
persistence-unit name으로 이름 지정  
`javax.persistence` : 다른 JPA 구현 라이브러리를 써도 변경 가능 (JPA 표준 속성)  
`hibernate` : 해당 라이브러리만 사용 가능 (하이버네이트 전용 속성)

#### 옵션
`hibernate.show_sql` : SQL 출력 여부  
`hibernate.format_sql` : SQL 포맷팅 출력 여부  
`hibernate.use_sql_comments` : 주석 포함 SQL 출력 여부

#### 데이터베이스 방언 (dialect)
JPA는 특정 RDB에 종속되지 않음  
`방언(dialect)` : SQL 표준을 지키지 않는 특정 데이터베이스 만의 고유한 기능  
`hibernate.dialect` : 해당 속성에 지정 가능


### Hello JPA - 애플리케이션 개발

#### JPA 구동 방식
`Persistence`가 설정 정보 조회하고 `EntityManagerFactory`를 생성  
`EntityManagerFactory`는 `EntityManager`를 생성

`@Entity` : JPA가 관리할 객체  
`@Id` : 데이터베이스 테이블 PK와 매핑

`EntityManagerFactory` : 하나만 생성해서 어플리케이션 전체에서 공유  
`EntityManager` : 쓰레드 간에 공유하지 않음 (사용하고 버림)

> JPA의 모든 데이터 변경은 **트랜잭션** 안에서 실행

#### JPQL
JPA는 SQL을 추상화한 **객체 지향 쿼리** 언어 제공

모든 DB 데이터를 객체로 변환해서 검색하는 것은 **불가능**  
검색을 할 떄 테이블이 아닌 **엔티티 객체**를 대상으로 검색

`JPQL` : **엔티티 객체**를 대상으로 쿼리  
`SQL` : **데이터베이스 테이블**을 대상으로 쿼리

---

## 영속성 관리 - 내부 동작 방식

### 영속성 컨텍스트 1

`영속성 컨텍스트` : 엔티티를 영구 저장하는 환경
```
// entity를 영속성 컨텍스트에 저장한다.
EntityManager.persist(entity);
```
* 논리적인 개념
* 눈에 보이지 않는다.
* 엔티티 매니저를 통해서 영속성 컨텍스트에 접근

#### 엔티티의 생명주기
* 비영속 (new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 **새로운** 상태
* 영속 (managed) : 영속성 컨텍스트에 **관리**되는 상태
* 준영속 (detached) : 영속성 컨텍스트에 저장되었다가 **분리**된 상태
* 삭제 (removed) : **삭제**된 상태

```
// 객체를 생성한 상태 (비영속)
Member member = new Memeber();
member.setId("member1");
member.setUsername('회원1');

EntityManager em = emf.createEntityManager();
em.getTransaction().begin();

// 객체를 저장한 상태 (영속)
em.persist(member);

// 영속성 컨텍스트에서 분리 (준영속)
em.detach(member);

// 객체를 삭제한 상태 (삭제)
em.remove(member);
```

#### 영속성 컨텍스트의 이점
* 1차 캐시
* 동일성(identity) 보장
* 트랜잭션을 지원하는 쓰기 지연
* 변경 감지
* 지연 로딩

