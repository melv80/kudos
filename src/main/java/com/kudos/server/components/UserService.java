package com.kudos.server.components;

import com.kudos.server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface UserService {

  void addUser(String name, String email, String password);

  void grantRole(long userID, String role);

  Set<String> getRoles(long userID);
}
