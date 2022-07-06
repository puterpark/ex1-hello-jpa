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
```java
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

```java
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

### 영속성 컨텍스트 2

#### 엔티티 조회 : 1차 캐시
> 영속성 컨텍스트 안에 있는 데이터를 먼저 조회하고, 없으면 DB에서 조회해서 1차 캐시에 저장

#### 영속 엔티티의 동일성 보장
> 1차 캐시로 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공
```java
Member findMember1 = em.find(Member.class, 100L); // DB에서 조회됨
Member findMember2 = em.find(Member.class, 100L); // 1차 캐시에서 조회됨

// 영속 엔티티의 동일성 보장
System.out.println("result = " + (findMember1 == findMember2)); // result = true
```

#### 엔티티 등록 : 트랜잭션을 지원하는 쓰기 지연
```java
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();
// 엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.
tx.begin(); // [트랜잭션] 시작
        
em.persist(memberA);
em.persist(memberB);
// 여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
        
// 커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
tx.commit(); // [트랜잭션] 커밋
```
em.persist(member) 시에 `1차 캐시`에 저장하는 것 뿐만 아니라  
INSERT SQL을 생성하여 `쓰기 지연 SQL 저장소`에도 저장한다.

tx.commit() 시에 INSERT SQL이 `flush`와 `commit`이 된다.

#### 엔티티 수정 : 변경 감지
```java
Member member = em.find(Member.class, 150L);
member.setName("ZZZZZ"); // 회원 이름 수정
```
1. flush()
2. `엔티티`와 `스냅샷(값을 최초로 가져온 순간)`을 비교한다.
3. UPDATE SQL 생성 후 `쓰기 지연 SQL 저장소`에 저장한다.
4. UPDATE SQL이 `flush`와 `commit`이 된다.

#### 엔티티 삭제
```java
//삭제 대상 엔티티 조회
Member member = em.find(Member.class, 22L);
em.remove(member); // 엔티티 삭제
```

### 플러시

영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화  
영속성 컨텍스트를 비우지 않음  
트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화하면 됨

#### 플러시 발생
* 변경 감지
* 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
* 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송 (등록, 수정, 삭제 쿼리)

#### 영속성 컨텍스트를 플러시하는 방법
* em.flush() - 직접 호출
* 트랜잭션 커밋 - 플러시 자동 호출
* JPQL 쿼리 실행 - 플러시 자동 호출

> 플러시를 한다해도 1차 캐시가 사라지지 않음

#### 플러시 모드 옵션
```java
em.setFlushMode(FlushModeType.COMMIT)
```
`FlushModeType.AUTO` : 커밋이나 쿼리를 실행할 때 플러시 (기본값)  
`FlushModeType.COMMIT` : 커밋할 때만 플러시

### 준영속 상태

영속 -> 준영속  
영속 상태의 엔티티가 영속성 컨텍스트에 분리 (detached)  
영속성 컨텍스트가 제공하는 기능을 사용 못함  

#### 준영속 상태로 만드는 방법
* `em.detach(entity)` : 특정 엔티티만 준영속 상태로 전환
* `em.clear()` : 영속성 컨텍스트를 완전히 초기화
* `em.close()` : 영속성 컨텍스트를 종료

---

## 엔티티 매핑

### 객체와 테이블 매핑

* 객체와 테이블 매핑 : `@Entity`, `@Table`
* 필드와 컬럼 매핑 : `@Column`
* 기본 키 매핑 : `@Id`
* 연관관계 매핑 : `@ManyToOne`, `@JoinColumn`

#### @Entity
해당 어노테이션이 붙은 클래스는 JPA가 관리, 엔티티라 한다.  
JPA를 사용해서 테이블과 매핑할 클래스는 **@Entity** 필수
> **[주의]**  
> **기본 생성자 필수** (파라미터가 없는 public 또는 protected 생성자)  
> final 클래스, enum, interface, inner 클래스 사용X  
> 저장할 필드에 final 사용X

#### @Table
엔티티와 매핑할 테이블 지정

|속성|기능|기본값|
|---|---|---|
|name| 매핑할 테이블 이름 | 엔티티 이름을 사용|
|catalog| 데이터베이스 catalog 매핑 ||
|schema| 데이터베이스 schema 매핑 ||
|uniqueConstraints<br/>(DDL)| DDL 생성 시에 유니크 제약 조건 생성 ||


### 데이터베이스 스키마 자동 생성

* DDL을 어플리케이션 실행 시점에 자동 생성
* 테이블 중심 -> 객체 중심
* 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
* 이렇게 **생성된 DDL을 개발 환경에서만 사용**
* 생성된 DDL은 운영서버에서는 사용하지 않거나 적절히 다듬은 후 사용

#### 속성
`hibernate.hbm2ddl.auto`

|옵션|설명|
|---|---|
|`create`|기존테이블 삭제 후 다시 생성 (DROP + CREATE)|
|`create-drop`|create와 같으나 종료 시점에 테이블 DROP|
|`update`|변경내용만 반영(운영DB 사용금지), ALTER<br/>삭제는 안됨|
|`validate`|엔티티와 테이블이 정상 매핑되었는지만 확인|
|`none`|사용하지 않음|

#### 주의점
> **운영 장비에는 절대 `create`, `create-drop`, `update`를 사용하면 안됨**

개발 초기 단계 : `create` 또는 `update`  
테스트 서버 : `update` 또는 `validate`  
스테이징 & 운영 서버 : `validate` 또는 `none`

#### DDL 생성 기능
`@Column(nullable = false, length = 10)`  
제약 조건 추가 : 회원 이름은 **필수(NULL불가)**, 10자 초과 불가

> DDL을 자동 생성할 때만 사용되고 JPA의 실행 로직에는 영향을 주지 않는다.

