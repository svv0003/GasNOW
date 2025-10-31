package project.gasnow.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;
import project.gasnow.user.model.mapper.UserMapper;
import project.gasnow.user.model.mapper.UserPointMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserPointMapper userPointMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 회원가입 메서드
     * @param user 클라이언트가 view에서 form에 작성한 정보를 User 객체로 받아옴
     */
    @Transactional
    @Override
    public void register(User user) {
        // 아이디 중복 체크
        if(!checkUserIdDuplicate(user.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 연락처 중복 체크
        if(!checkPhoneDuplicate(user.getUserPhone())) {
            throw new IllegalArgumentException("이미 등록된 연락처입니다.");
        }

        // 비밀번호 암호화
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));

        // 회원 등록 - 최종 DB에 저장
        userMapper.insertNewUser(user);

        // 포인트 초기화

        // 포인트 이력 추가
    }

    /**
     * 아이디 중복 체크 메서드
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디) <br>
     *      존재하지 않으면 true (사용 가능한 아이디)
     */
    @Override
    public boolean checkUserIdDuplicate(String userId) {
        // DB에 아이디가 존재하는 경우 (사용 불가능한 아이디)
        if(userMapper.checkUserIdExist(userId) > 0){
            return false;
        }
        return true;
    }

    /**
     * 연락처 중복 체크 메서드
     * @param userPhone 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디) <br>
     * 존재하지 않으면 true (사용 가능한 아이디)
     */
    @Override
    public boolean checkPhoneDuplicate(String userPhone) {
        // DB에 연락처가 존재하는 경우 (사용 불가능한 연락처)
        if(userMapper.checkUserPhoneExist(userPhone) > 0){
            return false;
        }
        return true;
    }

    /**
     * 이메일 인증코드 전송 메서드
     * @param email 세션에 저장된 클라이언트의 이메일 정보
     * @return null 또는 인증코드
     */
    @Override
    public String sendEmail(String email) {
        return "";
    }

    /**
     * 이메일 인증코드 확인 메서드
     * @param map Map<String, Object>
     * @return mapper 메서드에 map을 파라미터로 넘겨서 반환
     */
    @Override
    public int checkAuthKey(Map<String, Object> map) {
        return 0;
    }

    /**
     * 로그인 메서드
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @param password 클라이언트가 view에서 작성한 비밀번호
     */
    @Override
    public void login(String userId, String password) {

    }


    /**
     * 로그인 시 포인트 적립 메서드
     * @param userId 세션에 저장된 userId
     * @return 적립된 포인트
     */
    @Override
    public int addLoginPoint(String userId) {
        return 0;
    }
}
