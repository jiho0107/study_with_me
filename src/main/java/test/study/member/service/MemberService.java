package test.study.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.member.domain.Member;
import test.study.member.dto.SignUpRequestDTO;
import test.study.member.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }

    public Member findById(Long id){
        return memberRepository.findById(id);
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    @Transactional
    public Member join(SignUpRequestDTO signUpRequestDTO){
        Member findMember = memberRepository.findByLoginId(signUpRequestDTO.getLoginId());
        if(findMember != null){ // loginId 가 겹칠때
            return null;
        }
        // loginId가 안 겹칠 때
        Member member = new Member(signUpRequestDTO.getLoginId(),signUpRequestDTO.getName(), signUpRequestDTO.getPassword());
        return memberRepository.save(member);
    }

    @Transactional
    public Member login(String loginId, String password){
        Member member = memberRepository.findByLoginId(loginId);
        try {
            if(member.getPasswd().equals(password))
                return member;
        }catch (NullPointerException e){ // 회원이 존재하지 않는 경우
            return null;
        }
        // 비밀번호가 다른 경우
        return null;
    }
}
