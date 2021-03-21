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
public class DemoDataGenerator {


  private Logger logger = LoggerFactory.getLogger(DemoDataGenerator.class);


  private final AppConfig appConfig;
  private final ImageServiceImpl imageServiceImpl;
  private final KudosCardRepository kudosCardRepository;
  private final UserRepository userRepository;
  private final CommentRepository comments;
  private final PictureChannelRepository channels;


  public DemoDataGenerator(@Autowired AppConfig appConfig,
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
    if (!appConfig.isGenerateDemoData()) return;

    imageServiceImpl.scanForNewImages();
    User admin = new User("admin", "admin@gmail.com", "xxx");
    userRepository.save(admin);


    int cards = 15;
    List<KudosCard> demoList = new ArrayList<>();
    for (int i = 0; i < cards; i++) {
      demoList.add(demoCard(i));
    }
    kudosCardRepository.saveAll(demoList);

    logger.info("demo data added: "+cards);
  }

  private KudosCard demoCard(int index) {

    PictureChannel channel = new PictureChannel();
    channel.setName("Clara Fida Laila");

    channels.save(channel);

    KudosCard card = new KudosCard();
    card.setType(KudosType.values()[new Random().nextInt(KudosType.values().length)]);
    card.setBackgroundImage(imageServiceImpl.pickRandomImage(card.getType()));
    card.setCreated(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.setEdited(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.setPictureChannel(channel);

    User user = new User("Mama ("+index+")", "melv"+index+"@gmail.com", "xxx");
    userRepository.save(user);

    User user2 = new User("Krissi ("+index+")", "melv"+index+"@gmail.com", "xxx");
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
