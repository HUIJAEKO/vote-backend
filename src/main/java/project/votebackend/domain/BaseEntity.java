package project.votebackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate  // 생성 시간 자동 저장
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate  // 마지막 수정 시간 자동 저장
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
