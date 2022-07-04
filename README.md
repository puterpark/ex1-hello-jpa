# 자바 ORM 표준 JPA 프로그래밍 - 기본편

## Hello JPA - 프로젝트 생성

### persistence.xml
JPA 설정 파일  
/META-INF/persistence.xml 위치
persistence-unit name으로 이름 지정  
`javax.persistence` : 다른 JPA 구현 라이브러리를 써도 변경 가능 (JPA 표준 속성)  
`hibernate` : 해당 라이브러리만 사용 가능 (하이버네이트 전용 속성)

#### 옵션
`hibernate.show_sql` : SQL 출력 여부  
`hibernate.format_sql` : SQL 포맷팅 출력 여부  
`hibernate.use_sql_comments` : 주석 포함 SQL 출력 여부

### 데이터베이스 방언 (dialect)
JPA는 특정 RDB에 종속되지 않음  
`방언(dialect)` : SQL 표준을 지키지 않는 특정 데이터베이스 만의 고유한 기능  
`hibernate.dialect` : 해당 속성에 지정 가능

