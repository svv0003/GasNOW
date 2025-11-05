package project.gasnow.user.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.user.model.dto.ReviewItemDTO;

import java.util.List;

@Mapper
public interface GsRatingMapper {
    /**
     * 리뷰 목록 조회 메서드
     * @param userId service에서 넘겨받은 유저 아이디
     * @return
     */
    List<ReviewItemDTO> getMyReview(String userId);

    /**
     * 회원 탈퇴 메서드
     * @param userId service에서 넘겨받은 유저 아이디
     */
    void deleteMyReview(String userId);
}
