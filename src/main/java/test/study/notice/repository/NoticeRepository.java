package test.study.notice.repository;

import test.study.notice.domain.Notice;

import java.util.List;

public interface NoticeRepository {
    public Notice save(Notice notice); // 공지사항 저장
    public List<Notice> findAllNotices(); // 공지사항 전체 조회
    public Notice findNotice(Long noticeId); // 공지사항id로 공지사항 찾기
    public List<Notice> noticesOfGroup(Long groupId); // 해당 그룹의 공지사항 목록
}
