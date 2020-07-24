package com.kudos.server.components;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosCard;
import com.kudos.server.model.KudosItem;
import com.kudos.server.model.KudosType;
import com.kudos.server.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DemoKudosService implements KudosCardService {

  private ImageRepository repository;


  private List<KudosCard> demoList;

  public DemoKudosService(@Autowired ImageRepository imageRepository) {
    this.repository = imageRepository;
  }

  private List<KudosCard> populateList() {
    List<KudosCard> result = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
      result.add(demoCard(i));
    }

    return result;
  }

  private KudosCard demoCard(int index) {
    KudosCard card = new KudosCard();
    final List<Image> all = repository.findAll();
    card.backgroundImage = all.get(new Random().nextInt(all.size()));
    card.setEdited(Instant.now().minus(index % 7, ChronoUnit.DAYS));
    card.message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."+index;
    card.message = card.message.substring(30, new Random().nextInt(card.message.length()-50));
    card.type = KudosType.THANK_YOU;
    card.writer = "KristianJ ("+index+")";
    return card;
  }

  @Override
  public Set<String> getWriters(int weeksAgo) {
    return demoList.stream().map(KudosCard::getWriter).collect(Collectors.toSet());
  }

  @Override
  public List<KudosCard> getKudosCards(int weeksAgo) {
    if (demoList == null)
      demoList= populateList();
    return demoList;
  }
}
