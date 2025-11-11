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
        Double avgRating = ratingMapper.getAvgRating(gsId);
        return avgRating != null ? avgRating : 0.0;
    }


    @Override
    public int ratingCount(String gsId){
        return ratingMapper.ratingCount(gsId);
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

                return "오늘 이 주유소에 이미 평점을 입력했습니다. ";
            }
        }

        ratingMapper.addRating(rating);
        return "평점이 등록되었습니다";
    }


}


