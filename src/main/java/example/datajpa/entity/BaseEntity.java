package example.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
// 등록일, 수정일만 필요하면 BaseTimeEntity만 상속받아서 사용
// 등록일, 수정일, 등록자, 수정자 모두 사용하고싶으면 BaseEntity 상속받아서 사용 -> 실무에서 사용하는 기법
public class BaseEntity extends BaseTimeEntity{

    // 등록자, 수정자는 메인 클래스에 빈을 등록해줘야한다.
    // 등록자
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    // 수정자
    @LastModifiedBy
    private String lastModifiedBy;
}
