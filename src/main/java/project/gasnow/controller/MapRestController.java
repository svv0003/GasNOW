package project.gasnow.controller;

public class MapRestController{


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
