package project.gasnow.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.gasnow.user.model.dto.User;

/**
 * 관리자 권한 체크 자동화 인터셉터
 */
@Component
public class  AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");session.getAttribute("loginUser");

        // 관리자가 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        if(loginUser==null){
            response.sendRedirect("/login");
            return false;
        }

        if(!"ADMIN".equals(loginUser.getStatus())){
            response.sendRedirect("/");
            return false;
        }

        return true;
    }

}
