package test.study.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import test.study.member.domain.Member;
import test.study.post.domain.Post;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PostRequestDTO {
    @NotBlank(message = "제목은 필수 값입니다.")
    @Length(min = 1, max = 30,message = "제목은 1~30자 여야 합니다.")
    private String title; //게시글 제목

    @NotBlank(message = "내용은 필수 값입니다.")
    @Length(min = 1, message = "내용은 1자 이상이어야 합니다.")
    private String content; //게시글 내용

    public Post toEntity(Member member){
        return new Post(title,content,member);
    }
}
