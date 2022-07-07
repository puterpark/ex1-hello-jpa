package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity // JPA가 관리할 객체

public class Member {

	@Id // 데이터베이스 PK와 매핑
	private Long id;

	@Column(name = "name") // DB 컬림이 name으로 설정
	private String username;

	private Integer age;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	@Lob
	private String description;

	// 기본 생성자가 필요하다.
	public Member() {

	}


}