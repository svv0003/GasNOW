package project.gasnow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.rating.model.mapper.RatingMapper;
import project.gasnow.rating.model.service.RatingService;

import java.util.Date;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor

public class MapRestController{
    public final RatingService ratingService;

    @GetMapping("getRating")
    public double getAvgRating(@RequestParam String gsId) {
        return  ratingService.getAvgRating(gsId);
    }

    @GetMapping("ratingDate")
    Date checkingInputRating(@RequestParam String gsId, String userId) {
        return  ratingService.checkingInputRating(gsId, userId);
    }

    @PostMapping("addRating")
    String  addRating(@RequestBody Rating rating) {
        return  ratingService.addRating(rating);
    }
    // ============================================
    // 평점 관련 API
    // ============================================

    /**
     * 평점 조회
     * GET /api/map/info/review?gsId=xxxxx
     */
    /*
    @GetMapping("/info/review")
    public  getReviews(@RequestParam String gsId) {
        mapService.getRatingList(gsId);
        mapService.getAvgRating(gsId);
    }
*/
    /**
     * 평점 등록
     * POST /api/map/info/review
     * Body: { "gsId": "xxxxx", "rating": 5 }
     */
    /*
    @PostMapping("/info/review")
    public  addReview() {

        mapService.addRating(userId, gsId, rating);
        mapService.getAvgRating(gsId);

    }
     */
}
