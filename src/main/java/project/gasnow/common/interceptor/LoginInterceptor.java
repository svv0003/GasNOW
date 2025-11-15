package project.gasnow.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.gasnow.user.model.dto.User;

/**
 * 로그인 체크 자동화 인터셉터
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");

        if(loginUser==null){
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
