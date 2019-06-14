package com.ajulay.config;

import com.ajulay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
//                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/", true)
                //.failureUrl("/login.html?error=true")
//                .failureHandler(authenticationFailureHandler())
                .and()
                .rememberMe()
                .and()
                .logout()
//                .logoutUrl("/")
                .deleteCookies("JSESSIONID")
//                .logoutSuccessHandler(logoutSuccessHandler());
//         .and()
//        .logout()
//        .permitAll()
        ;
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("u")
//                        .password("p")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
        super.configure(web);
    }

    @Override
//               jdbcAuthentication()
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
//               .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
//               .passwordEncoder(NoOpPasswordEncoder.getInstance())
//               .usersByUsernameQuery("select username, password, active from \"user\" where username=?")
//               .authoritiesByUsernameQuery("select u.username, ur.roles from \"user\" u inner join user_role ur on u.id =ur.user_id where u.username=?")
        ;
    }
}