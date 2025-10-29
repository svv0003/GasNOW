package project.gasnow.rating.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.rating.model.dto.Rating;

import java.util.Date;

@Mapper
public interface RatingMapper {

    /**
     * 주유소 평균 평점 조회
     */
    Double getAvgRating(String gsId);
    Date checkingInputRating(String gsId, String userId);
    void  addRating(Rating rating);
    /**
     * 오늘 이미 평점을 입력했는지 확인
     */

    /**
     * 평점 등록
     */
}

