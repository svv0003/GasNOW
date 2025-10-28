package project.gasnow.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.gasnow.common.interceptor.AdminInterceptor;
import project.gasnow.common.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 일반 회원이 로그인할 때 접속할 수 있는 API 리스트
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(       // 일반 회원이 추가로 접속할 수 있는 API 주소들
                        "/mypage"       // 마이 페이지
                )
                .excludePathPatterns(   // 누구든지 들어갈 수 있는 API 주소들
                        "/",            // 메인 페이지
                        "/map",         // 지도 페이지
                        "/login",       // 로그인 페이지
                        "/register",    // 회원가입 페이지
                        "/css/**",      // CSS 파일
                        "/js/**"        // JS 파일
                )
        ;

        // 관리자 회원이 로그인할 때 추가로 접속할 수 있는 API 리스트
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns(

                )
        ;
    }
}