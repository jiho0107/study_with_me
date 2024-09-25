package test.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import test.study.member.domain.Member;
import test.study.study.studyGroup.repository.StudyGroupRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//@Controller
//@RequiredArgsConstructor
//public class HomeController {
//    private final StudyGroupRepository groupRepository;
//
//    @GetMapping("/")
//    public String home(HttpServletRequest request, Model model){
//        HttpSession session = request.getSession(false);
//        if(session == null){ // 세션이 없는 경우
//            return "home";
//        }
//
//        Member loginMember = (Member) session.getAttribute("loginMember");
//        if(loginMember == null){ // 세션에 회원 데이터가 없는 경우
//            return "home";
//        }
//
//        // 로그인 된 회원인 경우
//        model.addAttribute("member",loginMember);
//        return "main";
//    }
//
//    @GetMapping("/myPage") // 마이페이지
//    public String myPage(HttpServletRequest request, Model model){
//        HttpSession session = request.getSession(false);
//        Member member = (Member)session.getAttribute("loginMember");
//        model.addAttribute("member",member);
//        return "myPage";
//    }
//}
