package test.study.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.study.member.dto.LoginRequestDTO;
import test.study.member.dto.MemberResponseDTO;
import test.study.member.domain.Member;
import test.study.member.dto.SignUpRequestDTO;
import test.study.member.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

//    @PostMapping("/members") // 회원가입 처리
//    public ResponseEntity<Object> join(@RequestBody @Validated SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult) {
//        if(bindingResult.hasErrors()){
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            Map<String,String> errorMap = new HashMap<>();
//            for(FieldError fieldError : fieldErrors){
//                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
//            //return ResponseEntity.badRequest().body(errorMap);
//            //return ResponseEntity.badRequest().body("입력한 데이터가 올바르지 않습니다.");
//        }
//
//        Member member = memberService.join(signUpRequestDTO);
//        if(member == null){ // 입력한 loginId 가 이미 존재하는 경우
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용중인 로그인id 입니다.");
//        }
//        // 회원가입 성공 시
//        return ResponseEntity.status(HttpStatus.CREATED).body(signUpRequestDTO.toMemberResponseDTO());
//    }

    @PostMapping("/members") // 회원가입
    public ResponseEntity<Object> join(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO){
        Member member = memberService.join(signUpRequestDTO);

        if(member == null){ // 입력한 loginId 가 이미 존재하는 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 로그인id 입니다.");
        }

        // 회원가입 성공 시
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDTO(member));
        //return ResponseEntity.ok(signUpRequestDTO.toMemberResponseDTO());
    }

    @PostMapping("/login") // 로그인 처리
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
                                                     HttpServletRequest request)
    {
        Member loginMember = memberService.login(loginRequestDTO.getLoginId(),loginRequestDTO.getPassword());
        if(loginMember == null) { // 로그인 실패 시
            Map<String,String> errorMap = new HashMap<>();
            errorMap.put("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        // 로그인 성공 시
        HttpSession session = request.getSession(true);
        Member member = memberService.findByLoginId(loginRequestDTO.getLoginId());
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO(member);
        session.setAttribute("loginMember",member);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDTO);
    }

    @PostMapping("/logout") // 로그아웃
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
//        if(session == null){
//            return new ResponseEntity<>("로그아웃 할 수 없습니다.",HttpStatus.BAD_REQUEST);
//        }
        session.invalidate();
        return new ResponseEntity<>("로그아웃 성공",HttpStatus.OK);
    }
}
