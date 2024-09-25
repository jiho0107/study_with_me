package test.study.study.studyParticipants.repository;

import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyParticipants.domain.StudyParticipants;

import java.util.List;

public interface StudyParticipantsRepository {
    public StudyParticipants participate(Member member, StudyGroup group); // 스터디 참가
    public List<StudyParticipants> list(); // 스터디 참가자들 전체 목록
    public List<StudyParticipants> findByGroupId(Long groupId); // 해당 스터디그룹에 참여하는 참가자들 목록
    public StudyParticipants participant(Long memberId, Long groupId); // 해당 스터디그룹에 참여하고 있는 참가자
}
