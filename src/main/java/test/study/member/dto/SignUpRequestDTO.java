package test.study.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import test.study.member.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class SignUpRequestDTO {
    @NotBlank(message = "로그인 id는 필수 입력 값입니다.")
    @Length(min = 5, max = 20, message = "로그인id는 5~20자리여야 합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 5, max = 20, message = "비밀번호는 5~20자리여야 합니다.")
    private String password;

    @Pattern(regexp = "^[가-힣]{2,30}$", message = "이름은 한글, 2~30자여야 합니다.")
    @Length(min = 2, max = 30, message = "이름은 2~30자리여야 합니다.")
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    public Member toEntity(){
        return new Member(loginId,name,password);
    }
    public MemberResponseDTO toMemberResponseDTO(){
        return new MemberResponseDTO(toEntity());
    }
}
