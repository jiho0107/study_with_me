package test.study.study.studyRegister.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.study.studyRegister.domain.StudyRegister;

@Getter @Setter
public class StudyRegisterDTO {
    private Long memberId;
    private Long postId;
    private Boolean accept;

    public StudyRegisterDTO(StudyRegister register){
        this.memberId = register.getMember().getId();
        this.postId = register.getPost().getId();
        this.accept = register.getAccept();
    }
}
