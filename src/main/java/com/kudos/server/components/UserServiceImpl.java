package com.kudos.server.components;


import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public void addUser(String name, String email, String password) {
    User user = new User(name, email, password);
    userRepository.save(user);
  }

  @Override
  public void grantRole(long userID, String role) {
    Optional<User> userOptional = userRepository.findById(userID);
    if (!userOptional.isPresent()) throw new IllegalStateException();

    User user = userOptional.get();
    Set<String> roles = getRoles(user);
    roles.add(role);
    user.setRoles(String.join(",", roles));
    userRepository.save(user);
  }

  @Override
  public Set<String> getRoles(long userID) {
    final Optional<User> byId = userRepository.findById(userID);
    if (!byId.isPresent()) return Collections.emptySet();
    String[] split = byId.get().getRoles().split(",");
    return new HashSet<>(Arrays.asList(split));
  }

  private Set<String> getRoles(User user) {
    return new HashSet<>(Arrays.asList(user.getRoles().split(",")));
  }
}
