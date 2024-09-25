package test.study.study.studyRegister.repository;

import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyRegister.domain.StudyRegister;

import java.util.List;

public interface StudyRegisterRepository {
    public StudyRegister register(Member member, Post post); // 스터디 신청(스터디원)
    public StudyRegister accept(StudyRegister register); // 스터디 신청 수락(스터디 조장)
    public StudyRegister reject(StudyRegister register); // 스터디 신청 거절(스터디 조장)
    public List<StudyRegister> getList(); // 전체 스터디 신청 목록
    public List<StudyRegister> listOfMyPost(Post post); // 사용자가 쓴 게시글에 신청한 신청자 목록
    public List<StudyRegister> findMyRegister(Member member); // 사용자의 스터디 신청 목록
    public boolean registerOrNot(Long memberId, Long postId); // 해당 게시글에 사용자가 신청을 했는지 안했는지 여부
}
