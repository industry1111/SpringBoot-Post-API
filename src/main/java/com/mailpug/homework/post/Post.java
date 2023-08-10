package com.mailpug.homework.post;

import com.mailpug.homework.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String category;

    private String title;

    private String content;

    private String createBy;

    @Builder
    public Post(String category, String title, String content, String createBy) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.createBy = createBy;
    }

    public void changeContent(String updateContent) {
        this.content = updateContent;
    }
}
