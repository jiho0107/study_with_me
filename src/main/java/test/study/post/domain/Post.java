package test.study.post.domain;

import lombok.Getter;
import lombok.Setter;
import test.study.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long id; //게시글 id

    @Column(nullable = false,length = 30)
    private String title; //게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    @Lob
    private String content; //게시글 내용

    //@Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; //게시글 작성자

    public Post(){}
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
    public Post(String title, String content){
        this.title = title;
        this.content = content;
    }
}
