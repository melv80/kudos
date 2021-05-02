package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class JPAUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userService;

    @Autowired
    private AppConfig config;

    private UserDetails toUserDetails(com.kudos.server.model.jpa.User user) {

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return toUserDetails(userService.findUserByMail(username));
    }
}
