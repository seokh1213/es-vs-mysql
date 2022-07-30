package com.spring.performance.repository;

import com.spring.performance.model.mysql.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByTitleContainingOrContentContaining(String title, String content);

    @Query(value = "SELECT * FROM post_entity post\n" +
            "WHERE MATCH(post.title, post.content) AGAINST(:keyword IN BOOLEAN MODE);"
            , nativeQuery = true)
    List<PostEntity> findAllByTitleIndexAndContentIndex(String keyword);
}
