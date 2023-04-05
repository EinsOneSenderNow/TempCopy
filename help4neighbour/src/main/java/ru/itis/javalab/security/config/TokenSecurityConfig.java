package ru.itis.javalab.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.javalab.security.details.UserDetailsServiceImpl;
import ru.itis.javalab.security.filters.JwtAuthenticationFilter;
import ru.itis.javalab.security.filters.JwtAuthorizationFilter;
import ru.itis.javalab.security.provider.RefreshTokenAuthenticationProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class TokenSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security,
                                                   JwtAuthorizationFilter jwtAuthorizationFilter,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        security.csrf().disable();

        security.authorizeRequests()
                .antMatchers("/cheques/**").authenticated()
                .antMatchers("/swagger-ui.html/**").permitAll();

        security.addFilter(jwtAuthenticationFilter);
        security.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
        builder.authenticationProvider(provider);
    }
}
