package com.dandy.sboot_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // role -> grupo de usuário (perfil de usuário) Master, gerente, frente de loja, vendedor
    // authoriry -> permissões - cadastrar usuario, acessar tela de relatorio

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SenhaMasterAuthenticationProvider senhaMasterAuthenticationProvider,
                                                   CustomFilter customFilter) throws Exception {
        return http
                .authorizeHttpRequests(customizer ->{
                    customizer.requestMatchers("/public").permitAll();
                    customizer.requestMatchers("/admin").hasRole("ADMIN");
                    customizer.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(senhaMasterAuthenticationProvider)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails commonUser = User.builder()
                .username("user")
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(commonUser, adminUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //Para definir o prefix default e não precisar informar o prefixo ROLE_
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }
}
