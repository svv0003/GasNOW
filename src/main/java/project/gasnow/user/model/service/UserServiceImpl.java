package project.gasnow.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
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
        UserPoint point = User
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
}
