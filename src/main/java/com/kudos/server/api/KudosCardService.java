package com.kudos.server.api;

import com.kudos.server.model.KudosCard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface KudosCardService {
  /**
   * @param weeksAgo
   * @return a list of {@link KudosCard} sorted by date.
   */
  List<KudosCard> getKudosCards(int weeksAgo);

  Set<String> getWriters(int weeksAgo);
}
