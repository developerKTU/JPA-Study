package example.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
// SpringBoot 이므로 아래 어노테이션은 생략해도 됨
// @EnableJpaRepositories(basePackages = "example.datajpa.repository")
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    // 등록자, 수정자 데이터를 넣기 위해 랜덤 UUID 생성
    @Bean
    public AuditorAware<String> auditorProvider(){
        // 람다식
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
