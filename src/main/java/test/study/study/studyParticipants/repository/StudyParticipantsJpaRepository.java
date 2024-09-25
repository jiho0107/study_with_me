package test.study.study.studyParticipants.repository;

import org.springframework.stereotype.Repository;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyParticipants.domain.ParticipantPosition;
import test.study.study.studyParticipants.domain.StudyParticipants;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StudyParticipantsJpaRepository implements StudyParticipantsRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public StudyParticipants participate(Member member, StudyGroup group) {
        StudyParticipants studyParticipants = new StudyParticipants(group, member);
        if(member.getId() == group.getPost().getMember().getId()){ // 스터디 참여자가 게시글 작성자일 경우 -> LEADER
            studyParticipants.setPosition(ParticipantPosition.LEADER);
        }
        else{
            studyParticipants.setPosition(ParticipantPosition.MEMBER); // 그 외 참여자들 -> MEMBER
        }
        em.persist(studyParticipants);
        return studyParticipants;
    }

    @Override
    public List<StudyParticipants> list() {
        return em.createQuery("select p from StudyParticipants p", StudyParticipants.class)
                .getResultList();
    }

    @Override
    public List<StudyParticipants> findByGroupId(Long groupId) {
        String jpql = "select p from StudyParticipants p where p.group.id = :groupId";
        return em.createQuery(jpql,StudyParticipants.class)
                .setParameter("groupId",groupId)
                .getResultList();
    }

    @Override
    public StudyParticipants participant(Long memberId, Long groupId) {
        String jpql = "select p from StudyParticipants p where p.member.id = :memberId and p.group.id = :groupId";
        try{
            return em.createQuery(jpql, StudyParticipants.class)
                    .setParameter("memberId",memberId)
                    .setParameter("groupId",groupId)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
