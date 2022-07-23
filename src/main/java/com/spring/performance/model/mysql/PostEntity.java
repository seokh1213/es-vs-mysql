package com.spring.performance.model.mysql;

import com.spring.performance.model.vo.PostVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String author;
    private String title;
    @Column(length = 1000)
    private String content;
    private LocalDateTime createdDt;

    public static PostEntity of(PostVO postVO) {
        return new PostEntity(null, postVO.getAuthor(), postVO.getTitle(), postVO.getContent(), postVO.getCreatedDt());
    }
}
