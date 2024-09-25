package test.study.post.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.post.domain.Post;

@Getter @Setter
public class PostResponseDTO {
    Long id;
    String title;
    String content;
    Long memberId;

    public PostResponseDTO(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberId = post.getMember().getId();
    }
}
