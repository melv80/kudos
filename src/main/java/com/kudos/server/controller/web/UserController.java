package com.kudos.server.controller.web;


import com.kudos.server.components.UserService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.controller.Util;
import com.kudos.server.model.dto.ui.Identifiable;
import com.kudos.server.model.dto.ui.UserDTO;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.UserRepository;
import org.dom4j.util.UserDataAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class UserController {



  private static final Logger logger = LoggerFactory.getLogger("UserController");

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AppConfig appConfig;


  @GetMapping("/user")
  public String admin(final Model model) {
    model.addAttribute("elements", userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    model.addAttribute("title", "Users");
    return "user";
  }

  private UserDTO toDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.id = user.getId();
    userDTO.active = user.isActive();
    userDTO.name=user.getName();
    userDTO.edited = Util.formatDateTimeMedium(user.getEdited(), appConfig);
    userDTO.email = user.getEmail();
    return userDTO;
  }

  @GetMapping("/user/update")
  public String update(final Model model)  {
    return admin(model);
  }

  @PostMapping("/user/update/{id}")
  public ModelAndView update(
          @PathVariable(name = "id") Long id,
          @RequestParam(name = "name") String name,
          @RequestParam(name = "email") String email)
  {
    userRepository.findById(id).ifPresent(user -> {
      user.setName(name);
      user.setEmail(email);
      userRepository.save(user);
    });


    logger.info("updated user with id: "+id);
    return new ModelAndView("redirect:/user");
  }

}
