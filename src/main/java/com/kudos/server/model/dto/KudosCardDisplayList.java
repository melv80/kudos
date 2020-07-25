package com.kudos.server.model.dto;

import com.kudos.server.model.KudosCard;

import java.time.format.TextStyle;
import java.util.*;

/**
 * An ordered list of {@link KudosCard} ready for display.
 */
public class KudosCardDisplayList implements Iterable<DisplayCard> {
  private List<DisplayCard> cards;
  private Locale locale;

  public KudosCardDisplayList(List<DisplayCard> cards, Locale locale) {
    this.cards = cards;
    this.locale = locale;
  }

  public int size() {
    return cards.size();
  }

  public boolean isFirstCardOfDay(int index) {
    if (index == 0) return true;
    DisplayCard previousCard = cards.get(index-1);
    DisplayCard currentCard = cards.get(index);
    return previousCard.edited.getDayOfWeek() != currentCard.edited.getDayOfWeek();
  }

  // TODO: 25.07.2020 move to display service
  public String getDayOfCard(int index) {
    DisplayCard currentCard = cards.get(index);
    return currentCard.edited
        .getDayOfWeek().getDisplayName(TextStyle.FULL, locale);
  }

  @Override
  public Iterator<DisplayCard> iterator() {
    return cards.iterator();
  }
}
