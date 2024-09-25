package test.study.study.studyRegister.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.study.member.domain.Member;
import test.study.member.service.MemberService;
import test.study.post.domain.Post;
import test.study.post.service.PostService;
import test.study.study.studyRegister.domain.StudyRegister;
import test.study.study.studyRegister.dto.StudyRegisterDTO;
import test.study.study.studyRegister.service.StudyRegisterService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registers")
@Slf4j
public class StudyRegisterController {
    private final StudyRegisterService studyRegisterService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/{postId}") // 스터디 신청하기
    public ResponseEntity<Object> register(@PathVariable Long postId, HttpServletRequest request){
        Post post = postService.findPost(postId);
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        // 해당 게시글이 존재하는 경우
        if(post != null) {
            // 게시글 작성자와 스터디 지원자가 다를 경우만 스터디 신청이 가능함
            if (!Objects.equals(loginMember.getId(), post.getMember().getId())) {
                // 아직 해당 게시글에 스터디 신청을 하지 않은 경우
                if (!studyRegisterService.registerOrNot(loginMember.getId(), postId)) {
                    if(!studyRegisterService.registerOrNot(loginMember.getId(), postId)){
                        StudyRegister register = studyRegisterService.register(loginMember, post);
                        return ResponseEntity.status(HttpStatus.CREATED).body(new StudyRegisterDTO(register));
                    }
                }
                // 이미 해당 게시글에 스터디 신청을 한 경우
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 해당 게시글에 스터디 신청을 했습니다.");
            }
            // 게시글 작성자와 스터디 지원자가 같은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 작성자는 지원할 수 없습니다.");
        }
        // 해당 게시글이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글은 존재하지 않습니다.");
    }

    @PostMapping("/{postId}/{memId}/accept") // 신청자 수락
    public ResponseEntity<Object> accept(@PathVariable Long postId, @PathVariable Long memId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        Member applicant = memberService.findById(memId); // 신청자
        Post post = postService.findPost(postId);

        // 게시글이 존재하는 경우
        if(post != null){
            // 로그인한 회원이 게시글 작성자일 경우
            if(Objects.equals(post.getMember().getId(), loginMember.getId())) {
                List<StudyRegister> registers = studyRegisterService.listOfMyPost(post);
                for(StudyRegister register : registers){
                    // 신청자가 스터디 신청을 한 상태일 때
                    if(Objects.equals(register.getMember().getId(), applicant.getId())){
                        StudyRegister studyRegister = studyRegisterService.accept(register);
                        return ResponseEntity.status(HttpStatus.OK).body(new StudyRegisterDTO(studyRegister));
                    }
                }
                // 신청자가 스터디 신청을 하지 않은 상태일 때
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청자가 스터디 신청을 하지 않은 상태입니다.");
            }
            // 로그인한 회원이 게시글 작성자가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자가 아니라 신청자를 수락할 수 없습니다.");
        }
        // 해당 게시글이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글은 존재하지 않습니다.");
    }

    @PostMapping("/{postId}/{memId}/reject") // 신청자 거절
    public ResponseEntity<Object> reject(@PathVariable Long postId, @PathVariable Long memId, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        Member applicant = memberService.findById(memId); // 신청자
        Post post = postService.findPost(postId);

        // 게시글이 존재하는 경우
        if(post != null){
            // 로그인한 회원이 게시글 작성자일 경우
            if(Objects.equals(post.getMember().getId(), loginMember.getId())) {
                List<StudyRegister> registers = studyRegisterService.listOfMyPost(post);
                for(StudyRegister register : registers){
                    // 신청자가 스터디 신청을 한 상태일 때
                    if(Objects.equals(register.getMember().getId(), applicant.getId())){
                        StudyRegister studyRegister = studyRegisterService.reject(register);
                        return ResponseEntity.status(HttpStatus.OK).body(new StudyRegisterDTO(studyRegister));
                    }
                }
                // 신청자가 스터디 신청을 하지 않은 상태일 때
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청자가 스터디 신청을 하지 않은 상태입니다.");
            }
            // 로그인한 회원이 게시글 작성자가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자가 아니라 신청자를 거절할 수 없습니다.");
        }
        // 게시글이 존재하지 않는 경우
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글은 존재하지 않습니다.");
    }
}
