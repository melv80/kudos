package com.kudos.server.repositories;

import com.kudos.server.model.jpa.Image;
import com.sun.istack.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {


    @Query("select u from Image u where u.pathOnDisk = ?1")
    @Nullable Image findByPath(String path);
}
