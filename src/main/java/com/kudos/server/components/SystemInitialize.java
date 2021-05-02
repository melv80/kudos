package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.model.jpa.*;
import com.kudos.server.repositories.PictureChannelRepository;
import com.kudos.server.repositories.CommentRepository;
import com.kudos.server.repositories.KudosCardRepository;
import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class SystemInitialize {

  private static String demoPassword = "{MD5}f561aaf6ef0bf14d4208bb46a4ccb3ad";


  private Logger logger = LoggerFactory.getLogger(SystemInitialize.class);


  private final AppConfig appConfig;
  private final ImageServiceImpl imageServiceImpl;
  private final KudosCardRepository kudosCardRepository;
  private final UserRepository userRepository;
  private final CommentRepository comments;
  private final PictureChannelRepository channels;


  public SystemInitialize(@Autowired AppConfig appConfig,
                          @Autowired ImageServiceImpl imageServiceImpl,
                          @Autowired KudosCardRepository kudosCardRepository,
                          @Autowired UserRepository userRepository,
                          @Autowired CommentRepository comments,
                          @Autowired PictureChannelRepository channels


  ) {
    this.appConfig = appConfig;
    this.imageServiceImpl = imageServiceImpl;
    this.kudosCardRepository = kudosCardRepository;
    this.userRepository = userRepository;
    this.comments = comments;
    this.channels = channels;
  }

  @PostConstruct
  void populateList() {

    if (isSystemInitializing()) {
      User admin = new User("admin", "admin", demoPassword);
      admin.setRoles("admin");
      userRepository.save(admin);


      imageServiceImpl.scanForNewImages();
      PictureChannel channel = new PictureChannel();
      channel.setActive(true);
      channel.setName("Image Channel");
      channels.save(channel);
    }


    if (appConfig.isGenerateDemoData()) {
      PictureChannel channel = channels.findAll().get(0);
      int cards = 15;
      List<KudosCard> demoList = new ArrayList<>();
      for (int i = 0; i < cards; i++) {
        demoList.add(demoCard(i, channel));
      }
      kudosCardRepository.saveAll(demoList);

      logger.info("demo data added: " + cards);
    }
  }

  private boolean isSystemInitializing() {
    return userRepository.count() == 0;
  }

  private KudosCard demoCard(int index, PictureChannel channel) {

    KudosCard card = new KudosCard();
    card.setType(KudosType.values()[new Random().nextInt(KudosType.values().length)]);
    card.setBackgroundImage(imageServiceImpl.pickRandomImage(card.getType()));
    card.setCreated(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.setEdited(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.setPictureChannel(channel);

    User user = new User("Mama ("+index+")", "melv"+index+"@gmail.com", demoPassword);
    userRepository.save(user);

    User user2 = new User("Krissi ("+index+")", "krissi"+index+"@gmail.com", demoPassword);
    userRepository.save(user2);

    Comment comment = new Comment("first comment", user);
    comments.save(comment);

    Comment comment2 = new Comment("second comment", user2);
    comments.save(comment2);

    card.setWriter(user);
    card.addComment(comment);
    card.addComment(comment2);


    return card;
  }

}
