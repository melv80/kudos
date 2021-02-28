package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.model.dto.ui.CreateCard;
import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.jpa.PictureChannel;
import com.kudos.server.model.jpa.User;
import com.kudos.server.repositories.KudosCardRepository;
import com.kudos.server.repositories.PictureChannelRepository;
import com.kudos.server.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
  private UserRepository userRepository;

  @Autowired
  private PictureChannelRepository pictureChannelRepository;


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
  public void createCard(CreateCard createCard) {
    KudosCard card = new KudosCard();
    card.setMessage(createCard.getMessage());
    card.setType(createCard.getKudostype());
    // TODO: 20.02.2021 find active user
    card.setWriter(userRepository.findById(createCard.getWriterID()).orElseThrow(() -> new IllegalStateException("unknown user")));
    card.setBackgroundImage(imageService.pickRandomImage(card.getType()));

    kudosCardRepository.saveAndFlush(card);
    logger.info("card created");
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

  @Override
  public void importCards() {
    importCardsLocally();
  }

  @Scheduled(fixedRate = 5*60*1000)
  public void importCardsLocally() {
    List<KudosCard> cards = new ArrayList<>();

    AtomicInteger updated = new AtomicInteger();
    cards.stream()
         .filter(this::shouldInsertToDatabase)
         .peek(kudosCard -> kudosCard.setBackgroundImage(imageService.pickRandomImage(kudosCard.getType())))
         .forEach(kudosCard -> {
           kudosCardRepository.saveAndFlush(kudosCard);
           updated.incrementAndGet();
         });

    logger.info("imported cards from: " + appConfig.getImportDir() + " cards found: " + cards.size() + " imported: " + updated);
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
