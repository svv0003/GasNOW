package project.gasnow.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.gasnow.user.model.dto.ReviewItemDTO;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;
import project.gasnow.user.model.mapper.FavGsMapper;
import project.gasnow.user.model.mapper.GsRatingMapper;
import project.gasnow.user.model.mapper.UserMapper;
import project.gasnow.user.model.mapper.UserPointMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserMapper userMapper;
    private final UserPointMapper userPointMapper;
    private final FavGsMapper favGsMapper;
    private final GsRatingMapper gsRatingMapper;


    /**
     * 나의 리뷰 내역 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return 리뷰 리스트(List)
     */
    @Override
    public List<ReviewItemDTO> getMyReview(String userId) {
        List reviewList = gsRatingMapper.getMyReview(userId);
        return reviewList;
    }

    /**
     * 보유 포인트 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return UserPoint 객체
     */
    @Override
    public UserPoint getUserPoint(String userId) {
        UserPoint userPoint = userPointMapper.getUserPointById(userId);

        if(userPoint == null) {
            throw new IllegalArgumentException("포인트 정보가 없습니다.");
        }

        log.info("포인트 조회 - userId: {}, point: {}", userId, userPoint.getCurrentPoint());

        return userPoint;
    }

    /**
     * 포인트 변동 내역 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return 변동 내역 리스트(List)
     */
    @Override
    public List<UserPointHistory> getUserPointHistory(String userId) {
        List<UserPointHistory> pointHistoryList = userPointMapper.getPointHistoryList(userId);
        return pointHistoryList;
    }

    /**
     * 회원정보 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return User 객체
     */
    @Override
    public User getUserInfo(String userId) {
        User user = userMapper.getUserById(userId);

        if(user == null) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }

        user.setUserPassword(null);  // 비밀번호는 반환 X

        log.info("회원정보 조회 - userId: {}", userId);
        return user;
    }

    /**
     *
     * @param userId 세션에 저장된 유저 아이디
     * @param oldPassword 기존 비밀번호 (클라이언트 입력)
     * @param newPassword 변경할 비밀번호 (클라이언트 입력)
     */
    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {

    }

    @Override
    public void withdrawUser(String userId) {

    }
}
