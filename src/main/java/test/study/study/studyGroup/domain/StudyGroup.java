package test.study.study.studyGroup.domain;

import lombok.Getter;
import lombok.Setter;
import test.study.post.domain.Post;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "Post_UNIQUE",
                columnNames = {"post_id"}
        )
})
public class StudyGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id; // 그룹 번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시글 관련

    //@Column(name = "isActive")
    @Column(nullable = false)
    private boolean active; // 활동 여부

    public StudyGroup(){}
    public StudyGroup(Post post){
        this.post = post;
        this.active = true;
    }
}
