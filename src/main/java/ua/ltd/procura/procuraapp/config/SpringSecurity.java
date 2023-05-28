package ua.ltd.procura.procuraapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.ltd.procura.procuraapp.constants.UriStorage;
import ua.ltd.procura.procuraapp.dto.UserOAuth2Dto;
import ua.ltd.procura.procuraapp.service.UserService;
import ua.ltd.procura.procuraapp.service.impl.UserOAuth2Service;

import static ua.ltd.procura.procuraapp.constants.UriStorage.LOGIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final UserOAuth2Service oauthUserService;

    private static final String[] allowedStaticUri = {"/css/**", "/js/**", "/img/**", "/favicon.ico"};
    private static final String[] allowedUri = {"/register/**", "/index", "/login", "/oauth/**"};

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> {
                    try {
                        csrf
                                .disable()
                                .authorizeHttpRequests(
                                        authorize -> authorize
                                                .requestMatchers(allowedUri).permitAll()
                                                .requestMatchers("/users").hasRole("ADMIN")
                                ).authorizeHttpRequests(
                                        authStatic -> authStatic
                                                .requestMatchers(allowedStaticUri).permitAll()
                                ).oauth2Login(
                                        oauth -> oauth
                                                .loginPage(LOGIN)
                                                .userInfoEndpoint(config -> config.userService(oauthUserService))
                                                .successHandler((request, response, authentication) -> {
                                                    UserOAuth2Dto oauthUser = (UserOAuth2Dto) authentication.getPrincipal();
                                                    userService.processOAuthPostLogin(oauthUser.getName(), oauthUser.getEmail());
                                                    response.sendRedirect(UriStorage.USERS);
                                                })
                                                .permitAll()
                                ).formLogin(
                                        form -> form
                                                .loginPage(LOGIN)
                                                .defaultSuccessUrl(UriStorage.USERS, true)
                                                .permitAll()
                                ).logout(
                                        logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher(UriStorage.LOGOUT))
                                                .permitAll()
                                );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}
