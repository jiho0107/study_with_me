package test.study.study.studyGroup.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.post.service.PostService;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.dto.StudyGroupDTO;
import test.study.study.studyGroup.service.StudyGroupService;
import test.study.study.studyParticipants.domain.ParticipantPosition;
import test.study.study.studyParticipants.domain.StudyParticipants;
import test.study.study.studyParticipants.service.StudyParticipantsService;
import test.study.study.studyRegister.domain.StudyRegister;
import test.study.study.studyRegister.service.StudyRegisterService;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;
    private final PostService postService;
    private final StudyRegisterService studyRegisterService;
    private final StudyParticipantsService studyParticipantsService;

    @PostMapping("/{postId}") // 스터디 그룹 형성
    public ResponseEntity<Object> formGroup(@PathVariable Long postId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        Post post = postService.findPost(postId);
        List<StudyRegister> notDecided = new ArrayList<>(); // 수락이나 거절 여부가 정해지지 않은 지원자들
        List<StudyRegister> accepted = new ArrayList<>(); // 수락된 지원자들
        // 게시글이 존재하는 경우
        if(post != null){
            // 로그인한 회원이 게시글 작성자일 경우만 스터디 그룹 형성 가능
            if(Objects.equals(post.getMember().getId(), loginMember.getId())){
                List<StudyRegister> registerList = studyRegisterService.listOfMyPost(post);
                // 스터디 그룹이 아직 형성되지 않았을 때
                if(studyGroupService.findGroupByPost(postId) == null){
                    for(StudyRegister register : registerList){
                        if(Boolean.TRUE.equals(register.getAccept())){
                            accepted.add(register);
                        }
                        if(register.getAccept() == null){
                            notDecided.add(register);
                        }
                    }
                    if(!notDecided.isEmpty()){ // 수락이나 거절 여부가 정해지지 않은 지원자들이 존재하는 경우
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("모든 지원자들에 대한 수락/거절이 이루어지지 않았습니다.");
                    }
                    if(accepted.isEmpty()){ // 수락이나 거절 여부가 정해졌으나 수락된 회원이 1명 미만인 경우 -> 1명 이상은 수락해야 그룹 형성됨
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("1명 이상은 수락해야 그룹 형성이 가능합니다.");
                    }
                    // 스터디 그룹이 형성될 수 있는 조건일 때!!
                    StudyGroup group =  studyGroupService.formGroup(post);
                    studyParticipantsService.participate(loginMember,group);
                    for(StudyRegister register : accepted){ // 스터디 그룹 형성 후 스터디원들 생성됨
                        Member member = register.getMember();
                        studyParticipantsService.participate(member,group);
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body(new StudyGroupDTO(group));
                }
                // 스터디 그룹이 이미 형성된 경우
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 스터디 그룹이 형성되었습니다.");
            }
            // 게시글 작성자가 로그인한 회원이 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자가 아니라 그룹을 형성할 수 없습니다.");
        }
        //게시글이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글은 존재하지 않습니다.");
    }

    @PostMapping("/{groupId}/end") // 스터디 그룹 종료(조장만 가능)
    public ResponseEntity<String> endGroup(@PathVariable Long groupId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        StudyGroup group = studyGroupService.findGroup(groupId);
        List<StudyParticipants> participants = studyParticipantsService.findByGroupId(groupId);
        // 스터디 그룹이 존재하는 경우
        if(group != null){
            if(group.isActive()){ // 그룹이 활동중인 경우에만 그룹을 종료할 수 있음
                for(StudyParticipants participant : participants){
                    // 조장인 경우
                    if(Objects.equals(participant.getMember().getId(), loginMember.getId()) && participant.getPosition() == ParticipantPosition.LEADER){
                        studyGroupService.endGroup(group);
                        return ResponseEntity.status(HttpStatus.OK).body("스터디 그룹 활동이 종료되었습니다.");
                    }
                }
                // 조장이 아닌 경우
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("스터디장만이 그룹 활동을 종료할 수 있습니다.");
            }
            // 그룹이 활동을 이미 종료한 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 스터디 그룹 활동이 종료되었습니다.");
        }
        // 스터디 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 스터디 그룹은 존재하지 않습니다.");
    }

    @GetMapping("/me") // 사용자가 속한 스터디 그룹 목록
    public ResponseEntity<Object> myGroups(HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        List<StudyGroup> mygroups = studyGroupService.myList(loginMember);
        List<StudyGroupDTO> groupDTOs = new ArrayList<>();
        for(StudyGroup group : mygroups){
            groupDTOs.add(new StudyGroupDTO(group));
        }
        //return ResponseEntity.status(HttpStatus.OK).body(groupDTOs);
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(groupDTOs));
    }

    @GetMapping("/{groupId}") // 스터디 그룹 상세
    public ResponseEntity<Object> group(@PathVariable Long groupId, HttpServletRequest request){
        StudyGroup group = studyGroupService.findGroup(groupId);
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        List<StudyParticipants> participants = studyParticipantsService.findByGroupId(groupId);
        // 스터디 그룹이 존재하는 경우
        if(group != null){
            for(StudyParticipants participant : participants){
                // 로그인한 회원이 이 스터디그룹의 멤버일 경우
                if(Objects.equals(participant.getMember().getId(), loginMember.getId())) {
                    return ResponseEntity.status(HttpStatus.OK).body(new StudyGroupDTO(group));
                }
            }
            // 로그인한 회원이 이 스터디그룹의 멤버가 아닐 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 스터디그룹의 멤버가 아닙니다.");
        }
        //스터디 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 스터디 그룹은 존재하지 않습니다.");
    }


    // Result클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
    // 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
