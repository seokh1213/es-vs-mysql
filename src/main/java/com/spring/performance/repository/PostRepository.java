package com.spring.performance.repository;

import com.spring.performance.model.mysql.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    int countAllByTitleContainingOrContentContaining(String title, String content);

    @Query(value = "SELECT count(*) FROM post_entity post\n" +
            "WHERE MATCH(post.title, post.content) AGAINST(:keyword IN BOOLEAN MODE);"
            , nativeQuery = true)
    int countAllByTitleIndexAndContentIndex(String keyword);
}
