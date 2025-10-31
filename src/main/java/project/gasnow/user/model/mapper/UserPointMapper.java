package project.gasnow.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;

import java.util.List;

@Mapper
public interface UserPointMapper {
    /**
     * 회원가입 시 행 추가 메서드
     * @param userId User 객체에 저장된 userId
     * @return 추가된 행 개수
     */
    int insertNewUserPoint(String userId);

    /**
     * 포인트 적립/사용 시 point 변경 메서드
     * @param point total_earned 와 total_used 로 계산해 setter로 담겨진 현재 포인트
     */
    void updatePoint(UserPoint point);

    /**
     * 포인트 적립 시 total_earned 변경 메서드
     * @param point 적립된 포인트
     */
    void updateTotalEarned(UserPoint point);

    /**
     * 포인트 사용 시 total_used 변경 메서드
     * @param point 사용한 포인트
     */
    void updateTotalUsed(UserPoint point);

    /**
     * 회원의 현재 총 포인트 조회 메서드
     * @param userId 세션에 저장된 유저 아이디 (WHERE 조건절에서 사용)
     * @return 현재 총 포인트
     */
    int getCurrentPoint(String userId);

    /**
     * UserPointHistory 객체 반환 메서드
     * @param userId 유저 아이디
     * @return UserPointHistory 객체
     */
    UserPointHistory getPointHistoryById(String userId);

    /**
     * 포인트 적립/사용 시 point_history에 내역 추가 메서드
     * @param pointHistory 적립/사용 내역
     * @return 추가된 행의 개수
     */
    int insertPointHistory(UserPointHistory pointHistory);

    /**
     * 포인트 적립/사용 내역 조회
     * @param userId 세션에 저장된 유저 아이디 (WHERE 조건절에서 사용)
     * @return 포인트 내역 리스트
     */
    List<UserPointHistory> getPointHistoryList(String userId);
}
