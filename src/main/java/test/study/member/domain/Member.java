package test.study.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "LoginId_UNIQUE",
                columnNames = {"loginId"}
        )
})
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; // db에 저장되고 관리되는 아이디

    @Column(nullable = false,length = 20)
    private String loginId; //회원 로그인id
    @Column(nullable = false, length = 30)
    private String name; //회원 이름
    @Column(nullable = false,length = 20)
    private String passwd; //회원 비밀번호

    public Member(){}

    public Member(String loginId, String name, String passwd) {
        this.loginId = loginId;
        this.name = name;
        this.passwd = passwd;
    }
}
