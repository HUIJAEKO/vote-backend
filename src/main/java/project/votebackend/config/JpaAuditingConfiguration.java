package project.votebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
//datetime 생성 및 수정 시 자동 업데이트
public class JpaAuditingConfiguration {
}
