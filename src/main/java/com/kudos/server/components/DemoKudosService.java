package com.kudos.server.components;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosCard;
import com.kudos.server.model.KudosItem;
import com.kudos.server.model.KudosType;
import com.kudos.server.model.dto.CreateCard;
import com.kudos.server.model.dto.DisplayCard;
import com.kudos.server.repositories.ImageRepository;
import com.kudos.server.repositories.KudosCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DemoKudosService implements KudosCardService {

  private Logger logger = LoggerFactory.getLogger(DemoKudosService.class);


  private ImageRepository repository;
  private ImageScan imageScan;

  private List<KudosCard> demoList = new ArrayList<>();

  public DemoKudosService(@Autowired ImageRepository imageRepository,
                          @Autowired ImageScan imageScan
  ) {
    this.repository = imageRepository;
    this.imageScan = imageScan;
  }

  @PostConstruct
  void populateList() {
    imageScan.insertIntoDatabase();
    int cards = 15;
    for (int i = 0; i < cards; i++) {
      demoList.add(demoCard(i));
    }

    logger.info("demo data added: "+cards);
  }

  @Override
  public void createCard(CreateCard createCard) {

    KudosCard card = new KudosCard();
    final List<Image> all = repository.findAll();
    card.backgroundImage = all.get(new Random().nextInt(all.size()));
    card.message = createCard.getMessage();
    card.type = createCard.getKudostype();
    card.writer = createCard.getWriter();
    card.setId(demoList.size()+1);

    demoList.add(card);
    logger.info("card created");

  }

  @Override
  public void importCards() {
    logger.info("imported cards");
  }

  private KudosCard demoCard(int index) {
    KudosCard card = new KudosCard();
    final List<Image> all = repository.findAll();
    card.backgroundImage = all.get(new Random().nextInt(all.size()));
    card.setEdited(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."+index;
    card.message = card.message.substring(0, new Random().nextInt(card.message.length()-250));
    card.type = KudosType.THANK_YOU;
    card.writer = "KristianJ ("+index+")";
    card.setId(index);
    return card;
  }

  @Override
  public Set<String> getWriters(int weeksAgo) {
    return demoList.stream().map(KudosCard::getWriter).collect(Collectors.toSet());
  }

  @Override
  public List<KudosCard> getKudosCards(int weeksAgo) {
    return demoList;
  }
}
