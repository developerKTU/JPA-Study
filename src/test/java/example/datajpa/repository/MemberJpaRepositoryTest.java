package example.datajpa.repository;

import example.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// Junit5 + Stringboot 조합에선 @SpringBootTest 어노테이션만 명시해도 됨
@SpringBootTest
// JPA의 모든 데이터 변경은 트랜잭셔널 내에서 이루어져야하기 때문에 어노테이션 명시
@Transactional
// @SpringBootTest는 @Transectional이 있으면 테스트 후 롤백을 해버림 -> 공부목적으로 INSERT되는지 확인하려면 @Rollback(false) 옵션 주기!
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        // setter보다는 생성자로 파라미터를 넘겨주는게 더 좋은 방법
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        // 검증
        // isEqualTo는 '=='과 같다. 테이블은 같은 트랜잭션 내에서 이루어져야하기 때문에 member와 findMember는 같은 인스턴스로 취급한다.
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("Member1");
        Member member2 = new Member("Member2");

        // CREATE
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("M2", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("M2");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void pagingTest(){
        // given
        memberJpaRepository.save(new Member("M1", 10));
        memberJpaRepository.save(new Member("M2", 10));
        memberJpaRepository.save(new Member("M3", 10));
        memberJpaRepository.save(new Member("M4", 10));
        memberJpaRepository.save(new Member("M5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);


        // 페이징 계산 공식 적용...(Spring Data JPA는 지원해줌!)

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }

    @Test
    public void bulkUpdate(){
        // given
        memberJpaRepository.save(new Member("M1", 10));
        memberJpaRepository.save(new Member("M2", 19));
        memberJpaRepository.save(new Member("M3", 20));
        memberJpaRepository.save(new Member("M4", 21));
        memberJpaRepository.save(new Member("M5", 40));

        // when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        // then
        assertThat(resultCount).isEqualTo(3);

    }
}