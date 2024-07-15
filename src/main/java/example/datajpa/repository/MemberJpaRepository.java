package example.datajpa.repository;

import example.datajpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

// 개발자가 직접 구현한 JPA 구현체
// 인터페이스 생성 후 JpaRepository 인터페이스를 상속받아 사용하면 이런 구현체를 가져다가 쓸 수 있다! (Spring Data JPA 내장 구현체)
@Repository
public class MemberJpaRepository {

    // 이 어노테이션이 있어야 스프링 컨테이너가 EntitiManager라는 객체를 사용할 수 있음.
    // 이 EntityManager를 통해서 JPA가 DB에 INSERT, SELECT 등 쿼리역할을 수행한다.
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
