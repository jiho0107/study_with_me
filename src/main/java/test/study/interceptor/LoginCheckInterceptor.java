package test.study.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor { // 로그인 체크 인터셉터
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String requestURI = request.getRequestURI();

        //최초 로그인시
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        log.info("--------------인터셉터 진입-----------------");
        log.info("url:{} method:{}",requestURI,method);
        if("/login".equals(requestURI) && "POST".equals(method)){
            return true;
        }

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("loginMember") == null){
            //response.sendRedirect("/login?redirectURL="+requestURI);
            log.info("############# 로그인 인터셉터 - 세션 없음 ###############");
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("로그인한 회원만 이용 가능합니다.");
            return false;
        }
        log.info("############# 로그인 인터셉터 - 세션 있음 ###############");
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write("로그인 됨");
        return true;
    }
}
