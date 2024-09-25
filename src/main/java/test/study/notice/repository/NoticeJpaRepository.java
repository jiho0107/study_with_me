package test.study.notice.repository;

import org.springframework.stereotype.Repository;
import test.study.notice.domain.Notice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class NoticeJpaRepository implements NoticeRepository {
    @PersistenceContext
    EntityManager em;

    @Override // 공지사항 저장
    public Notice save(Notice notice) {
        em.persist(notice);
        return notice;
    }

    @Override // 공지사항 전체 조회
    public List<Notice> findAllNotices() {
        return em.createQuery("select n from Notice n", Notice.class)
                .getResultList();
    }

    @Override // 공지사항id로 공지사항 찾기
    public Notice findNotice(Long noticeId) {
        return em.find(Notice.class, noticeId);
    }

    @Override // 해당 그룹의 공지사항 목록
    public List<Notice> noticesOfGroup(Long groupId) {
        String jpql = "select n from Notice n where n.group.id=:groupId";
        return em.createQuery(jpql,Notice.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }
}
