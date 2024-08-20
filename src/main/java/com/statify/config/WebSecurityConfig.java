package com.statify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login**", "/logout", "/oauth2/**", "/static/**", "/header.html", "/footer.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestResolver(customAuthorizationRequestResolver())
                .and()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "SESSION", "SESSIONID")
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository());
    }

    @Bean
    public static ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration spotifyRegistration = getSpotifyClientRegistration();
        return new InMemoryClientRegistrationRepository(spotifyRegistration);
    }

    private static ClientRegistration getSpotifyClientRegistration() {
        return ClientRegistration.withRegistrationId("spotify")
                .clientId("a0afdb6374de488da6dfdd578b2b150e")
                .clientSecret("53500a989aba491faff04a9706bed7cf")
                .scope("user-read-private", "user-read-email", "user-top-read", "user-read-recently-played", "user-read-currently-playing", "playlist-modify-public", "playlist-modify-private")
                .authorizationUri("https://accounts.spotify.com/authorize")
                .tokenUri("https://accounts.spotify.com/api/token")
                .userInfoUri("https://api.spotify.com/v1/me")
                .userNameAttributeName("id")
                .clientName("Spotify")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
                .build();
    }
}