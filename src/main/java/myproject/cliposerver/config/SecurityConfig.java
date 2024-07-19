package myproject.cliposerver.config;

import lombok.RequiredArgsConstructor;
import myproject.cliposerver.config.jwt.JwtAccessDeniedHandler;
import myproject.cliposerver.config.jwt.JwtAuthenticationEntryPoint;
import myproject.cliposerver.config.jwt.JwtFilter;
import myproject.cliposerver.config.jwt.JwtTokenUtil;
import myproject.cliposerver.config.security.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig {
    private final JwtTokenUtil jwtTokenUtil;
    private final CorsConfig corsConfig;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //form 로그인 방식 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);

        //HTTP Basic 인증 방식 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);

        //세션 STATELESS
        // 시큐리티는 기본적으로 세션을 사용
        // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //cors 설정
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfig.corsFilter()));

        //경로별 인가작업
        http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        ;
        return http.build();
    }

}
