package com.kudos.server.components;

import com.kudos.server.api.KudosCardService;
import com.kudos.server.config.AppConfig;
import com.kudos.server.model.KudosCard;
import com.kudos.server.model.dto.DisplayCard;
import com.kudos.server.model.dto.KudosCardDisplayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class DisplayService {

  private final AppConfig appConfig;
  private final KudosCardService kudosCardService;

  public DisplayService(@Autowired KudosCardService kudosCardService,
                        @Autowired AppConfig appConfig) {
    this.kudosCardService = kudosCardService;
    this.appConfig = appConfig;
  }

  public DisplayCard toDisplayCard(KudosCard card) {
    DisplayCard result = new DisplayCard();
    result.edited = localDateTime(card.getEdited());
    result.formattedDate = formatDate(card.getEdited());
    result.imageId = card.backgroundImage.getId();
    result.title = card.getType().getFormattedText();
    result.writer = card.getWriter();
    result.message = card.getMessage();
    return result;
  }

  public KudosCardDisplayList getDisplayCards(int weeksAgo) {
    final List<KudosCard> kudosCards = kudosCardService.getKudosCards(weeksAgo);
    kudosCards.sort(Comparator.comparing(KudosCard::getEdited));
    return new KudosCardDisplayList(kudosCards.stream().map(this::toDisplayCard).collect(Collectors.toList()), appConfig.getLocale());
  }

  public ZonedDateTime localDateTime(Instant time) {
    return time.atZone(ZoneId.systemDefault());
  }


  public String formatDate(Instant edited) {
    return localDateTime(edited).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(appConfig.getLocale()));
  }

}
