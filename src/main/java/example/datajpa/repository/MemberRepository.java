package example.datajpa.repository;

import example.datajpa.dto.MemberDTO;
import example.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository 인터페이스만 상속받아주면 (상속받는것도 인터페이스) 구현체를 Spring Data JPA가 다 제공해준다. (Spring Data JPA의 힘!)
// JPARepository 인터페이스를 상속받으면 @Repository 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 원하는 조회조건으로 SELECT (JpaRepository 인터페이스에서 지원하는 이름으로 메소드 작성)
    // findBy : 조회한다, Username : equal 조건, And : and, AgeGreaterThan : age가 파라미터보다 큰 조건
    // findByUsername2AndAgeGreaterThan 이런식으로 짓는다면 에러 (No property username2 for Type Member!)
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html 자세한 내용은 링크참조
    // 조건 2개정는 이 방법으로 하지만(간단한 쿼리), 2개가 넘어간다면 메소드 명이 너무 길어져 코드가 보기 싫어짐! (2개 넘어가면 다른방법!)
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 쿼리 메소드 기능

    // Limit 조건
    List<Member> findTop3By();

    // @Query 어노테이션
    // Repository에 문자열로 쿼리를 작성하면(createQuery) 오타가 나도 컴파일러에서 오타를 잡아낼 수 없음
        // -> 이렇게 되면 고객이 해당 버튼을 눌렀을때 그때서야 에러가남 (큰일)
    // 이 @Query 어노테이션은 쿼리에서 오타가 나면 컴파일러에서 오타를 잡아주기 때문에 에러파악이 용이함! (실무에 많이 쓰임! 중요!)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    // username만 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO 이용
    @Query("select new example.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();
}
