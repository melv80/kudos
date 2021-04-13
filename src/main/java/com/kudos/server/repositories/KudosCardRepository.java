package com.kudos.server.repositories;

import com.kudos.server.model.jpa.KudosCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KudosCardRepository extends JpaRepository<KudosCard, Long> {
    @Query("select count(card) from KudosCard card where card.pictureChannel.id = ?1")
    int cardCount(long id);

    @Query("select u from KudosCard u where u.pictureChannel.id = ?1")
    List<KudosCard> cardsByChannel(long channelID);
}
