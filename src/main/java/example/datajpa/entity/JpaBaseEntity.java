package example.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 실제 상속은 아니고 필드의 데이터만 해당 엔티티(테이블)로 내려서 공유
// 이 어노테이션을 명시해주어야 등록 및 수정일이 테이블에 기록된다.
@MappedSuperclass

// 순수 JPA의 Auditing
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

}
