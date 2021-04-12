package com.kudos.server.controller.web;

import com.kudos.server.components.DisplayService;
import com.kudos.server.components.KudosCardService;
import com.kudos.server.components.SessionContext;
import com.kudos.server.config.AppConfig;
import com.kudos.server.model.jpa.Comment;
import com.kudos.server.model.jpa.Image;
import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.CommentRepository;
import com.kudos.server.repositories.ImageRepository;
import com.kudos.server.repositories.KudosCardRepository;
import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Controller
public class WebController {

  private Logger logger = LoggerFactory.getLogger(WebController.class);

  @Autowired
  AppConfig config;

  @Autowired
  KudosCardService kudosCardService;

  @Autowired
  KudosCardRepository kudosCardRepository;

  @Autowired
  DisplayService displayService;

  @Autowired
  UserRepository userRepository;


  @Autowired
  ImageRepository imageRepository;

  @Autowired
  CommentRepository commentRepository;

  @Autowired
  SessionContext sessionContext;

  @GetMapping("/card/{cardid}")
  public String card(Device device, final Model model, @PathVariable("cardid") String cardid) {
    model.addAttribute("kudoscards", displayService.getDisplayCards(1));
    model.addAttribute("contributors", kudosCardService.getWriters(1));
    model.addAttribute("title", config.getCornerTitle());
    sessionContext.channel = kudosCardService.getPictureChannel();
    model.addAttribute("channel", kudosCardService.getPictureChannel().getName());
    model.addAttribute("greeting", config.getGreeting());
    model.addAttribute("outro", config.getOutro());
    model.addAttribute("card", displayService.getDisplayCards(1).iterator().next());
    return "card"+ (device.isMobile() ? "-mobile" : "");
  }

  @GetMapping("/")
  public String index(Device device, final Model model) {
    model.addAttribute("kudoscards", displayService.getDisplayCards(1));
    model.addAttribute("contributors", kudosCardService.getWriters(1));
    model.addAttribute("title", config.getCornerTitle());
    sessionContext.channel = kudosCardService.getPictureChannel();
    model.addAttribute("channel", kudosCardService.getPictureChannel().getName());
    model.addAttribute("greeting", config.getGreeting());
    model.addAttribute("outro", config.getOutro());
    return "index"+ (device.isMobile() ? "-mobile" : "");
  }


  @RequestMapping(path = "/images/{id}", method = RequestMethod.GET)
  public ResponseEntity<ByteArrayResource> download(@PathVariable("id") Long id) throws IOException {

    final Optional<Image> byId = imageRepository.findById(id);
    if (!byId.isPresent()) throw new IllegalStateException("image not found with id: "+id);
    Path path = config.getBaseDir().resolve(byId.get().pathOnDisk).normalize();
    if (!Files.isReadable(path)) {
      logger.error(String.format("image[ID=%s] missing: %s",id, path));
      return ResponseEntity.notFound().build();
    }

    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    return ResponseEntity.ok()
        .contentLength(Files.size(path))
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);

  }


  @PostMapping("/comment")
  public String uploadFile(@RequestParam("comment") String comment,
                           @RequestParam("cardID") String cardID) {

    Optional<KudosCard> cardOptional = kudosCardRepository.findById(Long.parseLong(cardID));
    if (!cardOptional.isPresent()) {
      throw new IllegalStateException("kudos cardOptional not found!");
    }

    KudosCard card = cardOptional.get();
    User writer=userRepository.findAll().get(0);

    Comment newComment = new Comment(comment, writer);
    commentRepository.save(newComment);
    card.addComment(newComment);
    kudosCardRepository.saveAndFlush(card);

    return "redirect:/";
  }

}
