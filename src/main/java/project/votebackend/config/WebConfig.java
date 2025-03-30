package project.votebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 현재 실행 경로 기준으로 images 폴더를 /images/** 경로로 접근할 수 있게 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");
    }
}
