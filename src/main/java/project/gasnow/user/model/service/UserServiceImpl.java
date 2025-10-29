package project.gasnow.user.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gasnow.user.model.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;


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
