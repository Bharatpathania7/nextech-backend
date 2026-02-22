//package com.tradigo.tradigo_backend.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                // ✅ Disable CSRF (we use JWT)
//                .csrf(csrf -> csrf.disable())
//
//                // ✅ Disable CORS (Flutter mobile does NOT need CORS)
//                .cors(cors -> cors.disable())
//
//                // ✅ Stateless session (JWT)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//                // ✅ Authorization rules
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()   // Login & Register open
//                        .anyRequest().authenticated()              // Everything else protected
//                )
//
//                // ✅ Add JWT filter
//                .addFilterBefore(jwtAuthFilter,
//                        UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}

package com.tradigo.tradigo_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ✅ Disable CSRF (JWT used)
                .csrf(csrf -> csrf.disable())

                // ✅ ENABLE CORS for Flutter Web
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // 🔥 Allow all origins (DEV MODE)
                    config.setAllowedOriginPatterns(List.of("*"));

                    config.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    );

                    config.setAllowedHeaders(List.of("*"));

                    config.setAllowCredentials(true);

                    return config;
                }))

                // ✅ Stateless (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ✅ Authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/referral/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // VERY IMPORTANT
                        .anyRequest().authenticated()
                )

                // ✅ JWT Filter
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}