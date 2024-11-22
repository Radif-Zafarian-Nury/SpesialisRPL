package com.example.spesialisRPL;

import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    //utk configure login forms, authorization rules dll
    public SecurityFilterChain securityFilterChain(HttpSecurity HttpSecurity) throws Exception{
        return httpSecurity
                //kasihtau spring, suruh pake login page yg kita bikin buat loginnya
                .formLogin(httpForm -> {
                    httpForm
                        .loginPage("/login").permitAll();
                })

                //authorize buat register/singup
                .authorizeHttpRequests(register -> {
                    registry.requestMatchers("/req/signup").permitAll(); //urlnya sesuain
                    registry.anyRequest().authenticated(); //biar si user bisa access page ini walaupun blom login
                })
                
                .build()
    }
}
