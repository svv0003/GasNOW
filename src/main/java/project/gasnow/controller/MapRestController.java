package project.gasnow.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gasnow.common.util.SessionUtil;
import project.gasnow.favorite.model.dto.Favorite;
import project.gasnow.favorite.model.service.FavoriteService;
import project.gasnow.rating.model.dto.Rating;
import project.gasnow.rating.model.mapper.RatingMapper;
import project.gasnow.rating.model.service.RatingService;
import project.gasnow.user.model.dto.User;

import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor

public class MapRestController{

    public final RatingService ratingService;
    public final FavoriteService favoriteService;

    @GetMapping("/getRating")
    public double getAvgRating(@RequestParam String gsId) {
        return  ratingService.getAvgRating(gsId);
    }

    @GetMapping("/ratingCount")
    public int ratingCount(String gsId){
        return ratingService.ratingCount(gsId);
    }


    @GetMapping("/ratingDate")
    public Date checkingInputRating(@RequestParam String gsId, @RequestParam String userId) {
        return  ratingService.checkingInputRating(gsId, userId);
    }

    @PostMapping("/addRating")
    public String  addRating(@RequestBody Rating rating) {
        return  ratingService.addRating(rating);
    }


//      @GetMapping("/isFav")
//    public String getFavorite(@RequestParam String userId, @RequestParam String gsId){
//        return favoriteService.getFavorite(userId, gsId);
//    }

    @GetMapping("/isFav")
    public ResponseEntity<String> getFavorite(@RequestParam String gsId, HttpSession session) {
        User loginUser = SessionUtil.getLoginUser(session);
       if (loginUser == null) {

             return ResponseEntity.ok("0");
        }
         String result = favoriteService.getFavorite(loginUser.getUserId(), gsId);
        return ResponseEntity.ok(result);
     }

//    @PostMapping("/addFav")
//    public void addFavorite(@RequestBody Favorite favorite) {
//        favoriteService.addFavorite(favorite);
//    }

    @PostMapping("/addFav")
    public ResponseEntity<String> addFavorite(@RequestBody Favorite favorite, HttpSession session) {
        // 세션에서 로그인 사용자 정보 가져오기
        User loginUser = SessionUtil.getLoginUser(session);

        if (loginUser == null) {
            // 로그인되지 않았으면 에러 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        // favorite 객체에 userId를 세션 정보로 세팅
        favorite.setUserId(loginUser.getUserId());

        // 서비스 호출
        favoriteService.addFavorite(favorite);

        return ResponseEntity.ok("성공");
    }


    @GetMapping("/favList")
    public List<String> getFavoriteList(@RequestParam String userId) {
        List<String> favoriteList = favoriteService.getFavoriteList(userId);
        return favoriteList;
    }


}
