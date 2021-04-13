package com.kudos.server.controller.web;


import com.kudos.server.components.SessionContext;
import com.kudos.server.config.AppConfig;
import com.kudos.server.controller.Util;
import com.kudos.server.model.dto.ui.ChannelDTO;
import com.kudos.server.model.jpa.PictureChannel;
import com.kudos.server.repositories.KudosCardRepository;
import com.kudos.server.repositories.PictureChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class ChannelsController {



  private static final Logger logger = LoggerFactory.getLogger("ChannelsController");

  @Autowired
  private PictureChannelRepository repo;

  @Autowired
  private KudosCardRepository cards;

  @Autowired
  private AppConfig appConfig;

  @Autowired
  private SessionContext sessionContext;


  @GetMapping("/channel/select")
  public String selectchannel(final Model model) {
    model.addAttribute("elements", repo.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    model.addAttribute("activeChannelID", sessionContext.getChannel().getId());
    model.addAttribute("title", "Channels");
    return "selectchannel";
  }

  @GetMapping("/channel")
  public String admin(final Model model) {
    model.addAttribute("elements", repo.findAll().stream().map(this::toDTO).collect(Collectors.toList()));
    model.addAttribute("title", "Channels");
    return "channel";
  }

  private ChannelDTO toDTO(PictureChannel channel) {
    ChannelDTO channelDTO = new ChannelDTO();
    channelDTO.id = channel.getId();
    channelDTO.active = channel.isActive();
    channelDTO.name=channel.getName();
    channelDTO.edited = Util.formatDateTimeMedium(channel.getEdited(), appConfig);
    channelDTO.created = Util.formatDateTimeMedium(channel.getCreated(), appConfig);

    channelDTO.cardCount = cards.cardCount(channel.getId());
    return channelDTO;
  }

  @GetMapping("/channel/update")
  public String update(final Model model)  {
    return admin(model);
  }

  @PostMapping("/channel/newchannel")
  public ModelAndView newUser() {
    PictureChannel newUser = new PictureChannel();
    newUser.setActive(true);
    repo.save(newUser);
    logger.info("created new channel");
    return new ModelAndView("redirect:/channel");

  }

  @PostMapping("/channel/update/{id}")
  public ModelAndView update(
          @PathVariable(name = "id") Long id,
          @RequestParam(name = "name") String name,
          @RequestParam(name = "active") String isActive
          )
  {
    repo.findById(id).ifPresent(channel -> {
      channel.setName(name);
      channel.setActive(Boolean.valueOf(isActive));
      repo.save(channel);
    });


    logger.info("updated channel with id: "+id);
    return new ModelAndView("redirect:/channel");
  }

}
