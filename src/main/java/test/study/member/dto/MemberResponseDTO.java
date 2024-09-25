package test.study.member.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import test.study.member.domain.Member;

@Getter @Setter
public class MemberResponseDTO {
    private Long id;
    private String loginId;
    private String name;

    public MemberResponseDTO(Member member){
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
