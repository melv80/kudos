package com.kudos.server.repositories;

import com.kudos.server.model.jpa.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
