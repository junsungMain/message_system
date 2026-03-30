package net.prastars.messagesystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.prastars.messagesystem.auth.RestApiLoginAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;

@Configuration
@SuppressWarnings("unused")
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService detailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(detailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        RestApiLoginAuthFilter restApiLoginAuthFilter = new RestApiLoginAuthFilter(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/login"), authenticationManager);

        httpSecurity.csrf(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).addFilterAt(restApiLoginAuthFilter, UsernamePasswordAuthenticationFilter.class).authorizeHttpRequests(
                auth -> auth.requestMatchers("/api/v1/auth/register","/api/v1/auth/login", "/ws/v1/connect").permitAll().anyRequest().authenticated())
                .logout(
                        logout -> logout.logoutUrl("/api/v1/auth/logout").logoutSuccessHandler(this::logoutHandler));

        return httpSecurity.build();
    }

    private void logoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding("UTF-8");
        String message;

        if (authentication != null && authentication.isAuthenticated()) {
            response.setStatus(HttpStatus.OK.value());
            message = "로그아웃 성공";
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            message = "로그아웃 실패";
        }

        try {
            response.getWriter().write(message);
        } catch (IOException ex) {
            log.error("전송 실패. 원인: {}", ex.getMessage());
        }
    }
}
