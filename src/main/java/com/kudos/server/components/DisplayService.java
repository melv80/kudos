package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.controller.Util;
import com.kudos.server.model.dto.ui.DisplayComment;
import com.kudos.server.model.jpa.Image;
import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.dto.ui.DisplayCard;
import com.kudos.server.model.dto.ui.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
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
    result.created = Util.localDateTime(card.getCreated());
    result.formattedDate = Util.formatDate(card.getCreated(), appConfig);
    Image image = card.getBackgroundImage();
    result.imageId = -1;
    if (image != null && image.thumbnail != null)
      result.imageId = image.thumbnail.getId();

    result.title = card.getType().getFormattedText();
    result.writer = card.getWriter().getName();
    result.cardID = card.getId();
    result.comments = card.getComments().stream().map(comment -> new DisplayComment(
        comment.getWriter().getName(),
        comment.getMessage(),
        Util.formatDate(comment.getCreated(), appConfig))).collect(Collectors.toList());

    return result;
  }

  public CardList getDisplayCards(int weeksAgo) {
    final List<KudosCard> kudosCards = kudosCardService.getKudosCards(weeksAgo);
    kudosCards.sort(Comparator.comparing(KudosCard::getCreated).reversed());
    return new CardList(kudosCards.stream().map(this::toDisplayCard).collect(Collectors.toList()), appConfig.getLocale());
  }


}
