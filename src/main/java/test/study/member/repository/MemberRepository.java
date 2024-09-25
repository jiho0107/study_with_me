package test.study.member.repository;

import test.study.member.domain.Member;

import java.util.List;

public interface MemberRepository {
    public Member save(Member member); //회원 저장
    public Member findByLoginId(String loginId); // loginId로 회원찾기
    public Member findById(Long id); // db에 저장된 id로 회원찾기
    public List<Member> findAll(); // 등록된 회원 모두 찾기
}
