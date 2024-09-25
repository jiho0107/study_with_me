package test.study.study.studyGroup.repository;

import org.springframework.stereotype.Repository;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyGroup.domain.StudyGroup;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
public class StudyGroupJpaRepository implements StudyGroupRepository {
    @PersistenceContext
    EntityManager em;

    @Override // 스터디 그룹 형성
    public StudyGroup formGroup(Post post) {
        StudyGroup studyGroup = new StudyGroup(post);
        em.persist(studyGroup);
        return studyGroup;
    }

    @Override // 스터디 그룹 활동 종료(조장만 할 수 있음)
    public void endGroup(StudyGroup group) {
        StudyGroup studyGroup = em.find(StudyGroup.class, group.getId());
        studyGroup.setActive(false);
    }

    @Override // 전체 스터디 그룹 목록
    public List<StudyGroup> getList() {
        return em.createQuery("select g from StudyGroup g",StudyGroup.class)
                .getResultList();
    }

    @Override // 사용자가 속한 스터디 그룹 목록
    public List<StudyGroup> myList(Member member) {
        String jpql = "select p.group from StudyParticipants p where p.member.id = :memberId";
        return em.createQuery(jpql,StudyGroup.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    @Override // 그룹id로 그룹찾기
    public StudyGroup findGroup(Long groupId) {
        return em.find(StudyGroup.class, groupId);
    }

    @Override // 게시글id로 그룹찾기
    public StudyGroup findGroupByPost(Long postId) {
        String jpql = "select g from StudyGroup g where g.post.id = :postId";
        try{
            return em.createQuery(jpql,StudyGroup.class)
                    .setParameter("postId",postId)
                    .getSingleResult();
        }catch (NoResultException e){ // 결과가 없을 시
            return null;
        }
    }
}
