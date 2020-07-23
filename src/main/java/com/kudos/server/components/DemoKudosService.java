package com.kudos.server.components;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosCard;
import com.kudos.server.model.KudosType;
import com.kudos.server.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
    card.created = Instant.now();
    card.message = "Hello, this is a test message: "+index;
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
