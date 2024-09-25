package test.study.study.studyGroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.repository.StudyGroupRepository;
import test.study.study.studyParticipants.repository.StudyParticipantsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public StudyGroup formGroup(Post post){
        return studyGroupRepository.formGroup(post);
    }

    @Transactional
    public void endGroup(StudyGroup group){ // 스터디 조장만 스터디 그룹 활동 종료 가능함
        studyGroupRepository.endGroup(group);
    }

    public List<StudyGroup> getList(){
        return studyGroupRepository.getList();
    }

    public List<StudyGroup> myList(Member member){
        return studyGroupRepository.myList(member);
    }

    public StudyGroup findGroup(Long groupId){
        return studyGroupRepository.findGroup(groupId);
    }
    public StudyGroup findGroupByPost(Long postId){
        return studyGroupRepository.findGroupByPost(postId);
    }
}
