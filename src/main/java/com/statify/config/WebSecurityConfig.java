package com.statify.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers("/", "/login**", "/header.html").permitAll() // Allow access to the pages without authentication
                    .anyRequest().authenticated() // Require authentication for all other requests
                .and()
                    .logout() // Enable logout support
                    .logoutUrl("/logout?login") // Specify the logout URL
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true) // Очищаем сессию
                    .clearAuthentication(true) // Очистка аутентификации
                    .deleteCookies("JSESSIONID")
                    .permitAll(); // Allow access to the logout page without authentication
    }



}