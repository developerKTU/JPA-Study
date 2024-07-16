package example.datajpa.repository;

import example.datajpa.dto.MemberDTO;
import example.datajpa.entity.Member;
import example.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember(){
        System.out.println("memberRepository = "+ memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // 검증
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("Member1");
        Member member2 = new Member("Member2");

        // CREATE
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // JPARepository 인터페이스를 상속받은 memberRepository에서 findByUsernameAndAgeGreaterThan 이름의 메소드를 정의하면
        // 메소드 이름에 맞는 쿼리가 실행됨
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("M2", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("M2");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findTop3By (){
        // 쿼리가 조건에 맞게 실행되는지만 확인
        List<Member> result = memberRepository.findTop3By();
    }

    @Test
    public void queryAnnoTest (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findMember("M2", 20);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("M2");
    }

    @Test
    public void findUsernameList (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();

        // 실무에선 Assert 사용
        for (String s : result) {
            System.out.println("username = " + s);
        }
    }

    @Test
    public void findMemberDTO (){
        Team team1 = new Team("Team1");
        teamRepository.save(team1);

        Member m1 = new Member("M1", 10, team1);
        memberRepository.save(m1);

        List<MemberDTO> result = memberRepository.findMemberDTO();

        // 실무에선 Assert 사용
        for (MemberDTO memberDTO : result) {
            System.out.println("DTO = " + memberDTO);
        }
    }

    @Test
    public void findNames (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("M1", "M2"));

        // 실무에선 Assert 사용
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType (){
        Member m1 = new Member("M1", 10);
        Member m2 = new Member("M2", 20);
        Member m3 = new Member("M3", 30);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        // 컬렉션 반환타입 함수 호출 (여러건)
        // List는 찾고자하는 username과 매칭이 안되어도 빈 리스트를 반환하기 때문에 null이 아니므로 그냥 사용하면됨
        List<Member> result = memberRepository.findByUsername("M1");

        // Member 반환타입 함수 호출 (단건)
        // Memeber 단건 조회일경우 매칭이 안되면 null이 반환됨(Spring JPA에서는 자체적으로 try-catch문으로 감싸 null이 반환되지만)
        // JAVA 8 버전 이하의 JPA에서는 exception을 뱉어버리기 때문에 문제가 될 수 있음.
        Member result2 = memberRepository.findMemberByUsername("M2");

        // Optional 반환타입 함수 호출 (단건)
        // 따라서 조회결과가 있을지 없을지 판단이 안될땐 null일때 경우도 처리할 수 있는 Optional을 사용함.
        // 조회되는 데이터가 2건 이상일때 optional을 사용하면 exception 발생
        Optional<Member> result3 = memberRepository.findOptionalByUsername("m3");
    }
}