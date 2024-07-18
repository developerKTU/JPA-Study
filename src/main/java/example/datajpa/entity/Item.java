package example.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity implements Persistable<String> {

    // 새로운 엔티티 판단 --> pk값의 식별자가 객체 타입일때 : null
    //                     pk값의 식별자가 기본 타입일때 : 0
    // @Id @GeneratedValue
    // private Long id;        // @GeneratedValue가 있으므로 save 시점에서 id값은 null

    @Id
    private String id;

    public Item(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }


    // 실무에서는 이런식으로 id값이 아닌 데이터 생성일자(Auditing)로 새로운 엔티티 여부(null 여부) 판단!
    // 데이터 생성일자가 null이면 데이터를 넣은적 없는 새로운 엔티티이므로 판단하기 좋음
    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
