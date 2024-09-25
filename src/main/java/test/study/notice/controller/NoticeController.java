package test.study.notice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.study.member.domain.Member;
import test.study.notice.domain.Notice;
import test.study.notice.dto.NoticeRequestDTO;
import test.study.notice.dto.NoticeResponseDTO;
import test.study.notice.service.NoticeService;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.service.StudyGroupService;
import test.study.study.studyParticipants.domain.StudyParticipants;
import test.study.study.studyParticipants.service.StudyParticipantsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeService noticeService;
    private final StudyGroupService studyGroupService;
    private final StudyParticipantsService studyParticipantsService;

    @PostMapping("/{groupId}") // 공지사항 등록
    public ResponseEntity<Object> save(@PathVariable Long groupId, @Valid @RequestBody NoticeRequestDTO noticeDTO,
                                       HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        StudyGroup group = studyGroupService.findGroup(groupId);
        // 해당 그룹이 존재하는 경우
        if(group != null){
            List<StudyParticipants> participants = studyParticipantsService.findByGroupId(groupId);
            for(StudyParticipants participant : participants){
                // 로그인한 멤버가 해당 그룹의 멤버일 경우만 등록 가능
                if(Objects.equals(participant.getMember().getId(), loginMember.getId())) {
                    Notice notice = new Notice(noticeDTO.getTitle(), noticeDTO.getContent(), loginMember, group);
                    noticeService.save(notice);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new NoticeResponseDTO(notice));
                }
            }
            // 로그인한 멤버가 해당 그룹의 멤버가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 그룹의 멤버가 아니어서 공지사항을 등록할 수 없습니다.");
        }
        // 해당 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 스터디 그룹은 존재하지 않습니다.");
    }

//    @GetMapping("/myGroups") // 사용자의 그룹별 공지사항 목록
//    public ResponseEntity<Object> listPerGroup(HttpServletRequest request){
//        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
//        Map<Long, List<NoticeResponseDTO>> myNotices = new HashMap<>(); // key=groupId, value=List<NoticeResponseDTO>
//        List<StudyGroup> groups = studyGroupService.myList(loginMember);
//        for(StudyGroup group : groups){
//            List<Notice> notices = noticeService.noticesOfGroup(group.getId());
//            List<NoticeResponseDTO> noticeResponseDTOs = new ArrayList<>();
//            for(Notice notice : notices){
//                noticeResponseDTOs.add(new NoticeResponseDTO(notice));
//            }
//            myNotices.put(group.getId(), noticeResponseDTOs);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(myNotices);
//    }

    @GetMapping("/group/{groupId}") // 해당 그룹의 공지사항 목록 조회
    public ResponseEntity<Object> listOfGroup(@PathVariable Long groupId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        StudyGroup group = studyGroupService.findGroup(groupId);
        // 해당 그룹이 존재하는 경우
        if(group != null){
            // 로그인한 회원이 해당 그룹의 멤버인 경우
            if(studyParticipantsService.participant(loginMember.getId(), groupId) != null){
                List<Notice> notices = noticeService.noticesOfGroup(groupId);
                List<NoticeResponseDTO> noticeResponseDTOs = new ArrayList<>();
                for (Notice notice : notices) {
                    noticeResponseDTOs.add(new NoticeResponseDTO(notice));
                }
                //return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDTOs);
                return ResponseEntity.status(HttpStatus.OK).body(new Result<>(noticeResponseDTOs));
            }
            // 로그인한 회원이 해당 그룹의 멤버가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 그룹의 멤버가 아니라 이 그룹의 공지사항 목록을 볼 수 없습니다.");
        }
        // 해당 그룹이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 스터디 그룹은 존재하지 않습니다.");
    }

    @GetMapping("/{noticeId}") // 공지사항 상세
    public ResponseEntity<Object> notice(@PathVariable Long noticeId, HttpServletRequest request){
        Notice notice = noticeService.findNotice(noticeId);

        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        // 공지사항이 존재하는 경우
        if (notice != null) {
            StudyGroup group = notice.getGroup(); // 공지사항이 소속돼 있는 스터디 그룹
            // 로그인한 회원이 공지사항이 소속돼 있는 스터디 그룹의 멤버인지 알기위해
            StudyParticipants participant = studyParticipantsService.participant(loginMember.getId(), group.getId());
            // 로그인한 회원이 해당 그룹의 멤버인 경우
            if(participant != null){
                return ResponseEntity.status(HttpStatus.OK).body(new NoticeResponseDTO(notice));
            }
            // 로그인한 회원이 해당 그룹의 멤버가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 공지사항이 소속되어 있는 그룹의 멤버만 공지사항을 확인할 수 있습니다.");
        }
        // 공지사항이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 공지사항이 존재하지 않습니다.");
    }


    // Result클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
    // 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
