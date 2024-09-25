package test.study.study.studyGroup.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.study.studyGroup.domain.StudyGroup;

@Getter @Setter
public class StudyGroupDTO {
    private Long id;
    private Long postId;
    private boolean active;

    public StudyGroupDTO(StudyGroup group){
        this.id = group.getId();
        this.postId = group.getPost().getId();
        this.active = group.isActive();
    }
}
