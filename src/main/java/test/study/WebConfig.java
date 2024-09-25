package test.study;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import test.study.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/members"); // 로그인 할때랑 회원가입 할 때는 로그인체크 인터셉트 작동 안함
    }

    // CORS 문제 해결 (CORS 는 다른 출처 리소스 공유)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS를 적용할 URL 패턴을 정의
                .allowedOrigins("http://localhost:5173") // 자원 공유를 허락할 Origin을 지정
                .allowedMethods("OPTIONS","GET","POST","DELETE") // 허용할 HTTP method를 지정
                .allowCredentials(true) // 쿠키를 포함한 인증 정보 전달을 허용
                .allowedHeaders("Authorization", "Content-Type", "Accept") // 클라이언트 측의 CORS 요청에 허용되는 헤더를 지정
                .maxAge(600);
        // OPTIONS메서드는 서버와 브라우저가 통신하기 위한 통신 옵션을 확인하기 위해 사용(서버가 어떤 method, header, content type을 지원하는지를 알 수 있다)
        // 브라우저가 요청할 메서드와 헤더를 허용하는지 미리 확인한 후, 서버가 지원할 경우에 통신
    }
}
