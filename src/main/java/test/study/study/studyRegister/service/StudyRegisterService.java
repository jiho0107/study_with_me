package test.study.study.studyRegister.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyRegister.domain.StudyRegister;
import test.study.study.studyRegister.repository.StudyRegisterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRegisterService {
    private final StudyRegisterRepository studyRegisterRepository;

    @Transactional
    public StudyRegister register(Member member, Post post){ // 게시글 작성자가 아닐때만 지원 가능
        return studyRegisterRepository.register(member, post);
    }

    @Transactional
    public StudyRegister accept(StudyRegister register){ // 조장일 경우만 수락 가능
        return studyRegisterRepository.accept(register);
    }

    @Transactional
    public StudyRegister reject(StudyRegister register){ // 조장일 경우만 거절 가능
        return studyRegisterRepository.reject(register);
    }

    public List<StudyRegister> listOfMyPost(Post post){
        return studyRegisterRepository.listOfMyPost(post);
    }

    public List<StudyRegister> findMyRegister(Member member){
        return studyRegisterRepository.findMyRegister(member);
    }

    public boolean registerOrNot(Long memberId, Long postId){
        return studyRegisterRepository.registerOrNot(memberId, postId);
    }
}
