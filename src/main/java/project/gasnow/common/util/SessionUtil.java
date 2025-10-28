package project.gasnow.common.util;

import jakarta.servlet.http.HttpSession;
import project.gasnow.user.model.dto.User;

/**
 * 로그인 세션 설정 클래스
 */
public class SessionUtil {
    private static final String LOGIN_USER = "loginUser";

    /**
     * 로그인된 사용자 정보 세션에 저장하는 메서드 (30분 간)
     * @param session - 클라이언트의 세션 정보
     * @param user  컨트롤러에서 전달받을 user 정보
     */
    public static void setLoginUser(HttpSession session, User user) {
        session.setAttribute(LOGIN_USER, user);
        session.setMaxInactiveInterval(1800);
    }

    /**
     * 세션에서 로그인된 사용자 정보 가져오는 메서드
     * @param session - 클라이언트의 세션 정보
     * @return - "loginUser" 키로 저장된 user 객체
     */
    public static User getLoginUser(HttpSession session) {
        return (User) session.getAttribute(LOGIN_USER);
    }

    /**
     * 로그인 여부 확인 메서드
     * @param session - 클라이언트의 세션 정보
     * @return - 세션에 "loginUser"가 저장되어 있는지 여부 (true/false)
     */
    public static boolean isLoginUser(HttpSession session) {
        return session.getAttribute(LOGIN_USER) != null;
    }

    /**
     * 로그아웃 (세션 무효화) 메서드
     * @param session - 클라이언트의 세션 정보
     */
    public static void logout(HttpSession session) {
        session.invalidate();
    }
}
