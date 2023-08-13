package com.mailpug.homework.post;

import com.mailpug.homework.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String title;

    private String content;

    @NonNull
    @Column(name = "create_by")
    private String author;

    @Builder
    public Post(String name, String title, String content, String author) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void updatePost(PostDto postDto) {
        this.name = postDto.getName();
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
    }
}
