package ru.wostarnn.nxbootcampbss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Задано два пользователя: пользователь с правами доступа абонента
// и пользователь с правами доступа абонента и менеджера

// Не смог разобраться, как правильно использовать JdbcUserDetailsManager
// для того, чтобы можно было читать пользователей из БД
// Теоритически, мог создать свой класс для реализации данного функционала,
// Но решил не уделять этому много времени и сосредоточиться на других задачах,
// Так что оставил хардкод :D

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("abonent")
                .password(passwordEncoder.encode("abonent"))
                .roles("abonent")
                .build());
        manager.createUser(User.withUsername("manager")
                .password(passwordEncoder.encode("manager"))
                .roles("manager", "abonent")
                .build());
        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/abonent/**").hasRole("abonent")
                                .requestMatchers("/manager/**").hasRole("manager")
                                .anyRequest().authenticated()
                        )
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
