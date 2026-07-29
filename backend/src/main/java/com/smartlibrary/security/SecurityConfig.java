package com.smartlibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration.
 *
 * <p>Stateless JWT auth: no server session, a custom filter validates the token
 * on each request, and {@code @EnableMethodSecurity} unlocks {@code @PreAuthorize}
 * on service/controller methods for fine-grained role checks.</p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthEntryPoint jwtAuthEntryPoint,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh.authenticationEntryPoint(jwtAuthEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        // public
                        .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/books/**",
                                "/categories/**",
                                "/reviews/**"
                        ).permitAll()
                        .requestMatchers("/uploads/**", "/v3/api-docs/**", "/swagger-ui/**",
                                "/swagger-ui.html", "/h2-console/**", "/actuator/health").permitAll()
                        // writes on books/categories require staff roles
                        .requestMatchers(HttpMethod.POST, "/books/**", "/categories/**")
                        .hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/books/**", "/categories/**")
                        .hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/books/**", "/categories/**")
                        .hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**", "/categories/**")
                        .hasAnyRole("ADMIN", "LIBRARIAN")
                        // any authenticated user can read their own profile
                        .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                        // user management is admin-only
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        // dashboard for staff
                        .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "LIBRARIAN")
                        // everything else needs authentication
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // allow H2 console frames in dev
        http.headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()));

        return http.build();
    }
}
