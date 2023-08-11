package com.mailpug.homework.post;

import com.mailpug.homework.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @NonNull
    @Column(name = "create_by")
    private String author;

    @Builder
    public Post(String category, String title, String content, String author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void updatePost(PostDto postDto) {
        this.category = postDto.getCategory();
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
    }
}
