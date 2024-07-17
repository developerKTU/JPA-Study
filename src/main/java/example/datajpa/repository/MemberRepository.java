package example.datajpa.repository;

import example.datajpa.dto.MemberDTO;
import example.datajpa.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// JpaRepository 인터페이스만 상속받아주면 (상속받는것도 인터페이스) 구현체를 Spring Data JPA가 다 제공해준다. (Spring Data JPA의 힘!)
// JPARepository 인터페이스를 상속받으면 @Repository 생략 가능
/* 사용자 정의 레포지토리 (MemberCustomRepository) : JPA 레포지토리 인터페이스만으로 해결이 안되는 복잡한 쿼리나 동적 쿼리 구현
   또는 비즈니스 로직, 단순 화면 구현 로직 등으로 클래스(인터페이스)를 나눌때 사용자 정의 레포지토리를 사용한다.
   (사용자 정의 레포지토리 + queryDSL 또는 Mybatis 조합으로 사용) */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

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
    // 이 @Query 어노테이션은 쿼리에서 오타가 나면 컴파일러에서 오타를 잡아주기 때문에 에러 파악이 용이함! (실무에 많이 쓰임! 중요!)
    // :username, :age는 파라미터 바인딩이다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);


    // username만 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();


    // DTO 이용
    @Query("select new example.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();


    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    // 반환타입 (Spring JPA는 유연한 반환타입을 지원!)
    List<Member> findByUsername(String username);       // 반환타입 : 컬렉션
    Member findMemberByUsername(String username);       // 반환타입 : Member(단건)
    Optional<Member> findOptionalByUsername(String username);    // 반환타입 : Optional(단건)


    // Spring Data JPA의 정렬 및 페이징
    // Page 클래스 사용 시, Spring Data JPA에서는 본 쿼리 (컨텐츠 조회)와 totalCount 쿼리가 자동으로 조회됨
    // @Query의 본 쿼리(컨텐츠 조회)에서 조인 등 복잡한 쿼리로 되어있을 시, count 쿼리도 복잡한 쿼리로 조회되기 때문에 성능저하가 되므로 따로 명시 (실무에서 중요!)
    @Query(value="select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    // Page를 Slice로 변환
    //Slice<Member> findByAge(int age, Pageable pageable);


    // 벌크성 수정 쿼리 : 한 건 한 건 조회하여 그 데이터를 업데이트하기 보다는 전체 데이터 (테이블)에 대해서 업데이트를 해야할 경우
    // bulk 수정 쿼리를 수행하게되면 바로 DB에 update를 치기때문에 영속성 컨텍스트는 이를 모르는 상태이다.
    // 이 상태에서 테스트를 해보면 콘솔에는 update 전 데이터가 나온다.
    // 따라서 bulk update 수행 후 영속성 컨텍스트를 날려버려야한다. (claarAutomatically = true)
    // JPQL은 DB에 먼저 플러쉬 후 JPQL이 실행된다. (bulk 연산 예외)
    // JPA와 Mybatis를 섞어서 사용할 경우 이러한 JPA는 Mybatis에서 한 연산을 인식못하기 때문에 클리어 작업이 필요함.
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m" +
            "  SET m.age = m.age + 1" +
            "WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    // fetch join (복잡한 쿼리 및 기능의 경우 JPQL + fetch join 사용)
    // fetch join : Member 엔티티를 조회할때 연관된 다른 엔티티 Team도 한번에 조회하는 것(지연로딩의 문제점인 N+1 문제 해결)
    @Query("SELECT m" +
            " FROM Member m" +
            " LEFT JOIN FETCH m.team")
    List<Member> findMemberFetchJoin();


    // 매번 fetch join(JPQL)을 적는게 번거롭다면? (또는 간단한 조회 등 이런 경우 @EntityGraph + attributePaths 사용)
    // 메소드 쿼리 등 @Query 사용하지 않는데, fetch join까지 깔끔하게 사용하고 싶다! ==> @EntityGraph
    @Override   // 상위 인터페이스 JPARepository의 findAll 메서드를 재정의
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // @EntityGraph 다른 사용 형태 1  -> JPQL fetch join 명시 안하는 경우
    @Query("SELECT m" +
            " FROM Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraph();

    // @EntityGraph 다른 사용 형태 2 -> 메소드 쿼리 사용의 경우
    @EntityGraph(attributePaths = {"team"})
    List<Member> findKtuByUsername(@Param("username") String username);

    // @EntityGraph 다른 사용 형태 3 -> Named Entitiy Graph (Member 엔티티의 @NamedEntityGraph 참고)
    @EntityGraph("Team.all")
    List<Member> findNaemdEGByUsername(@Param("username") String username);


    // JPA Query Hint
    // 진짜 복잡한 쿼리나 API를 사용할때만 최적화를 위해 넣는 것이지, 조회 기능에 무조건 다 넣어봤자 최적화가 크게 안된다. 성능테스트 후에 넣을지 말지 결정!
    @QueryHints(value= @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // JPA Query Lock == select for update (LockModeType.PESSIMISTIC_WRITE --> JAVA JPA에서 제공
    // select for update : DB 행 잠금 기술, 해당 트랜잭션이 종료될 때까지 다른 트랜잭션이 해당 레코드에 대한 수정을 방지함.
    // 실시간 트래픽을 서비스하는 애플리케이션에선 잘 사용하지 않음.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
