package test.study.schedule.repository;

import org.springframework.stereotype.Repository;
import test.study.schedule.domain.Schedule;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ScheduleJpaRepository implements ScheduleRepository {
    @PersistenceContext
    EntityManager em;

    @Override // 일정 저장
    public Schedule save(Schedule schedule) {
        em.persist(schedule);
        return schedule;
    }

    @Override // 전체 일정 조회
    public List<Schedule> findAllSchedule() {
        return em.createQuery("select s from Schedule s", Schedule.class)
                .getResultList();
    }

    @Override // 사용자가 등록한 일정 목록 조회
    public List<Schedule> mySchedule(Long userId) {
        String jpql = "select s from Schedule s where s.member.id = :memberId";
        return em.createQuery(jpql,Schedule.class)
                .setParameter("memberId",userId)
                .getResultList();
    }

    @Override // 일정id로 일정 찾기
    public Schedule findSchedule(Long scheduleId) {
        return em.find(Schedule.class, scheduleId);
    }

    @Override // 해당 그룹의 일정 목록 조회
    public List<Schedule> scheduleOfGroup(Long groupId) {
        String jpql = "select s from Schedule s where s.studyGroup.id = :groupId";
        return em.createQuery(jpql, Schedule.class)
                .setParameter("groupId",groupId)
                .getResultList();
    }
}
