package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.example.backend")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()// for
                .antMatchers("/user/login","/user/createUser", "/verify", "/favorite/addToFavorite", "/favorite/getAllFavorites", "/favorite/removeFromFavorites").permitAll()//
                .and()
                .authorizeRequests()
                .antMatchers("/**")//
                .authenticated()// everythings else need authentication
                .and()
                .exceptionHandling()
                .and()//
                .csrf().disable()
                .addFilterBefore(jwtFilter(), BasicAuthenticationFilter.class)
        ;
        http.cors()
                .and()
                .csrf()
                .disable();
    }


    @Bean
    public JwtAuthenticationFilter jwtFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
