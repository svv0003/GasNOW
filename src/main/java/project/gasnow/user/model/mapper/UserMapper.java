package project.gasnow.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.user.model.dto.User;

import java.util.List;

/**
 * 회원가입, 회원 정보, 탈퇴 관련 Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 회원가입 메서드
     * @param user 클라이언트 정보
     */
    void insertNewUser(User user);

    /**
     * 회원가입 - 아이디 중복 체크 메서드
     * @param userId 클라이언트가 입력한 아이디 / DB의 WHERE 절에서 사용될 파라미터
     * @return 클라이언트가 입력한 아이디와 일치하는 테이블 행의 개수
     */
    int checkUserIdExist(String userId);

    /**
     * 회원가입 - 연락처 중복 체크 메서드
     * @param userPhone 클라이언트가 입력한 연락처 / DB의 WHERE 절에서 사용될 파라미터
     * @return 클라이언트가 입력한 아이디와 일치하는 테이블 행의 개수
     */
    int checkUserPhoneExist(String userPhone);

    /**
     * 마이페이지 - 회원정보 조회 메서드
     * @param userId 세션에 저장되어 있는 회원 아이디
     * @return 회원 객체
     */
    User getUserInfoById(String userId);

    /**
     * 마이페이지 - 비밀번호 변경 메서드 (현재 비밀번호 일치 여부 확인)
     * @param user 세션에 저장되어 있는 유저 객체
     * @return DB에 저장되어 있는 암호화된 유저 비밀번호
     */
    String getCurrentPassword(User user);

    /**
     * 마이페이지 - 비밀번호 변경 메서드 (새로운 비밀번호 저장)<br>
     * Service에서 새로 입력받은 비밀번호를 먼저 유저 객체의 비밀번호로 저장한 뒤에(setter)<br>
     * 이 메서드로 DB에 업데이트
     * @param user 세션에 저장되어 있는 유저 객체
     * @return 업데이트 된 행의 개수 / Service 에서 boolean으로 정상작동 했는지 판단하는 용도로 사용
     */
    int updateUserPassword(User user);


    /**
     * 마이페이지 - 회원 탈퇴 메서드
     * @param user 세션에 저장되어 있는 유저 객체
     * @return 삭제된 행의 개수 / Service 에서 boolean으로 정상작동 했는지 판단하는 용도로 사용
     */
    int deleteUser(User user);
}
