package test.study.study.studyParticipants.domain;

import lombok.Getter;
import lombok.Setter;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;

import javax.persistence.*;

@Entity
@Getter @Setter
public class StudyParticipants {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participants_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantPosition position; // 그룹 내 직급(leader/member)

    public StudyParticipants(){}
    public StudyParticipants(StudyGroup group, Member member) {
        this.group = group;
        this.member = member;
    }
}
