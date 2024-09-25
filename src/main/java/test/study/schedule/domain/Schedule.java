package test.study.schedule.domain;

import lombok.Getter;
import lombok.Setter;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id; // 일정 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 일정 작성한 회원

    //@Temporal(TemporalType.TIMESTAMP)
    //@Column(name = "schdule_date")
    @Column(nullable = false)
    private LocalDateTime date; // 일정 날짜
    @Column(nullable = false,length = 30)
    private String title; // 일정 제목

    //@Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type; // 그룹/개인 일정 구분

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup; // 스터디 그룹(그룹 일정인 경우)(개인 일정인 경우는 null 이 됨)

    public Schedule(){}
    public Schedule(Member member, LocalDateTime date, String title, ScheduleType type, StudyGroup studyGroup) {
        this.member = member;
        this.date = date;
        this.title = title;
        this.type = type;
        this.studyGroup = studyGroup;
    }
    public Schedule(Member member, LocalDateTime date, String title, ScheduleType type){
        this.member = member;
        this.date = date;
        this.title = title;
        this.type = type;
        this.studyGroup = null; // 개인 일정인 경우
    }
}
