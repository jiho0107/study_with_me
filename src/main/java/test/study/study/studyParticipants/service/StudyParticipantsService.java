package test.study.study.studyParticipants.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyParticipants.domain.StudyParticipants;
import test.study.study.studyParticipants.repository.StudyParticipantsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyParticipantsService {
    private final StudyParticipantsRepository studyParticipantsRepository;

    @Transactional
    public StudyParticipants participate(Member member, StudyGroup group){
        return studyParticipantsRepository.participate(member, group);
    }

    public List<StudyParticipants> list(){
        return studyParticipantsRepository.list();
    }

    public List<StudyParticipants> findByGroupId(Long groupId){
        return studyParticipantsRepository.findByGroupId(groupId);
    }

    public StudyParticipants participant(Long memberId, Long groupId){
        return studyParticipantsRepository.participant(memberId, groupId);
    }
}
