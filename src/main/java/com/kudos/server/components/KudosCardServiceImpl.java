package com.kudos.server.components;

import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.dto.ui.CreateCard;
import com.kudos.server.repositories.KudosCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class KudosCardServiceImpl implements KudosCardService {
  private Logger logger = LoggerFactory.getLogger(KudosCardServiceImpl.class);

  @Autowired
  private ImageService imageService;

  @Autowired
  private KudosCardRepository kudosCardRepository;


  @Override
  public List<KudosCard> getKudosCards(int weeksAgo) {
    // TODO: 25.07.2020 filter on db
    Instant now = Instant.now();
    Instant weekAgo = now.minus(7 * weeksAgo, ChronoUnit.DAYS);
    return kudosCardRepository.findAll().stream().filter(card -> card.getCreated().compareTo(weekAgo) > 0).collect(Collectors.toList());
  }

  @Override
  public Set<String> getWriters(int weeksAgo) {
    return getKudosCards(weeksAgo).stream().map(KudosCard::getWriter).collect(Collectors.toSet());
  }

  @Override
  public void createCard(CreateCard createCard) {
    KudosCard card = new KudosCard();
    card.setMessage(createCard.getMessage());
    card.setType(createCard.getKudostype());
    card.setWriter(createCard.getWriter());
    card.setBackgroundImage(imageService.pickRandomImage(card.getType()));

    kudosCardRepository.save(card);
    logger.info("card created");
  }


  @Override
  public void deleteCard(Long id) {
    kudosCardRepository.deleteById(id);
    logger.info("card deleted: " + id);
  }

  public void importCards() {
    try {
      List<KudosCard> cards = new ConfluenceImporter().importCards(new File("./demoimport/DemoImport.json").toURI().toURL());

      AtomicInteger updated = new AtomicInteger();
      cards.stream()
          .filter(this::shouldInsertToDatabase)
          .peek(kudosCard -> kudosCard.setBackgroundImage(imageService.pickRandomImage(kudosCard.getType())))
          .forEach(kudosCard -> {
              kudosCardRepository.save(kudosCard);
              updated.incrementAndGet();
          });

      logger.info("imported cards: " + cards.size() + " new: "+updated);
    } catch (MalformedURLException e) {
      throw new RuntimeException("could not import demo data", e);
    }
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
