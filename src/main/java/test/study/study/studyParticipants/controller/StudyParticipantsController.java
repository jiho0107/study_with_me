package test.study.study.studyParticipants.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.study.member.domain.Member;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.service.StudyGroupService;
import test.study.study.studyParticipants.domain.StudyParticipants;
import test.study.study.studyParticipants.dto.StudyParticipantsDTO;
import test.study.study.studyParticipants.service.StudyParticipantsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class StudyParticipantsController {
    private final StudyParticipantsService studyParticipantsService;
    private final StudyGroupService studyGroupService;

    @GetMapping("/me/{groupId}") // 해당 그룹에서 나의 참가 조회
    public ResponseEntity<Object> participationOfGroup(@PathVariable Long groupId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        StudyGroup findGroup = studyGroupService.findGroup(groupId);
        // 해당 그룹이 존재하는 경우
        if(findGroup != null){
            StudyParticipants participant = studyParticipantsService.participant(loginMember.getId(), groupId);
            if(participant != null){ // 해당 그룹에 참여하는 경우
                StudyParticipantsDTO participantsDTO = new StudyParticipantsDTO(participant);
                return ResponseEntity.status(HttpStatus.OK).body(participantsDTO);
            }
            // 해당 그룹에 참여하지 않는 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사용자는 현재 해당 그룹에 참여하고 있지 않습니다.");
        }
        // 해당 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 그룹은 존재하지 않습니다.");
    }
}
