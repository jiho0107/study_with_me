package test.study.schedule.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.study.member.domain.Member;
import test.study.schedule.domain.Schedule;
import test.study.schedule.dto.ScheduleRequestDTO;
import test.study.schedule.dto.ScheduleResponseDTO;
import test.study.schedule.domain.ScheduleType;
import test.study.schedule.service.ScheduleService;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.service.StudyGroupService;
import test.study.study.studyParticipants.domain.StudyParticipants;
import test.study.study.studyParticipants.service.StudyParticipantsService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final StudyGroupService studyGroupService;
    private final StudyParticipantsService studyParticipantsService;

    @PostMapping // 일정 등록
    public ResponseEntity<Object> save(@Valid @RequestBody ScheduleRequestDTO scheduleRequestDTO,
                                       HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");

        // 개인 일정인 경우
        if(scheduleRequestDTO.getType() == ScheduleType.PERSONAL){
            Schedule schedule = new Schedule(loginMember, scheduleRequestDTO.getDate(), scheduleRequestDTO.getTitle(), ScheduleType.PERSONAL);
            scheduleService.save(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ScheduleResponseDTO(schedule));
        }
        // 그룹 일정인 경우
        // 스터디 그룹 id 를 적은 경우
        if(scheduleRequestDTO.getStudyGroup() != null){
            // 해당 스터디 그룹이 존재하는 경우
            StudyGroup findGroup = studyGroupService.findGroup(scheduleRequestDTO.getStudyGroup());
            if(findGroup != null){
                List<StudyGroup> groups = studyGroupService.myList(loginMember);
                for (StudyGroup group : groups) {
                    // 로그인한 회원이 해당 스터디 그룹의 멤버인 경우
                    if(Objects.equals(group.getId(), scheduleRequestDTO.getStudyGroup())){
                        Schedule schedule = new Schedule(loginMember, scheduleRequestDTO.getDate(), scheduleRequestDTO.getTitle(), ScheduleType.GROUP, findGroup);
                        scheduleService.save(schedule);
                        return ResponseEntity.status(HttpStatus.CREATED).body(new ScheduleResponseDTO(schedule));
                    }
                }
                // 로그인한 회원이 해당 스터디 그룹의 멤버가 아닌 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 그룹의 멤버가 아니라 해당 그룹의 일정을 등록할 수 없습니다.");
            }
            //해당 스터디 그룹이 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 그룹은 존재하지 않습니다.");
        }
        // 그룹 일정인데 스터디 그룹 id가 null인 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("그룹 일정 등록 시 그룹id는 필수 입력값입니다.");
    }

//    @GetMapping("/groups") // 그룹별 일정 목록 조회
//    public ResponseEntity<Object> schedulesByGroup(HttpServletRequest request){
//        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
//        Map<Long, List<ScheduleResponseDTO>> groupSchedule = new HashMap<>();
//        List<StudyGroup> groups = studyGroupService.myList(loginMember);
//        for (StudyGroup group : groups) {
//            List<Schedule> schedules = scheduleService.scheduleOfGroup(group.getId());
//            List<ScheduleResponseDTO> scheduleResponseDTOs = new ArrayList<>();
//            for (Schedule schedule : schedules) {
//                scheduleResponseDTOs.add(new ScheduleResponseDTO(schedule));
//            }
//            groupSchedule.put(group.getId(),scheduleResponseDTOs);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(groupSchedule);
//    }

    @GetMapping("/personal") // 개인 일정 목록 조회
    public ResponseEntity<Object> personalSchedules(HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        List<Schedule> schedules = scheduleService.mySchedule(loginMember.getId());
        List<Schedule> mySchedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if(schedule.getType() == ScheduleType.PERSONAL){
                mySchedules.add(schedule);
            }
        }
        List<ScheduleResponseDTO> scheduleResponseDTOs = new ArrayList<>();
        for(Schedule schedule : mySchedules){
            scheduleResponseDTOs.add(new ScheduleResponseDTO(schedule));
        }
        //return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(scheduleResponseDTOs));
    }

    @GetMapping("/{groupId}") // 해당 그룹의 일정 목록 조회
    public ResponseEntity<Object> schedulesOfGroup(@PathVariable Long groupId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        StudyGroup group = studyGroupService.findGroup(groupId);
        // 해당 그룹이 존재하는 경우
        if(group != null){
            List<Schedule> schedules = scheduleService.scheduleOfGroup(groupId);
            StudyParticipants participant = studyParticipantsService.participant(loginMember.getId(), groupId);
            if(participant != null){ // 로그인한 회원이 해당 그룹의 멤버일 경우
                List<ScheduleResponseDTO> scheduleResponseDTOs = new ArrayList<>();
                for(Schedule schedule : schedules){
                    scheduleResponseDTOs.add(new ScheduleResponseDTO(schedule));
                }
                //return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseDTOs);
                return ResponseEntity.status(HttpStatus.OK).body(new Result<>(scheduleResponseDTOs));
            }
            // 로그인한 회원이 해당 그룹의 멤버가 아닐 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 그룹의 멤버가 아니라 해당 그룹의 일정을 볼 수 없습니다.");
        }
        // 해당 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 그룹은 존재하지 않습니다.");
    }


    // Result클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
    // 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
