package co.edu.iub.myfinalproject.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf(
            "http://localhost:5500",
            "http://127.0.0.1:5500"
        )
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {it.configurationSource(corsConfigurationSource())}
            .csrf {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/")
                    .permitAll()
                it.requestMatchers(HttpMethod.POST, "/auth/login")
                    .permitAll()
                it.requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                )
                    .permitAll()
                it.requestMatchers("/users/**")
                    .hasRole("ADMIN")
                it.anyRequest()
                    .authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint { request, response, _ ->
                    response.contentType = "application/json"
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write(
                        """{"timestamp":"${java.time.LocalDateTime.now()}","status":401,"error":"Unauthorized","message":"No autenticado","path":"${request.requestURI}"}"""
                    )
                }
                it.accessDeniedHandler { request, response, _ ->
                    response.contentType = "application/json"
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.writer.write(
                        """{"timestamp":"${java.time.LocalDateTime.now()}","status":403,"error":"Forbidden","message":"No tiene permisos suficientes","path":"${request.requestURI}"}"""
                    )
                }
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .httpBasic {
                it.disable()
            }
            .formLogin {
                it.disable()
            }
            .build()
    }
}