package edu.example.restz.config;

import edu.example.restz.security.filter.JWTCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class CustomSecurityConfig {
    private JWTCheckFilter jwtCheckFilter;  //setter injection

    @Autowired
    public void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter) {
        this.jwtCheckFilter = jwtCheckFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login ->  login.disable())   //로그인폼 X
                .logout( logout -> logout.disable())    //로그아웃 X
                .csrf( csrf -> csrf.disable())          //CSRF는 세션단위 관리 -> X
                .sessionManagement( sess                //세션 사용 X
                        -> sess.sessionCreationPolicy(SessionCreationPolicy.NEVER));

        //JWTCheckFilter 필터 추가
        http.addFilterBefore(jwtCheckFilter,
                             UsernamePasswordAuthenticationFilter.class);

        http.cors(cors -> {
            cors.configurationSource(corsConfigurationSource());
        });

        return http.build();
    }

    //CORS ; Cross Origin Resource Sharing설정 관련 처리 ---------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();

        //접근 패턴 - 모든 출처에서의 요청 허락
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        corsConfig.setAllowedMethods(           //허용 메서드
                List.of("GET", "POST", "PUT", "DELETE"));

        corsConfig.setAllowedHeaders(           //허용 헤더
                List.of("Authorization",
                        "Content-Type",
                        "Cache-Control"));

        corsConfig.setAllowCredentials(true);   //자격 증명 허용 여부

        //URL 패턴 기반으로 CORS 구성
        UrlBasedCorsConfigurationSource corsSource
                = new UrlBasedCorsConfigurationSource();

        corsSource.registerCorsConfiguration("/**",    //모든 경로 적용
                                             corsConfig);

        return corsSource;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}


/*

test.html에서 로그인 시도 - user99
>>> local storage에 액세스 토큰 저장 확인

login.html에서 로그인 시도 - 임의의 아이디
>>> 쿠키에 액세스 토큰, 리프레시 토큰, mid 저장 확인


 */









