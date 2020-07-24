package com.kudos.server.model;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * An ordered list of {@link KudosCard} ready for display.
 */
public class KudosCardList implements Iterable<KudosCard> {
  private List<KudosCard> cards;

  public KudosCardList(List<KudosCard> cards) {
    this.cards = cards;
    cards.sort(Comparator.comparing(KudosItem::getEdited));
  }

  public int size() {
    return cards.size();
  }

  public boolean isFirstCardOfDay(int index) {
    if (index == 0) return true;
    KudosCard previousCard = cards.get(index-1);
    KudosCard currentCard = cards.get(index);
    return previousCard.localEdited().getDayOfWeek() != currentCard.localEdited().getDayOfWeek();
  }

  public String getDayOfCard(int index) {
    KudosCard currentCard = cards.get(index);
    return currentCard.getEdited()
        .atZone(ZoneId.systemDefault())
        .getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
  }

  @Override
  public Iterator<KudosCard> iterator() {
    return cards.iterator();
  }
}
