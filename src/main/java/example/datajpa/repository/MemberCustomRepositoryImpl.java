package example.datajpa.repository;

import example.datajpa.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
// 구현 클래스 이름 오타나면 에러남. 네이밍은 <<Entity명 + Impl>> 또는 <<사용자정의 인터페이스명 + Impl>>로 해야함.
// (오타나서 JUnit 테스트 오류남)
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    // 순수 JPA 구현하고 싶으면 이렇게 사용하거나 Mybatis를 쓰고 싶으면 라이브러리 임포트해서 사용
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
