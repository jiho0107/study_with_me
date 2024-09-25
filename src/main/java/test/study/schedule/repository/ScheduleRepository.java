package test.study.schedule.repository;

import test.study.schedule.domain.Schedule;

import java.util.List;

public interface ScheduleRepository {
    public Schedule save(Schedule schedule); // 일정 저장
    public List<Schedule> findAllSchedule(); // 전체 일정 조회
    public List<Schedule> mySchedule(Long userId); // 사용자가 등록한 일정 목록 조회
    public Schedule findSchedule(Long scheduleId); // 일정id로 일정 찾기
    public List<Schedule> scheduleOfGroup(Long groupId); // 해당 그룹의 일정 목록 조회
}
