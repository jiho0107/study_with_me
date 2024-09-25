package test.study.study.studyParticipants.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.study.studyParticipants.domain.ParticipantPosition;
import test.study.study.studyParticipants.domain.StudyParticipants;

@Getter @Setter
public class StudyParticipantsDTO {
    private Long groupId;
    private Long memberId;
    private ParticipantPosition position;

    public StudyParticipantsDTO(StudyParticipants participants){
        this.groupId = participants.getGroup().getId();
        this.memberId = participants.getMember().getId();
        this.position = participants.getPosition();
    }
}
