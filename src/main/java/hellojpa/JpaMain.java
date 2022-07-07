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
			/* 회원 등록 */
//			Member member = new Member();
//			member.setId(2L);
//			member.setName("HelloB");
//			em.persist(member);

			/* 회원 조회 */
//			Member findMember = em.find(Member.class, 1L);
//			System.out.println("findMember.id = " + findMember.getId());
//			System.out.println("findMember.name = " + findMember.getName());

			/* 회원 삭제 */
//			Member findMember = em.find(Member.class, 1L);
//			em.remove(findMember);

			/* 회원 수정 */
//			Member findMember = em.find(Member.class, 1L);
//			findMember.setName("helloJPA"); // List처럼 set만 해도 수정됨

			/* JPQL */
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
//			Member member = new Member();
//			member.setId(100L);
//			member.setName("HelloJPA");
//
//			// 영속
//			em.persist(member); // 이때 DB에 저장이 되는 것이 아님, 1차 캐시에 저장됨
//
//			Member findMember = em.find(Member.class, 100L);
//			System.out.println("findMember.id = " + findMember.getId());
//			System.out.println("findMember.name = " + findMember.getName());

			/* 영속 엔티티의 동일성 보장 */
//			Member findMember1 = em.find(Member.class, 100L); // DB에서 조회됨
//			Member findMember2 = em.find(Member.class, 100L); // 1차 캐시에서 조회됨
//
//			System.out.println("result = " + (findMember1 == findMember2));

			/* 트랜잭션을 지원하는 쓰기 지연 */
//			Member member1 = new Member(150L, "A");
//			Member member2 = new Member(160L, "B");
//
//			// 쓰기 지연 SQL 저장소에 저장
//			em.persist(member1);
//			em.persist(member2);

			/* 변경 감지 */
//			Member member = em.find(Member.class, 150L);
//			member.setName("ZZZZZ");

			/* 엔티티 삭제 */
//			Member member = em.find(Member.class, 22L);
//			em.remove(member); // 엔티티 삭제

			/* 플러시 */
//			Member member = new Member(200L, "member200");
//			em.persist(member);
//			em.flush(); // 플러시 직접 호출
//
//			System.out.println("=====");

			/* 준영속 상태 */
//			Member member = em.find(Member.class, 150L);
//			member.setName("AAAAA");
//
////			em.detach(member); // 준영속 상태로 전환 (수정되지 않음)
//			em.clear(); // 통채로 초기화, 1차 캐시도 사라짐
//
//			Member member2 = em.find(Member.class, 150L);



			tx.commit(); // 이때 쿼리가 날아감 (flush, commit)
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}

}
