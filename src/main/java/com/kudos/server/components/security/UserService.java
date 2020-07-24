package com.kudos.server.components.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashSet;
import java.util.Set;

public class UserService extends InMemoryUserDetailsManager {
  private final Set<String> deletedUsers = new HashSet<>();
  private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


  @Override
  public void createUser(UserDetails user) {
    // nop
  }

  @Override
  public void deleteUser(String username) {
    deletedUsers.add(username);
  }

  @Override
  public UserDetails updatePassword(UserDetails user, String newPassword) {
    return User.withUserDetails(user).password(newPassword).build();
  }

  @Override
  public boolean userExists(String username) {
    return !deletedUsers.contains(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if ("admin".equalsIgnoreCase(username)) {
      return
          User.builder()
          .username("admin")
          .passwordEncoder(encoder::encode)
          .password("xxx")
          .roles("USER", "ADMIN")
          .build();
    }
    else {
      throw new UsernameNotFoundException("login failed");
    }
  }
}
