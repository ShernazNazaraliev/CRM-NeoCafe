package com.example.NeoCafe.config;

import com.example.NeoCafe.Enums.ERole;
import com.example.NeoCafe.entity.User;
import com.example.NeoCafe.jwt.JwtRequestFilter;
import com.example.NeoCafe.repository.UserRepository;
import com.example.NeoCafe.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl myUserServiceImpl;

    private final JwtRequestFilter jwtRequestFilter;

    private final UserRepository userRepository;

    public WebSecurityConfig(UserDetailsServiceImpl myUserServiceImpl, JwtRequestFilter jwtRequestFilter, UserRepository userRepository) {
        this.myUserServiceImpl = myUserServiceImpl;
        this.jwtRequestFilter = jwtRequestFilter;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/login",
                        "/client/registration",
                        "/client/activate",
                        "/client/auth",
                        "client/login").permitAll()
                .antMatchers("/admin/*").hasRole(String.valueOf(ERole.ROLE_ADMIN.name))
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public void addAdmin(){
        User user = new User();

        if (!userRepository.existsByPhoneNumber("admin")) {
            user.setPhoneNumber("admin");
            user.setRole(ERole.getRole(1));
            user.setActive(true);
            user.setCompleted(true);
            user.setActivationCode("$2a$12$HZAXhyLTr9r1tS7/JPPOXO.NuXCB9a2KXM7o0OW0ZK40uLPfzdB.6");
            userRepository.save(user);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(myUserServiceImpl).passwordEncoder(passwordEncoder());
    }
}