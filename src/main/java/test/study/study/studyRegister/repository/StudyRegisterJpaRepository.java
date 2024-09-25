package test.study.study.studyRegister.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.study.studyRegister.domain.StudyRegister;
import test.study.study.studyRegister.dto.StudyRegisterDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class StudyRegisterJpaRepository implements StudyRegisterRepository {
    @PersistenceContext
    EntityManager em;

    @Override // 스터디 신청(스터디원)
    public StudyRegister register(Member member, Post post) {
        StudyRegister studyRegister = new StudyRegister(member, post);
        em.persist(studyRegister);
        return studyRegister;
    }

    @Override // 스터디 신청 수락(스터디 조장)
    public StudyRegister accept(StudyRegister register) {
        StudyRegister studyRegister = em.find(StudyRegister.class, register.getId());
        studyRegister.setAccept(true);
        return studyRegister;
    }

    @Override // 스터디 신청 거절(스터디 조장)
    public StudyRegister reject(StudyRegister register) {
        StudyRegister studyRegister = em.find(StudyRegister.class, register.getId());
        studyRegister.setAccept(false);
        return studyRegister;
    }

    @Override // 전체 스터디 신청 목록
    public List<StudyRegister> getList() {
        return em.createQuery("select r from StudyRegister r", StudyRegister.class)
                .getResultList();
    }

    @Override // 사용자가 쓴 게시글에 신청한 신청자 목록
    public List<StudyRegister> listOfMyPost(Post post) {
        String jpql = "select r from StudyRegister r where r.post.id = :postId";
        return em.createQuery(jpql, StudyRegister.class)
                .setParameter("postId",post.getId())
                .getResultList();
    }

    @Override // 사용자의 스터디 신청 목록
    public List<StudyRegister> findMyRegister(Member member) {
        String jpql = "select r from StudyRegister r where r.member.id = :memberId";
        return em.createQuery(jpql,StudyRegister.class)
                .setParameter("memberId",member.getId())
                .getResultList();
    }

    @Override // 해당 게시글에 사용자가 신청을 했는지 안했는지 여부
    public boolean registerOrNot(Long memberId, Long postId) {
        String jpql = "select r from StudyRegister r where r.member.id = :memberId and r.post.id = :postId";
        try{
            StudyRegister register = em.createQuery(jpql, StudyRegister.class)
                    .setParameter("memberId", memberId)
                    .setParameter("postId", postId)
                    .getSingleResult();
            if(register != null)
                return true;
        }catch (NoResultException e){
            return false;
        }
        return false;
    }
}
