package project.gasnow.user.model.service;

import project.gasnow.user.model.dto.ReviewItemDTO;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;

import java.util.List;

public interface MyPageService {

    /**
     * 나의 리뷰 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return 리뷰 리스트(List)
     */
    List<ReviewItemDTO> getMyReview(String userId);

    /**
     * 보유 포인트 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return UserPoint 객체
     */
    UserPoint getUserPoint(String userId);

    /**
     * 포인트 변동 내역 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return UserPointHistory 객체
     */
    List<UserPointHistory> getUserPointHistory(String userId);

    /**
     * 회원정보 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return User 객체
     */
    User getUserInfo(String userId);

    /**
     * 비밀번호 변경 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @param oldPassword 기존 비밀번호 (클라이언트 입력)
     * @param newPassword 변경할 비밀번호 (클라이언트 입력)
     * @return 업데이트 된 행의 개수
     */
    int changePassword(String userId, String oldPassword, String newPassword);

    /**
     * 회원 탈퇴 메서드
     * @param userId 세션에 저장된 유저 아이디
     */
    public void withdrawUser(String userId);
}
