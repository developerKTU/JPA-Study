package example.datajpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 연관관계 필드(외래키, 참조키 등)는 되도록 명시 x
@ToString(of = {"id", "username","age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    // GeneratedValue 어노테이션 -> JPA가 알아서 pk값을 시퀀스를 생성해주는 등 순차적 값을 생성해줌
    private Long id;
    private String username;
    private int age;

    // JPA에서 모든 연관관계는 다 지연로딩 (LAZY)로 설정한다.(실무에도 적용) -> EAGER로 하면 성능최적화가 매우 힘들다
    // 지연로딩 : 클래스의 멤버만 일단 먼저 조회 (id, username, age), team은 가짜 객체로 가지고 있다가. team을 조회할때 그때 쿼리 한번 더 태움
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    // JPA 표준 스팩에 따르면 Entity에서는 기본 생성자가 있어야함.
    // 기본 생성자는 protected까지 열어놔야함 -> JPA 구현체(Hibernate)가 proxy 기술 등을 수행할때 private로 막아버리면 접근할 수 없기 때문
    // 어노테이션으로 대체
    //protected Member(){}

    public Member(String username){
        this.username = username;
    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team){
        this.username = username;
        this.age = age;

        if(team != null){
            changeTeam(team);
        }
    }

    // 팀이 변결되었을 경우, 연관 되어있는 다른 테이블의 team도 같이 변경
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    // Setter 대신 이런식으로 사용가능
    public void changeUserName(String username){
        this.username = username;
    }

}
