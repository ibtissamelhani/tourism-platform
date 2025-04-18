package org.ibtissam.dadesadventures.config;

import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String participationLink = "/api/v1/participation/**";
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(
                        authorizeHttpRequest -> {
                            authorizeHttpRequest.requestMatchers("/api/v1/auth/**")
                                    .permitAll();
                            authorizeHttpRequest.requestMatchers(HttpMethod.GET,"/api/v1/activities")
                                    .permitAll();
                            authorizeHttpRequest.requestMatchers(HttpMethod.GET,"/api/v1/categories")
                                    .permitAll();
                            authorizeHttpRequest
                                    .requestMatchers(HttpMethod.GET, "/api/v1/activities/search").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/v1/activities/search").permitAll();
                            authorizeHttpRequest.requestMatchers("/uploads/**").permitAll();
                            authorizeHttpRequest.anyRequest().authenticated();
                        }
                ).sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
