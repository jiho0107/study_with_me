package test.study.notice.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class NoticeRequestDTO {
    @NotBlank(message = "제목은 필수 값입니다.")
    @Length(min = 1, max = 30, message = "제목은 1~30자 여야 합니다.")
    private String title; // 공지사항 제목
    @NotBlank(message = "내용은 필수 값입니다.")
    @Length(min = 1, message = "내용은 최소 1자리 이상이여야 합니다.")
    private String content; // 공지사항 내용
}
