package example.datajpa.repository;

import example.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository 인터페이스만 상속받아주면 (상속받는것도 인터페이스) 구현체를 Spring Data JPA가 다 제공해준다. (Spring Data JPA의 힘!)
// JPARepository 인터페이스를 상속받으면 @Repository 생략 가능
public interface TeamRepository extends JpaRepository<Team, Long> {
}
