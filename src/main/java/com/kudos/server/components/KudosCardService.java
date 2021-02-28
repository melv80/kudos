package com.kudos.server.components;

import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.dto.ui.CreateCard;
import com.kudos.server.model.jpa.PictureChannel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
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

  void setPictureChannel(PictureChannel channel);
  PictureChannel getPictureChannel();

  void createCardRandomImage(CreateCard createCard);

  /**
   * creates a card with the currently logged in user
   * @param comment of the card
   * @param image relativePath to base directory
   */
  void createCardWithUploadImage(String comment, Path image) throws IOException;

  void importCards();

  void deleteCard(Long id);
}
