package test.study.member.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import test.study.member.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class MemberJpaRepository implements MemberRepository {
    @PersistenceContext
    EntityManager em;

    @Override //회원 저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override // loginId로 회원찾기
    public Member findByLoginId(String loginId) {
        try{
            String jpql = "select m FROM Member m WHERE m.loginId = :loginId";
            return em.createQuery(jpql,Member.class)
                    .setParameter("loginId",loginId)
                    .getSingleResult();
        } catch (NoResultException e){ // 쿼리 결과가 없는 경우
            return null;
        }
    }

    @Override // db에 저장된 id로 회원찾기
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll() { // 등록된 회원 모두 찾기
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
