package com.sapient.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                // We use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle unauthorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
                .authorizeRequests()
                // all are allowed to access the auth service through POST method
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                .antMatchers(HttpMethod.POST, "/create").permitAll()
                .antMatchers("/create/list-all").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/funds/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/add/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/delete/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/addEntitlements/**").hasRole("ADMIN")
                .antMatchers("/portfolio/update/baseCurrency**").hasRole("TRADER")
                .antMatchers("/portfolio/update/**").hasAnyRole( "TRADER", "VIEWER")
                .antMatchers("/portfolio/add/user**").hasAnyRole( "TRADER", "VIEWER")
                .antMatchers("/portfolio/delete/**").hasRole( "ADMIN")
                .antMatchers("/portfolio/**").hasRole( "TRADER")
                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

}
