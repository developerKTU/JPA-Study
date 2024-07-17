package example.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
// Spring Data JPA의 Auditing
// 메인클래스 (DataJpaApplication)에 @EnableJpaAuditing 어노테이션 명시해줘야함.
public class BaseTimeEntity {

    // 등록일
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    // 수정일자
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
