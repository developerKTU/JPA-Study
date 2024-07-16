package example.datajpa.repository;

import example.datajpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 개발자가 직접 구현한 JPA 구현체
// 인터페이스 생성 후 JpaRepository 인터페이스를 상속받아 사용하면 이런 구현체를 가져다가 쓸 수 있다! (Spring Data JPA 내장 구현체)
@Repository
public class MemberJpaRepository {

    // 이 어노테이션이 있어야 스프링 컨테이너가 EntitiManager라는 객체를 사용할 수 있음.
    // 이 EntityManager를 통해서 JPA가 DB에 INSERT, SELECT 등 쿼리역할을 수행한다.
    @PersistenceContext
    private EntityManager em;

    // CREAT
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    // DELETE
    public void delete(Member member){
        em.remove(member);
    }

    // 전체 조회
    public List<Member> findAll(){
        //JPQL -> 테이블 대상이 아닌 객체를 대상으로 하는 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 조건 조회
    public Optional<Member> findById(long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 카운트 조회
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    // 단건 조회
    public Member find(Long id){
        return em.find(Member.class, id);
    }

    // 다중 조건 조회 (JPA 구현체 직접 구현)
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    // 순수 JPA 페이징과 정렬
    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // 페이징 기능을 구현하기 위해 TOTAL COUNT 조회
    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

}
