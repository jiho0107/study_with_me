package test.study.study.studyRegister.domain;

import lombok.Getter;
import lombok.Setter;
import test.study.member.domain.Member;
import test.study.post.domain.Post;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter @Setter
public class StudyRegister {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Boolean accept; // 수락 여부(수락/거절 을 안 하면 null이 됨)

    public StudyRegister(){}
    public StudyRegister(Member member, Post post) {
        this.member = member;
        this.post = post;
        //this.accept = null; // 처음 스터디 신청할 때는 수락 여부가 null이 됨
    }

}
