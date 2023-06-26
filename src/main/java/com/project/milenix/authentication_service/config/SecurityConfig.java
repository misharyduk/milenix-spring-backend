package com.project.milenix.authentication_service.config;

import com.project.milenix.authentication_service.filter.AuthenticationFilter;
import com.project.milenix.authentication_service.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;
import static com.project.milenix.authentication_service.model.AppRole.*;
import static com.project.milenix.authentication_service.model.AppAuthority.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtFilter(), AuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .anonymous().and()
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/api/v1/articles/hot/**").permitAll()
//                        .anyRequest().authenticated())
                .authorizeHttpRequests()
                .requestMatchers(GET, "/api/v1/articles/hot",
                        "/api/v1/articles/**", "/api/v1/articles/pagination",
                        "/api/v1/articles/search", "/api/v1/categories/**",
                        "/api/v1/categories/pagination", "/api/v1/categories/search/**",
                        "/api/v1/users/**", "/api/v1/users/pagination", "/api/v1/users/search").permitAll()
                .requestMatchers(POST, "/login/**", "/api/v1/auth/token/refresh/**", "/api/v1/auth/register/**").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .httpBasic(Customizer.withDefaults());
//                .httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails linda = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password"))
//                .authorities(USER.getGranterAuthorities())
//                .build();
//
//        UserDetails tom = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password"))
//                .authorities(ADMIN.getGranterAuthorities())
//                .build();
//
//        UserDetails nick = User.builder()
//                .username("nick")
//                .password(passwordEncoder.encode("password"))
//                .authorities(MODERATOR.getGranterAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(linda, tom, nick);
//    }
}
