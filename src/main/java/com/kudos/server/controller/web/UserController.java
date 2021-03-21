package com.kudos.server.controller.web;


import com.kudos.server.components.UserService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.controller.Util;
import com.kudos.server.model.dto.ui.Identifiable;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AppConfig appConfig;


  @GetMapping("/user")
  public String admin(final Model model) {
    model.addAttribute("elements", userRepository.findAll().stream().map(this::toIdentifiable).collect(Collectors.toList()));
    model.addAttribute("title", "Users");
    return "user";
  }

  private Identifiable toIdentifiable(User user) {
    Identifiable identifiable = new Identifiable();
    identifiable.id = user.getId();
    identifiable.name=user.getName();
    identifiable.edited = Util.formatDateTimeMedium(user.getEdited(), appConfig);
    return identifiable;
  }

  @PostMapping("/user/delete")
  public ModelAndView delete(@RequestParam(name = "id") Long id) {
    userRepository.deleteById(id);
    return new ModelAndView("redirect:/user");
  }

  @GetMapping("/user/create")
  public ModelAndView importCards() {
    return new ModelAndView("redirect:/user/create");
  }
}
