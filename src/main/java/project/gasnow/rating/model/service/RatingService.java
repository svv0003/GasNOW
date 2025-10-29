package project.gasnow.rating.model.service;

import project.gasnow.rating.model.dto.Rating;

import java.util.Date;

public interface RatingService {
    /*
    // ============ 평점 ============
    getAvgRating(String gsId);
     getRatingList(String gsId);
    addRating(String userId, String gsId, Integer rating);

     */
    Double getAvgRating(String gsId);
    Date checkingInputRating(String gsId, String userId);
    String  addRating(Rating rating);

}
