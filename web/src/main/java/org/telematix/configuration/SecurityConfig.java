package org.telematix.configuration;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.telematix.security.JwtFilter;
import org.telematix.services.UserDetailService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final AuthenticationEntryPoint UNAUTHORIZED = (request, response, authException) ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    private final JwtFilter jwtFilter;
    private final UserDetailService userDetailService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailService userDetailService) {
        this.jwtFilter = jwtFilter;
        this.userDetailService = userDetailService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
                .userDetailsService(userDetailService)
                .exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()
                .antMatchers(
                        HttpMethod.POST,
                        "/api/register",
                        "/api/login"
                ).permitAll()
                .antMatchers("/api/users", "/api/user/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
