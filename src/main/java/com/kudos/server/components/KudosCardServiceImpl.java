package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.model.dto.ui.CreateCard;
import com.kudos.server.model.jpa.*;
import com.kudos.server.repositories.CommentRepository;
import com.kudos.server.repositories.KudosCardRepository;
import com.kudos.server.repositories.PictureChannelRepository;
import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KudosCardServiceImpl implements KudosCardService {
  private Logger logger = LoggerFactory.getLogger(KudosCardServiceImpl.class);

  @Autowired
  private AppConfig appConfig;

  @Autowired
  private ImageService imageService;

  @Autowired
  private KudosCardRepository kudosCardRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PictureChannelRepository pictureChannelRepository;

  @Autowired
  private SessionContext sessionContext;


  @Override
  public KudosCard getKudosCard(long id) {
    final Optional<KudosCard> byId = kudosCardRepository.findById(id);
    if (byId.isPresent())
      return byId.get();
    throw new IllegalStateException("unknown card with id: "+id);
  }

  @Override
  public List<KudosCard> getKudosCards(int weeksAgo, long channelID) {
    // TODO: 25.07.2020 filter on db
    Instant now     = Instant.now();
    Instant weekAgo = now.minus(7 * weeksAgo, ChronoUnit.DAYS);
    return kudosCardRepository.cardsByChannel(channelID).stream().filter(card -> card.getCreated().compareTo(weekAgo) > 0).collect(Collectors.toList());
  }

  @Override
  public List<KudosCard> getKudosCards(int weeksAgo) {
    // TODO: 25.07.2020 filter on db
    Instant now     = Instant.now();
    Instant weekAgo = now.minus(7 * weeksAgo, ChronoUnit.DAYS);
    return kudosCardRepository.findAll().stream().filter(card -> card.getCreated().compareTo(weekAgo) > 0).collect(Collectors.toList());
  }

  @Override
  public Set<String> getWriters(int weeksAgo) {
    return getKudosCards(weeksAgo).stream().map(KudosCard::getWriter).map(User::getName).collect(Collectors.toSet());
  }

  @Override
  public Set<String> getWriters(int weeksAgo, long channelID) {
    return getKudosCards(weeksAgo, channelID).stream().map(KudosCard::getWriter).map(User::getName).collect(Collectors.toSet());
  }

  @Override
  public void createCardRandomImage(CreateCard createCard) {
    KudosCard card = new KudosCard();
    card.setType(createCard.getKudostype());
    card.setWriter(userRepository.findById(createCard.getWriterID()).orElseThrow(() -> new IllegalStateException("unknown user")));
    card.setBackgroundImage(imageService.pickRandomImage(card.getType()));
    kudosCardRepository.saveAndFlush(card);
    logger.info("card created");
  }

  @Override
  public void createCardWithUploadImage(String comment, Path imagePath) throws IOException {
    KudosCard card = new KudosCard();
    card.setType(KudosType.UPLOAD);
    final User writer = userRepository.findAll().get(0);
    card.setWriter(writer);
    card.setBackgroundImage(imageService.importImage(imagePath).image);
    Comment commentObject = new Comment(comment, writer);
    commentRepository.save(commentObject);
    card.setPictureChannel(sessionContext.getChannel());

    card.addComment(commentObject);
    kudosCardRepository.saveAndFlush(card);
    logger.info("card created for active user");
  }

  @Override
  public void setPictureChannel(PictureChannel channel) {
    
  }

  @Override
  public PictureChannel getPictureChannel() {
    // TODO: 27.02.2021  
    return pictureChannelRepository.findAll().get(0);
  }

  @Override
  public void deleteCard(Long id) {
    kudosCardRepository.deleteById(id);
    logger.info("card deleted: " + id);
  }


  private boolean shouldInsertToDatabase(KudosCard kudosCard) {
    return !(kudosCardRepository.findOne(Example.of(example(kudosCard))).isPresent());
  }

  public static KudosCard example(KudosCard other) {
    KudosCard res = new KudosCard();
    res.setEdited(null);
    res.setCreated(null);
    res.setImporterID(other.getImporterID());
    return res;
  }
}
