package test.study.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.schedule.domain.Schedule;
import test.study.schedule.repository.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule save(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAllSchedule(){
        return scheduleRepository.findAllSchedule();
    }

    public List<Schedule> mySchedule(Long userId){
        return scheduleRepository.mySchedule(userId);
    }

    public Schedule findSchedule(Long scheduleId){
        return scheduleRepository.findSchedule(scheduleId);
    }

    public List<Schedule> scheduleOfGroup(Long groupId){
        return scheduleRepository.scheduleOfGroup(groupId);
    }
}
