package test.study.schedule.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import test.study.schedule.domain.ScheduleType;
import test.study.study.studyGroup.domain.StudyGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleRequestDTO {
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Length(min = 1, max = 30,message = "제목은 1~30자 여야 합니다.")
    private String title; // 일정 제목

    @NotNull(message = "날짜는 필수 입력 값입니다.")
    private LocalDateTime date; // 일정 날짜

    @NotNull(message = "그룹/개인 선택은 필수 입력 값입니다.")
    private ScheduleType type; // 그룹/개인 일정 구분

    private Long studyGroup; // 스터디 그룹 id
}
