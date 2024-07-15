package example.datajpa.repository;

import example.datajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 인터페이스만 상속받아주면 (상속받는것도 인터페이스) 구현체를 Spring Data JPA가 다 제공해준다. (Spring Data JPA의 힘!)
public interface MemberRepository extends JpaRepository<Member, Long> {

}
