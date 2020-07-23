package com.kudos.server.api;

import com.kudos.server.model.KudosCard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface KudosCardService {
  List<KudosCard> getKudosCards(int weeksAgo);

  Set<String> getWriters(int weeksAgo);
}
