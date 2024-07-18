package example.datajpa.dto;

import example.datajpa.entity.Member;
import lombok.Data;

@Data
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;

    public MemberDTO(Long id, String username, String teamName){
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // DTO는 Entity를 바라봐도 된다. (의존관계)
    // Entity는 애플리케이션 내에서 공통으로 모두 볼 수 있는 객체이기 때문에 DTO는 Entity를 참조해도 괜찮다.
    // 하지만 반대로 Entity는 공통객체의 개념이기 때문에, 가급적 DTO를 참조해선 안된다. (단 Entity를 필드멤버로 넣으면 안됨!)
    public MemberDTO(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }

}
