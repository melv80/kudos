package com.kudos.server.controller;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosCard;
import com.kudos.server.model.KudosCardList;
import com.kudos.server.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    return "index";
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
