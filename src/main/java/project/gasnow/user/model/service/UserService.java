package project.gasnow.user.model.service;

import project.gasnow.user.model.dto.User;

import java.util.Map;

public interface UserService {

    /**
     * 회원가입 메서드
     * @param user 클라이언트가 view에서 form에 작성한 정보를 User 객체로 받아옴
     */
    void register(User user);
       
    /**
     * 아이디 중복 체크 메서드<br>
     * 클라이언트가 작성한 아이디를 받아 DB에서 일치하는 아이디가 있는지 검색
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디) <br>
     *           존재하지 않으면 true (사용 가능한 아이디)
     */
    boolean checkUserIdDuplicate(String userId);

    /**
     * 연락처 중복 체크 메서드<br>
     * 클라이언트가 작성한 연락처를 받아 DB에서 일치하는 아이디가 있는지 검색
     * @param userPhone 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디)<br>
     * 존재하지 않으면 true (사용 가능한 아이디)
     */
    boolean checkPhoneDuplicate(String userPhone);

    /**
     * 이메일 인증코드 전송 메서드
     * @param htmlName 이메일 전송 화면이 구현된 html 파일명
     * @param email 세션에 저장된 클라이언트의 이메일 정보
     * @return null 또는 인증코드
     */
    String sendEmail(String htmlName, String email);

    // 이메일 인증코드 확인

    /**
     * 이메일 인증코드 확인 메서드
     * @param map Map<String, Object>
     * @return mapper의 메서드에 map을 파라미터로 넘겨서 반환
     */
    int checkAuthKey(Map<String, Object> map);

    /**
     * 로그인 메서드
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @param password 클라이언트가 view에서 작성한 비밀번호
     */
    void login(String userId, String password);

    /**
     * 로그인 시 포인트 적립 메서드
     * @param userId 세션에 저장된 userId
     * @return 적립된 포인트
     */
    int addLoginPoint(String userId);

}
