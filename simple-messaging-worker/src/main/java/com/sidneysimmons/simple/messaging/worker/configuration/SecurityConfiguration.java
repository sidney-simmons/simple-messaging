package com.sidneysimmons.simple.messaging.worker.configuration;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Security configuration.
 * 
 * @author Sidney Simmons
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;

    private static final String ACTUATOR_ROLE = "ACTUATOR";

    /**
     * The /actuator/* endpoints require a specific role. All other endpoints don't require
     * authentication. All CSRF is disabled.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/actuator/*", "/cache-stats/*").hasRole(ACTUATOR_ROLE).anyRequest().permitAll().and()
                .httpBasic().and().csrf().disable();
    }

    /**
     * Use BCrypt as the password encoder.
     * 
     * @return the password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Setup a user details service with a predefined collection of users.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(buildUsers());
    }

    /**
     * Build a collection of users and roles.
     * 
     * @return a collection of users and roles
     */
    private Collection<UserDetails> buildUsers() {
        // Actuator user
        String actuatorUsername = environment.getProperty("user.actuator.username");
        String actuatorPassword = environment.getProperty("user.actuator.password");
        String actuatorRole = environment.getProperty("user.actuator.role");
        Collection<GrantedAuthority> actuatorAuthorities = new ArrayList<>();
        actuatorAuthorities.add(new SimpleGrantedAuthority("ROLE_" + actuatorRole));
        UserDetails user = new User(actuatorUsername, actuatorPassword, actuatorAuthorities);

        // Return all the users
        Collection<UserDetails> userDetails = new ArrayList<>();
        userDetails.add(user);
        return userDetails;
    }

}