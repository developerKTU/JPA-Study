package example.datajpa.repository;

import example.datajpa.dto.MemberDTO;
import example.datajpa.entity.Member;
import example.datajpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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

    @Test
    public void pagingTest(){
        // given
        memberRepository.save(new Member("M1", 10));
        memberRepository.save(new Member("M2", 10));
        memberRepository.save(new Member("M3", 10));
        memberRepository.save(new Member("M4", 10));
        memberRepository.save(new Member("M5", 10));

        // Spring Data JPA의 페이징은 0부터 시작한다(주의!)
        // '0' 페이지에서 '3개'의 데이터를 username DESC로 가져온다!
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        int age = 10;

        // when
        // 이를 Controller에 바로 반환하면 큰일남! 엔티티는 외부에 노출시키면 안됨 무조건 애플리케이션 안에 숨겨야함! DTO를 통해서 리턴!
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        // DTO  변환
        Page<MemberDTO> toMap = page.map(m -> new MemberDTO(m.getId(), m.getUsername(), null));

        //슬라이스로 변환
        //Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // Spring Data JPA에서 반환타입을 Page로 받으면 totalCount 쿼리까지 같이 실행해줌 -> 따라서 아래 코드는 필요없음
        // long totalCount = memberRepository.totalCount(age);

        // then
        List<Member> contents = page.getContent();
        long totalElements = page.getTotalElements();

        // 현재 페이지의 데이터 갯수가 3개인가? true
        Assertions.assertThat(contents.size()).isEqualTo(3);
        // 총 데이터의 갯수는 5개인가? true
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);    // Slice에선 지원 x
        // 총 페이지 수는 2개인가? true (총 5개의 데이터에 3개씩 보여지므로 페이지는 2개가 나온다.)
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);       // Slice에선 지원 x
        // 첫번째 페이지인가? true
        Assertions.assertThat(page.isFirst()).isTrue();
        // 다음 페이지가 존재하는가? true
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void bulkUpdate(){
        // given
        memberRepository.save(new Member("M1", 10));
        memberRepository.save(new Member("M2", 19));
        memberRepository.save(new Member("M3", 20));
        memberRepository.save(new Member("M4", 21));
        memberRepository.save(new Member("M5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        // bulk update 수행 후 영속성 컨텍스트를 클리어
        // em.flush();
        // em.clear();

        List<Member> result = memberRepository.findByUsername("M5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // then
        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy(){
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 영속성 컨텍스트 클리어
        em.flush();
        em.clear();

        // when
        // select Member
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            // 지연로딩(fetch = FetchType.LAZY)
            // Entity에서 fetch를 Lazy로 설정하면 일단 먼저 Member에 대한 필드만 가져오므로 Team객체는 null로 가져올 수 없으니 가짜 객체로 가져온다.(...$HibernateProxy$7ffISbXn)
            System.out.println("member.getClass() = " + member.getTeam().getClass()); // 가짜 객체 : class example.datajpa.entity.Team$HibernateProxy$7ffISbXn
            // 가짜 객체가 만들어진 상황에서 getName()을 하려하니 그때서야 Team 객체를 조회하는 쿼리가 수행된다. (이게 fetch join -> LAZY이다.)
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

        // 지연로딩의 문제점 << N+1 문제 : 10번의 select 쿼리 수행 시 가짜 객체를 실제로 조회하는 쿼리가 10번 더 실행되는 문제 >>
        // 이를 fetch join으로 해결함.
        List<Member> fetchMembers = memberRepository.findMemberFetchJoin();
        for (Member fm : fetchMembers) {
            System.out.println("fm.getUsername() = " + fm.getUsername());
            // fetch join을 사용하면 member와 team을 한번에 조회할 수 있는 쿼리가 실행됨. (N+1 문제 해결)
            System.out.println("fm.getTeam().getClass() = " + fm.getTeam().getClass());     // 실제 객체 : class example.datajpa.entity.Team (단 위 코드 없애거나 주석해야함)
            System.out.println("fm.getTeam().getName() = " + fm.getTeam().getName());       // 실제 Team 객체의 getName() 메소드 수행
        }
    }

    @Test
    public void findAllEntityGraph(){
        // given
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("Member1", 10, teamA);
        Member member2 = new Member("Member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 영속성 컨텍스트 클리어
        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findAll();
                                            // .findMemberEntityGraph();
                                            // .findKtuByUsername();
                                            // .findNaemdEGByUsername();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            // @EntityGraph에 의해서 fetch join 수행
            System.out.println("member.getClass() = " + member.getTeam().getClass());           // 실제 객체 : class example.datajpa.entity.Team
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){

        // given
        Member member1 = new Member("M1", 10);
        memberRepository.save(member1);

        // 영속성 컨텍스트 클리어
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("M1");

        // readOnly가 true로 되어있기 때문에 변경감지 체크를 안함. 결국 업데이트 쿼리는 수행하지 않음.
        //findMember.changeUserName("M2");

    }

    @Test
    public void lockTest(){

        // given
        Member member1 = new Member("M1", 10);
        memberRepository.save(member1);

        // 영속성 컨텍스트 클리어
        em.flush();
        em.clear();

        // when
        // 실행 쿼리 끝에 'for update'라는 키워드가 붙게 된다. (Lock이 걸렸다는 의미)
        List<Member> result = memberRepository.findLockByUsername("M1");
    }

}