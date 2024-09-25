package test.study.study.studyGroup.repository;

import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyGroup.domain.StudyGroup;

import java.util.List;
import java.util.Map;

public interface StudyGroupRepository {
    public StudyGroup formGroup(Post post); // 스터디 그룹 형성
    public void endGroup(StudyGroup group); // 스터디 그룹 활동 종료(조장만 할 수 있음)
    public List<StudyGroup> getList(); // 전체 스터디 그룹 목록
    public List<StudyGroup> myList(Member member); // 사용자가 속한 스터디 그룹 목록
    public StudyGroup findGroup(Long groupId); // 그룹id로 그룹찾기
    public StudyGroup findGroupByPost(Long postId); // 게시글id로 그룹찾기
}
