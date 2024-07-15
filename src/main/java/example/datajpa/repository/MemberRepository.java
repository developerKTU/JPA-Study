package example.datajpa.repository;

import example.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository 인터페이스만 상속받아주면 (상속받는것도 인터페이스) 구현체를 Spring Data JPA가 다 제공해준다. (Spring Data JPA의 힘!)
// JPARepository 인터페이스를 상속받으면 @Repository 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 원하는 조회조건으로 SELECT (JpaRepository 인터페이스에서 지원하는 이름으로 메소드 작성)
    // findBy : 조회한다, Username : equal 조건, And : and, AgeGreaterThan : age가 파라미터보다 큰 조건
    // findByUsername2AndAgeGreaterThan 이런식으로 짓는다면 에러 (No property username2 for Type Member!)
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html 자세한 내용은 링크참조
    // 조건 2개정는 이 방법으로 하지만, 2개가 넘어간다면 메소드 명이 너무 길어져 코드가 보기 싫어짐! (2개 넘어가면 다른방법!)
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 쿼리 메소드 기능

    // Limit 조건
    List<Member> findTop3By();
}
