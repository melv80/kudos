package com.kudos.server.controller;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.repositories.KudosCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(name = "/api/v1/kudoscards")
public class ApiController {

  private static final Logger logger = LoggerFactory.getLogger("ApiController");

  @Autowired
  private KudosCardRepository repositories;

  @Autowired
  private KudosCardService kudosCardService;

  @RequestMapping(
      value = "delete",
      method = RequestMethod.POST
  )
  public ModelAndView delete(@RequestParam(name = "id") Long id) {
    repositories.deleteById(id);
    logger.info("kudos deleted: "+id);
    return new ModelAndView("redirect:/");
  }

  @RequestMapping(
      value = "import",
      method = RequestMethod.POST
  )
  public ModelAndView importCards() {
    kudosCardService.importCards();
    return new ModelAndView("redirect:/admin");
  }


}
