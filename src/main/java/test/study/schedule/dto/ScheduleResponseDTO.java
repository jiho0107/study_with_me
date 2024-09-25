package test.study.schedule.dto;

import lombok.Getter;
import lombok.Setter;
import test.study.schedule.domain.Schedule;
import test.study.schedule.domain.ScheduleType;

import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleResponseDTO {
    private String title;
    private LocalDateTime date;
    private Long memberId;
    private ScheduleType type;
    private Long groupId;

    public ScheduleResponseDTO(Schedule schedule) {
        this.title = schedule.getTitle();
        this.date = schedule.getDate();
        this.memberId = schedule.getMember().getId();
        this.type = schedule.getType();
        //this.groupId = schedule.getStudyGroup().getId();
        if(schedule.getStudyGroup() != null){
            this.groupId = schedule.getStudyGroup().getId();
        }
        else{
            this.groupId = null;
        }
    }
}
