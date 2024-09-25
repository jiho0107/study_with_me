package test.study.notice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id; // 공지사항 id
    @Column(nullable = false,length = 30)
    private String title; // 공지사항 제목

    @Lob //@Column(name = "contents")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 공지사항 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 공지사항 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group; // 공지사항이 어느 그룹에 소속되어있는지

    public Notice(){}
    public Notice(String title, String content, Member member, StudyGroup group) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.group = group;
    }
}
