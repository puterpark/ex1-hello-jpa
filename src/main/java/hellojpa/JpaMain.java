package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

	public static void main(String[] args) {
		// 엔티티 매니저 팩토리는 하나만 생성해서 어플리케이션 전체에서 공유
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// 쓰레드 간에 공유하지 않음 (사용하고 버림)
		EntityManager em = emf.createEntityManager();

		// JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			// 회원 등록
//			Member member = new Member();
//			member.setId(2L);
//			member.setName("HelloB");
//			em.persist(member);

			// 회원 조회
//			Member findMember = em.find(Member.class, 1L);
//			System.out.println("findMember.id = " + findMember.getId());
//			System.out.println("findMember.name = " + findMember.getName());

			// 회원 삭제
//			Member findMember = em.find(Member.class, 1L);
//			em.remove(findMember);

			// 회원 수정
//			Member findMember = em.find(Member.class, 1L);
//			findMember.setName("helloJPA"); // List처럼 set만 해도 수정됨

			// JPQL
//			List<Member> result = em.createQuery("select m from Member as m", Member.class)
//					.setFirstResult(0)
//					.setMaxResults(5)
//					.getResultList();
//
//			for (Member member : result) {
//				System.out.println("member.name = " + member.getName());
//			}
			
			/* 영속성 컨텍스트 */
			
			// 비영속
			Member member = new Member();
			member.setId(100L);
			member.setName("HelloJPA");
			
			// 영속
			em.persist(member); // 이때 DB에 저장이 되는 것이 아님

			// 준영속
			em.detach(member); // 영속성 컨텍스트에서 분리

			tx.commit(); // 이때 쿼리가 날아감
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}

}
