package example.datajpa.repository;

import example.datajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}