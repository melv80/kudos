package com.kudos.server.controller.web;

import com.kudos.server.components.KudosCardService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.model.dto.ui.CreateCard;
import com.kudos.server.model.jpa.KudosType;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UploadController {

    @Autowired
    KudosCardService kudosCardService;

    @Autowired
    UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private AppConfig config;


    @GetMapping("/create")
    public String createCard(final Model model) {
        model.addAttribute("types", KudosType.values());
        model.addAttribute("newCard", new CreateCard());
        return "create";
    }

    @PostMapping("/create")
    public String createCard(@Valid @ModelAttribute CreateCard createCard) {
        createCard.setKudostype(KudosType.UPLOAD);
        User user = userRepository.findAll().get(0);
        createCard.setWriter(user.getName());
        createCard.setWriterID(user.getId());
        kudosCardService.createCardRandomImage(createCard);
        return "redirect:/";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("comment") String comment,
                             RedirectAttributes attributes) {

        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        try {
            Path relativePath = Paths.get(KudosType.UPLOAD.getFolder(), fileName);
            Path path =config.getBaseDir().resolve(relativePath).toAbsolutePath().normalize();
            Files.createDirectories(path.getParent());
            logger.error("uploading to "+ path);
            // TODO: 28.02.2021 handle file names exists
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            kudosCardService.createCardWithUploadImage(comment, path);
        } catch (IOException e) {
            logger.error("could not upload file", e);
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        return "redirect:/";
    }

}