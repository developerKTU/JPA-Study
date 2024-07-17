package example.datajpa.entity;

import example.datajpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    // 생성자 주입
    private MemberRepository memberRepository;
    @Autowired
    public MemberTest(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
        Team team = new Team("team");
        Team teamB = new Team("teamB");

        em.persist(team);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, team);
        Member member2 = new Member("member2", 20, team);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();

        // 확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    public void auditingTest() throws Exception{

        // given
        Member member1 = new Member("M1");
        memberRepository.save(member1); // @PrePersist 수행

        // 되도록 실제 테스트에선 사용하지말자... (테스트하기 좋은 코드는 아님)
        Thread.sleep(100);
        member1.changeUserName("M2");

        // 영속성 컨텍스트 클리어
        em.flush(); // @PreUpdate 수행
        em.clear();

        // when
        Member findMember = memberRepository.findById(member1.getId()).get();   // 실무에선 .get()을 사용하지 않는다.

        // then (Spring Data JPA)
        System.out.println("findMember.getCreateTime = " + findMember.getCreatedDate());
        System.out.println("findMember.getLastModifiedDate = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy = " + findMember.getLastModifiedBy());

    }
}