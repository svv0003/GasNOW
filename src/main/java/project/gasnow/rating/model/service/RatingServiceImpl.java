package project.gasnow.rating.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.rating.model.mapper.RatingMapper;

import java.util.Calendar;
import java.util.Date;

@Transactional
@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    private RatingMapper ratingMapper;


    @Override
    public double getAvgRating(String gsId) {
        return ratingMapper.getAvgRating(gsId);
    }

    @Override
    public Date checkingInputRating(String gsId, String userId) {
        return ratingMapper.checkingInputRating(gsId,userId);
    }


    @Override
    public String addRating(Rating rating) {

        Date createdAt = checkingInputRating(rating.getGsId(), rating.getUserId());
        if (createdAt != null) {
            Calendar calCreated = Calendar.getInstance();
            calCreated.setTime(createdAt);

            Calendar calToday = Calendar.getInstance();

            if (calCreated.get(Calendar.YEAR) == calToday.get(Calendar.YEAR)
                    && calCreated.get(Calendar.MONTH) == calToday.get(Calendar.MONTH)
                    && calCreated.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH)) {
                return "이미 평점을 입력했습니다";
            }
        }

        ratingMapper.addRating(rating);
        return "평점이 정상적으로 등록되었습니다";
    }




/*
@Override
public String addRating(Rating rating) {
    // 오늘 입력했는지 확인
    String check = checkingInputRating(rating.getGsId(), rating.getUserId());
    if ("이미 평점을 입력했습니다".equals(check)) {
        return "fail"; // 저장하지 않음
    }

    // DB에 저장
    ratingMapper.insertRating(rating);
    return "success";
}

 */

    /*

    // ============ 평점 ============


    @Override
    public boolean addRating(String userId, String gsId, Integer rating) {
        // 오늘 이미 평점을 입력했는지 확인
        int count = mapMapper.checkTodayRating(userId, gsId);
        if (count > 0) {
            return false; // 이미 오늘 작성함
        }

        GsRating gsRating = new GsRating();
        gsRating.setUserId(userId);
        gsRating.setGsId(gsId);
        gsRating.setRating(rating);

        return mapMapper.insertRating(gsRating) > 0;
    }

     */
}
