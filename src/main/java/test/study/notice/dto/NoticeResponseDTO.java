package test.study.notice.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.notice.domain.Notice;

@Getter @Setter
public class NoticeResponseDTO {
    private Long id;
    private String title;
    private String content;

    public NoticeResponseDTO(Notice notice){
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }
}
