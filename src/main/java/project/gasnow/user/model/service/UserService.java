package project.gasnow.user.model.service;

import project.gasnow.user.model.dto.User;

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

    // 이메일 인증코드 전송

    // 이메일 인증코드 확인

    // 로그인

    // 포인트 적립 (로그인 시)


}
