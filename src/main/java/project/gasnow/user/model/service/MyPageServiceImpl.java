package project.gasnow.user.model.service;

import org.springframework.stereotype.Service;
import project.gasnow.user.model.dto.ReviewItemDTO;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.mapper.GsRatingMapper;

import java.util.List;

@Service
public class MyPageServiceImpl implements MyPageService {

    private final GsRatingMapper gsRatingMapper;

    public MyPageServiceImpl(GsRatingMapper gsRatingMapper) {
        this.gsRatingMapper = gsRatingMapper;
    }

    /**
     * 나의 리뷰 조회 메서드
     * @param userId 세션에 저장된 유저 아이디
     * @return 리뷰 리스트(List)
     */
    @Override
    public List<ReviewItemDTO> getMyReview(String userId) {
        List reviewList = gsRatingMapper.getMyReview(userId);
        return List.of();
    }

    /**
     *
     * @param userId 세션에 저장된 유저 아이디
     * @return
     */
    @Override
    public UserPoint getUserPoint(String userId) {
        return null;
    }

    @Override
    public User getUserInfo(String userId) {
        return null;
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {

    }

    @Override
    public void withdrawUser(String userId) {

    }
}
