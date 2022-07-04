package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity // JPA가 관리할 객체
public class Member {

	@Id // 데이터베이스 PK와 매핑
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}