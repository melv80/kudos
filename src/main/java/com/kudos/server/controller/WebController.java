package com.kudos.server.controller;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosCardList;
import com.kudos.server.model.KudosType;
import com.kudos.server.model.dto.CreateCard;
import com.kudos.server.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Controller
public class WebController {

  @Autowired
  AppConfig config;

  @Autowired
  KudosCardService kudosCardService;

  @Autowired
  ImageRepository imageRepository;

  @GetMapping("/")
  public String index(final Model model) {
    model.addAttribute("kudoscards", new KudosCardList(kudosCardService.getKudosCards(1)));
    model.addAttribute("contributors", kudosCardService.getWriters(1));
    model.addAttribute("title", config.getCornerTitle());
    model.addAttribute("greeting", config.getGreeting());
    return "index";
  }

  @GetMapping("/create")
  public String createCard(final Model model) {
    model.addAttribute("types", KudosType.values());
    model.addAttribute("newCard", new CreateCard());
    return "create";
  }

  @PostMapping("/create")
  public String createCard(@Valid @ModelAttribute CreateCard createCard) {
    kudosCardService.createCard(createCard);
    return "redirect:/";
  }

  @GetMapping("/admin")
  public String admin(final Model model) {
    model.addAttribute("kudoscards", new KudosCardList(kudosCardService.getKudosCards(1)));
    model.addAttribute("contributors", kudosCardService.getWriters(1));
    model.addAttribute("title", config.getCornerTitle());
    model.addAttribute("greeting", config.getGreeting());
    return "admin";
  }


  @RequestMapping(path = "/images/{id}", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download(@PathVariable("id") Long id) throws IOException {

    final Optional<Image> byId = imageRepository.findById(id);
    if (!byId.isPresent()) throw new IllegalStateException("image not found");
    Path path = config.getBasedir().resolve(byId.get().pathOnDisk);

    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    return ResponseEntity.ok()
        .contentLength(Files.size(path))
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);

  }
}
