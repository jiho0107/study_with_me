package test.study.post.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import test.study.member.domain.Member;
import test.study.post.domain.Post;
import test.study.post.dto.PostRequestDTO;
import test.study.post.dto.PostResponseDTO;
import test.study.post.repository.PostRepository;
import test.study.post.service.PostService;
import test.study.study.studyGroup.domain.StudyGroup;
import test.study.study.studyGroup.repository.StudyGroupRepository;
import test.study.study.studyGroup.service.StudyGroupService;
import test.study.study.studyRegister.domain.StudyRegister;
import test.study.study.studyRegister.repository.StudyRegisterRepository;
import test.study.study.studyRegister.service.StudyRegisterService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final StudyGroupService studyGroupService;

//    @PostMapping // 게시글 저장(등록)
//    public ResponseEntity<Object> save(@RequestBody @Validated PostRequestDTO postRequestDTO, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            Map<String, String> errorMap = new HashMap<>();
//            for(FieldError fieldError : fieldErrors){
//                errorMap.put(fieldError.getObjectName(), fieldError.getDefaultMessage());
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
//        }
//
//        postService.save(postRequestDTO.toEntity());
//        return ResponseEntity.status(HttpStatus.CREATED).body(postRequestDTO);
//    }

    @PostMapping // 게시글 저장(등록)
    public ResponseEntity<Object> save(@Valid @RequestBody PostRequestDTO postRequestDTO, HttpServletRequest request){
        Member loginMember = (Member)request.getSession(false).getAttribute("loginMember");
        Post post = postService.save(postRequestDTO.toEntity(loginMember));
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDTO(post));
    }

    @GetMapping // 게시글 전체 목록 조회
    public ResponseEntity<Object> allPosts(){
        List<Post> allPosts = postService.findAllPosts();
        List<PostResponseDTO> responsePosts = new ArrayList<>();
        for(Post post : allPosts){
            responsePosts.add(new PostResponseDTO(post));
        }
        //return ResponseEntity.status(HttpStatus.OK).body(responsePosts);
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(responsePosts));
    }

    @GetMapping("/me") // 사용자가 작성한 게시글 목록 조회
    public ResponseEntity<Object> myPosts(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("loginMember");
        List<Post> myPosts = postService.findMyPosts(member.getId());
        List<PostResponseDTO> responsePosts = new ArrayList<>();
        for(Post post : myPosts){
            responsePosts.add(new PostResponseDTO(post));
        }
        //return ResponseEntity.status(HttpStatus.OK).body(responsePosts);
        return ResponseEntity.status(HttpStatus.OK).body(new Result<>(responsePosts));
    }

    @GetMapping("/{postId}") // 게시글 상세화면
    public ResponseEntity<Object> post(@PathVariable Long postId){
        Post post = postService.findPost(postId);
        try {
            // 해당 게시글이 존재하는 경우
            PostResponseDTO postResponseDTO = new PostResponseDTO(post);
            return ResponseEntity.status(HttpStatus.OK).body(postResponseDTO);
        }catch (NullPointerException e){
            // 해당 게시글이 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글이 존재하지 않습니다.");
        }
    }

    @PostMapping("/{postId}") // 게시글 수정(스터디 그룹 형성 전 까지만 가능)
    public ResponseEntity<Object> editPost(@PathVariable Long postId, HttpServletRequest request,
                                                    @Valid @RequestBody PostRequestDTO updateDTO){
        Post post = postService.findPost(postId);
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("loginMember");

        // 게시글이 존재할 때
        if(post != null) {
            // 게시글 작성자가 로그인한 사용자인 경우
            if (Objects.equals(post.getMember().getId(), member.getId())) {
                // 스터디 그룹이 형성되지 않은 경우
                if (studyGroupService.findGroupByPost(postId) == null) {
                    Post edited = postService.edit(postId, updateDTO.toEntity(member));
                    return ResponseEntity.status(HttpStatus.OK).body(new PostResponseDTO(edited));
                }
                // 스터디 그룹이 이미 형성된 경우
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("스터디 그룹이 이미 형성돼 게시글을 수정할 수 없습니다.");
            }
            // 게시글 작성자가 로그인한 사용자가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자가 아니여서 게시글을 수정할 수 없습니다.");
        }
        // 게시글이 존재하지 않을 때
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 게시글입니다.");
    }

    @DeleteMapping("/{postId}") // 게시글 삭제(스터디 그룹 형성 전까지만 가능)
    public ResponseEntity<String> removePost(@PathVariable Long postId, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("loginMember");

        Post post = postService.findPost(postId);
        try {
            if(Objects.equals(post.getMember().getId(), member.getId())) { // 게시글 작성자가 로그인한 멤버인 경우
                if(studyGroupService.findGroupByPost(postId) == null){ // 스터디 그룹이 형성되지 않은 경우
                    postService.remove(postId);
                    return ResponseEntity.status(HttpStatus.OK).body("게시글을 삭제했습니다.");
                }
                // 스터디 그룹이 이미 형성된 경우
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("스터디 그룹이 형성돼 게시글을 삭제할 수 없습니다.");
            }
            // 게시글 작성자가 로그인한 멤버가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글 작성자가 아니라 게시글을 삭제할 수 없습니다.");
        }catch (NullPointerException | EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글은 존재하지 않습니다.");
        }
    }


    // Result클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
    // 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}
