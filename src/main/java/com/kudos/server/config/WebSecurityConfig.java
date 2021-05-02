package com.kudos.server.config;

import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private Logger logger = LoggerFactory.getLogger("users");

  @Autowired
  private AppConfig config;
  
  @Autowired
  private UserRepository userRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(  "/login", "/css/**", "/js/**", "/icons/**").permitAll()
                .anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.and()
			.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login");
  }


  @Bean
  @Override
  public InMemoryUserDetailsManager userDetailsService() {
    Set<UserDetails> allUsers = userRepository
        .findAll()
        .stream()
        .map(this::toUserDetails)
        .collect(Collectors.toSet());
    return new InMemoryUserDetailsManager(allUsers);
  }

  private UserDetails toUserDetails(com.kudos.server.model.jpa.User user) {
    logger.info("adding user: "+user.getEmail());

    boolean isAdmin = user.getName().equalsIgnoreCase("admin");

    User.UserBuilder userBuilder = User
        .withUsername(user.getEmail())
        .password(isAdmin ? config.getPasswordHash() : user.getPasswordHash());

    if (isAdmin) {
      userBuilder.roles("ADMIN", "USER");
    }
    else {
      userBuilder.roles("USER");
    }
    return userBuilder.build();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring().antMatchers("/resources/**");
  }
}