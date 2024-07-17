package example.datajpa.repository;

import example.datajpa.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
// 이처럼 사용자 정의 레포지토리는 복잡한 쿼리라고 무작정 사용하는 것이 아니고 비즈니스 로직이나, 화면에 따라서 등등
// 로직에 따라 클래스를 따로 만들어주는 것이 깔끔하고 아케텍쳐 상으로도 알맞는 코드가 된다.
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMember(){
        return em.createQuery("select m from Member m where m.username = '복잡한 쿼리는 이쪽에서...'")
                .getResultList();
    }

}
