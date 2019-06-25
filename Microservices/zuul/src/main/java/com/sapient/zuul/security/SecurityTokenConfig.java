package com.sapient.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
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
                // all are allowed to access the auth service
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                .antMatchers("/trade/exchange/**").hasAnyRole("ADMIN", "TRADER")
                .antMatchers("/fund-handling/api/funds/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/add/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/delete/**").hasRole("ADMIN")
                .antMatchers("/fund-handling/api/entitlements/search**").hasAnyRole("ADMIN", "TRADER")
                .antMatchers("/fund-handling/api/entitlements/get**").hasAnyRole("ADMIN", "TRADER")
                .antMatchers( "/gallery/viewer/**").hasAnyRole("USER", "ADMIN", "TRADER")
                .antMatchers( "/gallery/trader/**").hasAnyRole("TRADER","ADMIN")
                .antMatchers("/gallery/admin/**").hasRole("ADMIN")
                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}
