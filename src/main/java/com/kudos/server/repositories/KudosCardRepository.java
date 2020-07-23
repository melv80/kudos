package com.kudos.server.repositories;

import com.kudos.server.model.KudosCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KudosCardRepository extends JpaRepository<KudosCard, Long> {
}
